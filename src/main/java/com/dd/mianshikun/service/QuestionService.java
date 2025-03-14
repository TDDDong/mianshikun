package com.dd.mianshikun.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dd.mianshikun.model.dto.question.QuestionQueryRequest;
import com.dd.mianshikun.model.entity.Question;
import com.dd.mianshikun.model.entity.User;
import com.dd.mianshikun.model.vo.QuestionVO;
import io.reactivex.Flowable;
import reactor.core.publisher.Flux;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 题目服务
 *
  
 */
public interface QuestionService extends IService<Question> {

    /**
     * 校验数据
     *
     * @param question
     * @param add 对创建的数据进行校验
     */
    void validQuestion(Question question, boolean add);

    /**
     * 获取查询条件
     *
     * @param questionQueryRequest
     * @return
     */
    QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);
    
    /**
     * 获取题目封装
     *
     * @param question
     * @param request
     * @return
     */
    QuestionVO getQuestionVO(Question question, HttpServletRequest request);

    /**
     * 分页获取题目封装
     *
     * @param questionPage
     * @param request
     * @return
     */
    Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request);

    /**
     * 分页获取题目列表（仅管理员可用）
     *
     * @param questionQueryRequest
     * @return
     */
    Page<Question> listQuestionByPage(QuestionQueryRequest questionQueryRequest);

    /**
     * 从 ES 查询题目
     *
     * @param questionQueryRequest
     * @return
     */
    Page<Question> searchFromEs(QuestionQueryRequest questionQueryRequest);


    /**
     * 批量删除题目
     * @param questionIdList
     */
    void batchDeleteQuestions(List<Long> questionIdList);

    /**
     * 调用AI生成题目  同步
     */
    boolean aiGenerateQuestions(String questionType, int number, int modelKey, User user);

    /**
     * 调用AI生成题目  流式
     */
    Flowable<Character> aiStreamGenerateQuestions(String questionType, int number, int modelKey);

    /**
     * 调用AI生成答案  流式
     */
    Flowable<Character> aiStreamGenerateAnswer(String question, int modelKey);
}
