package com.dd.mianshikun.model.dto.questionBank;

import com.dd.mianshikun.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 查询题库请求
 *
  
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionBankQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * id
     */
    private Long notId;

    /**
     * 标题
     */
    private String title;

    /**
     * 标题
     */
    private String searchText;

    /**
     * 描述
     */
    private String description;

    /**
     * 图片
     */
    private String picture;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 是否需要关联查询题目列表
     */
    private boolean needQueryQuestionList;

    private static final long serialVersionUID = 1L;
}