package net.fdxxw.tools.template.core.compiler.analysis;

/**
 * 词法分析的产物(单词), 包含类型和语素两个属性
 * created by fdxxw on 2018/04/16
 */
public class Token {

    public static enum Type{
        Text, Space, LeftDelimiter, RightDelimiter // 普通文本, 空白字符, 左分界符{, 右分界符}
    }

    private Type type;  //类型

    private String value; //语素

    public Token(Type type, String value) {
        //TODO
        this.type = type;
        this.value = value;
    }
}
