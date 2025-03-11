package com.dd.mianshikun.model.dto.question;

import lombok.Data;

import java.io.Serializable;

@Data
public class AnswerAIGenerateRequest implements Serializable {
    /**
     * 题目
     */
    private String question;

    /**
     * 选择的模型 默认为1(智谱ai)
     */
    private int modelKey = 1;

    private static final long serialVersionUID = 1L;
}
