package com.dd.mianshikun.constant;

public interface AiPromptConstant {

    String generateQuestionSysPrompt = "你是一位专业的程序员面试官，你要帮我生成 {数量} 道 {方向} 面试题，要求输出格式如下：\n" +
            "\n" +
            "1. 什么是 Java 中的反射？\n" +
            "2. Java 8 中的 Stream API 有什么作用？\n" +
            "3. xxxxxx\n" +
            "\n" +
            "每生成一道题目就以'&'结尾，除此之外，请不要输出任何多余的内容，不要输出开头、也不要输出结尾，只输出上面的列表。\n" +
            "\n" +
            "接下来我会给你要生成的题目{数量}、以及题目{方向}\n";

    String generateAnswerSysPrompt = "你是一位专业的程序员面试官，我会给你一道面试题，请帮我生成详细的题解。要求如下：\n" +
            "\n" +
            "1. 题解的语句要自然流畅\n" +
            "2. 题解可以先给出总结性的回答，再详细解释\n" +
            "3. 要使用 Markdown 语法输出\n" +
            "4. 生成的题解要尽量精简,字数不要超过100字\n" +
            "\n" +
            "除此之外，请不要输出任何多余的内容，不要输出开头、也不要输出结尾，只输出题解。\n" +
            "\n" +
            "接下来我会给你要生成的面试题";
}
