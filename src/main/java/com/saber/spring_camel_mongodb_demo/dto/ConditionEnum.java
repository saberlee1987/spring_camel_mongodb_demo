package com.saber.spring_camel_mongodb_demo.dto;

public enum ConditionEnum {
    UNKNOWN(0,"unknown"),
    GreatThan(1,"gt"),
    GreatEqual(2,"gte"),
    LESSThan(3,"lt"),
    LessEqual(4,"lte"),
    Equal(5,"eq");

    private final int code;
    private final String text;

    ConditionEnum(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}
