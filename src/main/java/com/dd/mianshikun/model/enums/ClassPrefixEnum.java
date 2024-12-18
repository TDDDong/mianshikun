package com.dd.mianshikun.model.enums;

/**
 * 类名前缀枚举
 * 用于HotKeyCheck中的prefix赋值
 */
public enum ClassPrefixEnum {

    QUESTION("question", "question_detail_"),

    QUESTION_BANK("questionBank", "question_bank_");

    private final String key;
    private final String prefix;

    ClassPrefixEnum(String key, String prefix){
        this.key = key;
        this.prefix = prefix;
    }

    public static String getPrefixByKey(String key) {
        for (ClassPrefixEnum prefixEnum : ClassPrefixEnum.values()) {
            if (prefixEnum.getKey().equals(key)) {
                return prefixEnum.prefix;
            }
        }
        return null;
    }

    public String getKey() {
        return key;
    }

    public String getPrefix() {
        return prefix;
    }
}
