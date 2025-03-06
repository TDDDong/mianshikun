package com.dd.mianshikun.model.dto.question;

import lombok.Data;

import java.io.Serializable;

@Data
public class QuestionAIGenerateRequest implements Serializable {
    /**
     * 题目类型，比如 Java
     */
    private String questionType;

    /**
     * 题目数量，比如 10
     */
    private int number = 10;

    /**
     * 选择的模型 默认为1(智谱ai)
     */
    private int modelKey = 1;

    private static final long serialVersionUID = 1L;
}
