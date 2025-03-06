package com.dd.mianshikun.model.enums;

import java.util.Objects;

public enum AiModelEnum {
    ZHI_PU(1, "ZHI_PU"),

    DEEPSEEK(2, "DEEPSEEK")
    ;


    private Integer key;

    private String model;

    AiModelEnum(Integer key, String model) {
        this.key = key;
        this.model = model;
    }

    public Integer getKey() {
        return key;
    }

    public String getModel() {
        return model;
    }

    public static String getModelByKey(Integer key) {
        for (AiModelEnum e : AiModelEnum.values()) {
            if (Objects.equals(key, e.getKey())) {
                return e.getModel();
            }
        }
        return "";
    }
}
