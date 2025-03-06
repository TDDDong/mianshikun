package com.dd.mianshikun.strategy;

import cn.hutool.core.collection.CollUtil;
import com.dd.mianshikun.common.ErrorCode;
import com.dd.mianshikun.exception.BusinessException;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionChoice;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("DEEPSEEK")
public class DeepSeekChatStrategyImpl implements AiChatStrategy {
    @Resource
    private ArkService arkService;

    // 较稳定的随机数
    private static final double STABLE_TEMPERATURE = 0.05d;

    // 不稳定的随机数
    private static final double UNSTABLE_TEMPERATURE = 0.99d;

    @Override
    public String doSyncStableRequest(String systemMessage, String userMessage) {
        return doRequest(systemMessage, userMessage, STABLE_TEMPERATURE);
    }

    @Override
    public String doSyncUnstableRequest(String systemMessage, String userMessage) {
        return doRequest(systemMessage, userMessage, UNSTABLE_TEMPERATURE);
    }

    private String doRequest(String systemMessage, String userMessage, Double temperature) {
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage systemChatMessage = ChatMessage.builder().role(ChatMessageRole.SYSTEM).content(systemMessage).build();
        ChatMessage userChatMessage = ChatMessage.builder().role(ChatMessageRole.USER).content(userMessage).build();
        messages.add(systemChatMessage);
        messages.add(userChatMessage);
        return doRequest(messages, temperature);
    }


    public String doRequest(List<ChatMessage> messages, Double temperature) {
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model("deepseek-v3-241226")
                .messages(messages)
                .temperature(temperature)
                .build();
        List<ChatCompletionChoice> choices = arkService.createChatCompletion(chatCompletionRequest).getChoices();
        if (CollUtil.isNotEmpty(choices)) {
            return (String) choices.get(0).getMessage().getContent();
        }
        throw new BusinessException(ErrorCode.OPERATION_ERROR, "DeepSeek 调用失败，没有返回结果");
    }
}
