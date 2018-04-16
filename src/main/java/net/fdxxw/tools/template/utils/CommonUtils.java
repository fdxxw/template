package net.fdxxw.tools.template.utils;

public class CommonUtils {
    /* 清空字符数组 */
    public static void clearCharArray(char[] array) {
        if(array != null) {
            int length = array.length;
            for(int i=0; i < length; i++) {
                array[i] = '\u0000'; //空字符
            }
        }
    }
}
