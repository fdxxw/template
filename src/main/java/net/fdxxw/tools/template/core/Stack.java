package net.fdxxw.tools.template.core;

/**
 * <p>栈</p>
 * created by fdxxw on 2018/04/14
 * @param <E>
 */
public class Stack<E> {
    /* 栈元素数组 */
    private Object[] stack;
    /* 栈的默认大小 */
    private static final int INIT_SIZE = 2;
    /* 栈顶元素索引 */
    private int index;
    /* 栈扩展系数,默认两倍 */
    private int factor = 2;

    public Stack() {
        this.stack = new Object[INIT_SIZE];
        this.index = -1;

    }

    public Stack(int initSize) {
        if(initSize < 0) {
            throw new IllegalArgumentException("参数szie不能为负数");
        }
        this.stack = new Object[initSize];
        this.index = -1;
    }

    public Stack(int initSize, int factor) {
        this(initSize);
        this.factor = factor;
    }

    /* 入栈 */
    public synchronized void push(E e) {
        if(isFull()) {
            Object[] temp = stack;
            stack = new Object[factor * stack.length];
            System.arraycopy(temp, 0, stack, 0, temp.length);
        }
        index++;
        stack[index] = e;

    }

    /* 出栈 */
    public synchronized E pop() {
        if(!isEmpty()) {
            E e = peek();
            stack[index--] = null;
            return e;
        }

        return null;
    }
    /* 取出栈顶元素 */
    public E peek() {
        if(!isEmpty()) {
            return (E)stack[index];
        }
        return null;
    }
    /* 栈是否为空 */
    public boolean isEmpty() {
        return index == -1;
    }
    /* 栈是否满了 */
    public boolean isFull() {
        return index > this.stack.length -1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        while (!isEmpty()) {
            sb.insert(0, pop());
        }
        return sb.toString();
    }
}
