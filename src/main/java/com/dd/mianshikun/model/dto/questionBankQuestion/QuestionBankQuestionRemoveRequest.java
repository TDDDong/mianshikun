package com.dd.mianshikun.model.dto.questionBankQuestion;

import lombok.Data;

import java.io.Serializable;

/**
 * 移除题目题库关系请求
 */
@Data
public class QuestionBankQuestionRemoveRequest implements Serializable {
    /**
     * 题库id
     */
    private Long questionBankId;
    /**
     * 题目id
     */
    private Long questionId;

    private static final long serialVersionUID = 1L;
}
