package net.fdxxw.tools.template.core.compiler.analysis;

import java.util.LinkedList;
import java.util.Map;

/**
 * 词法分析类
 * created by fdxxw on 2018/04/16
 */
public class LexicalAnalysis {

    /**
     * Normal 普通状态, 其它状态的中转
     * Text 普通文本状态
     * Space 空格
     * Expression 表达式
     * LeftDelimiter 左分界符
     * RightDelimiter 右分界符
     */
    public static enum State {
        Normal, Text, Space, Expression, LeftDelimiter, RightDelimiter
    }

    /**
     * 状态
     */
    private State state = State.Normal;
    /**
     * 上一个状态
     */
    private State preState;

    private final LinkedList<Token> tokenBuffer = new LinkedList<>();

    private StringBuilder readerBuffer = new StringBuilder();

    /**
     * 上一个token创建完成后,刷新缓存
     * @param c
     */
    public void refreshBuffer(char c) {

        //readerBuffer = new StringBuilder();
        readerBuffer.delete(0, readerBuffer.length());
        readerBuffer.append(c);
    }

    /**
     * 创建token
     * @param type
     */
    public void createToken(Token.Type type) {
        Token token = new Token(type, readerBuffer.toString());
        tokenBuffer.add(token);
    }

    /**
     * 扫描字符串
     * @param c
     * @return
     */
    public boolean readerChar(char c) throws Exception{

        boolean moveCursor = true; //读完当前字符,是否读取下一个字符

        Token.Type createType = null;

        switch(state) {
            case Normal: {
                if(preState != State.LeftDelimiter && c == '{') {
                    state = State.LeftDelimiter;
                } else if(preState == State.Expression && c == '}') {
                    state = State.RightDelimiter;

                } else if(preState == State.LeftDelimiter) {
                    state = State.Expression;
                } else {
                    state = State.Text;
                }

                preState = null;

                refreshBuffer(c);

                break;

            }

            case Text: {

                if(c != '{') {
                    readerBuffer.append(c);
                } else {
                    createType = Token.Type.Text;
                    state = State.Normal;
                    moveCursor = false;
                }

                break;

            }

            case Space: {
                break;
            }

            case Expression: {
                if(c != '}') {
                    readerBuffer.append(c);
                } else {
                    createType = Token.Type.Expression;
                    state = State.Normal;
                    preState = State.Expression;
                    moveCursor = false;
                }
                break;
            }

            case LeftDelimiter: {
                if(c == '{') {
                    preState = State.LeftDelimiter;
                    createType = Token.Type.LeftDelimiter;
                } else {
                    createType = Token.Type.Text;
                }
                readerBuffer.append(c);
                state = State.Normal;
                break;
            }

            case RightDelimiter: {
                if(c == '}') {
                    createType = Token.Type.RightDelimiter;
                } else {
                    throw new Exception("语法错误");
                }

                readerBuffer.append(c);
                state = State.Normal;
                break;

            }

            default: {
                break;
            }

        }

        if(createType != null) {
            createToken(createType);
        }

        return moveCursor;
    }

    public LinkedList<Token> getTokenBuffer() {
        return tokenBuffer;
    }



    public static void render(String template, Map<String, String> map) {
        long start = System.currentTimeMillis();

        char[] chars = template.toCharArray();
        LexicalAnalysis lex = new LexicalAnalysis();
        StringBuilder sb = new StringBuilder();

        try {
            for(int i=0; i < chars.length;) {
                if(lex.readerChar(chars[i])) {
                    i++;
                }
            }

            for(int i = 0; i < 100000; i++) {

                sb.delete(0, sb.length());

                for(Token token : lex.getTokenBuffer()) {
                    if(token.getType() == Token.Type.Text) {
                        sb.append(token.getValue());
                    } else if(token.getType() == Token.Type.Expression) {
                        sb.append(map.get(token.getValue().trim()));
                    } else {
                        continue;
                    }
                }
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println(sb.toString());

        System.out.println((System.currentTimeMillis() - start) + " ms");
    }


}
