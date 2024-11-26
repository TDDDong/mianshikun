package com.dd.mianshikun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dd.mianshikun.model.entity.Post;
import com.dd.mianshikun.model.entity.Question;

import java.util.Date;
import java.util.List;

/**
* @author 12618
* @description 针对表【question(题目)】的数据库操作Mapper
* @createDate 2024-11-20 22:36:13
* @Entity generator.domain.Question
*/
public interface QuestionMapper extends BaseMapper<Question> {

    /**
     * 查询问题列表（包括已被删除的数据）
     */
    List<Question> listQuestionWithDelete(Date minUpdateTime);

}




