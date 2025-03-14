package com.dd.mianshikun.strategy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.dd.mianshikun.common.ErrorCode;
import com.dd.mianshikun.exception.BusinessException;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.*;
import io.reactivex.Flowable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service("ZHI_PU")
public class ZhiPuChatStrategyImpl implements AiChatStrategy {
    @Resource
    private ClientV4 clientV4;

    // 较稳定的随机数
    private static final float STABLE_TEMPERATURE = 0.05f;

    // 不稳定的随机数
    private static final float UNSTABLE_TEMPERATURE = 0.99f;

    /**
     * 同步调用（答案较稳定）
     *
     * @param systemMessage
     * @param userMessage
     * @return
     */
    @Override
    public String doSyncStableRequest(String systemMessage, String userMessage) {
        return doSyncRequest(systemMessage, userMessage, STABLE_TEMPERATURE);
    }

    /**
     * 同步调用（答案较随机）
     *
     * @param systemMessage
     * @param userMessage
     * @return
     */
    @Override
    public String doSyncUnstableRequest(String systemMessage, String userMessage) {
        return doSyncRequest(systemMessage, userMessage, UNSTABLE_TEMPERATURE);
    }

    private String doSyncRequest(String systemMessage, String userMessage, Float temperature) {
        return doRequest(systemMessage, userMessage, Boolean.FALSE, temperature);
    }

    private String doRequest(String systemMessage, String userMessage, Boolean stream, Float temperature) {
        // 构造请求
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage systemChatMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), systemMessage);
        ChatMessage userChatMessage = new ChatMessage(ChatMessageRole.USER.value(), userMessage);
        messages.add(systemChatMessage);
        messages.add(userChatMessage);
        return doRequest(messages, stream, temperature);
    }


    private String doRequest(List<ChatMessage> messages, Boolean stream, Float temperature) {
        // 构造请求
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4)
                .stream(stream)
                .invokeMethod(Constants.invokeMethod)
                .temperature(temperature)
                .messages(messages)
                .build();
        ModelApiResponse invokeModelApiResp = clientV4.invokeModelApi(chatCompletionRequest);
        List<Choice> choices = invokeModelApiResp.getData().getChoices();
        if (CollUtil.isNotEmpty(choices)) {
            return (String) choices.get(0).getMessage().getContent();
        }
        throw new BusinessException(ErrorCode.OPERATION_ERROR, "智谱AI 调用失败，没有返回结果");
    }


    @Override
    public Flowable<Character> doStreamStableRequest(String systemMessage, String userMessage) {
        Flowable<ModelData> flowable = doStreamRequest(systemMessage, userMessage, STABLE_TEMPERATURE);
        Flowable<Character> newFlowable = flowable.map(chunk -> chunk.getChoices().get(0).getDelta().getContent())
                .map(message -> message.replaceAll("\\s", ""))
                .filter(StrUtil::isNotEmpty)
                .flatMap(message -> Flux.fromIterable(() -> message.chars().mapToObj(c -> (char) c).iterator()));
        return newFlowable;
    }

    @Override
    public Flowable<Character> doStreamUnstableRequest(String systemMessage, String userMessage) {
        Flowable<ModelData> flowable = doStreamRequest(systemMessage, userMessage, UNSTABLE_TEMPERATURE);
        ArrayList<Character> list = new ArrayList<>();
        Flowable<Character> newFlowable = flowable.map(chunk -> chunk.getChoices().get(0).getDelta().getContent()).flatMap(message -> {
            for (char c : message.toCharArray()) {
                list.add(c);
            }
            return Flux.fromIterable(list);
        });
        return newFlowable;
    }

    /**
     * 通用流式请求（简化消息传递）
     *
     * @param systemMessage
     * @param userMessage
     * @param temperature
     * @return
     */
    private Flowable<ModelData> doStreamRequest(String systemMessage, String userMessage, Float temperature) {
        // 构造请求
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage systemChatMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), systemMessage);
        ChatMessage userChatMessage = new ChatMessage(ChatMessageRole.USER.value(), userMessage);
        messages.add(systemChatMessage);
        messages.add(userChatMessage);
        return doStreamRequest(messages, temperature);
    }

    /**
     * 通用流式请求
     *
     * @param messages
     * @param temperature
     * @return
     */
    private Flowable<ModelData> doStreamRequest(List<ChatMessage> messages, Float temperature) {
        // 构造请求
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4)
                .stream(Boolean.TRUE)
                .invokeMethod(Constants.invokeMethod)
                .temperature(temperature)
                .messages(messages)
                .build();
        ModelApiResponse invokeModelApiResp = clientV4.invokeModelApi(chatCompletionRequest);
        return invokeModelApiResp.getFlowable();
    }
}
