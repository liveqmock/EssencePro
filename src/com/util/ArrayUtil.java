package com.util;

public class ArrayUtil {

    public static int SumIntArray(int[] args) {
        if (args == null)
            return 0;
        int sum = 0;
        for (int i = 0; i < args.length; i++) {
            sum += args[i];

        }
        return sum;
    }

    public static boolean ArrayCheck(String[] Array, String str) {
        if (Array == null)
            return false;
        for (int i = 0; i < Array.length; i++) {
            if (str == null || Array[i] == null) {
                return false;
            } else {
                if (str.trim().equals(Array[i].trim()))
                    return true;
            }

        }
        return false;
    }

    public static boolean isEmptyArr(String[] strArr) {
        if (strArr == null) {
            return true;
        } else if (strArr.length == 0) {
            return true;
        }
        return false;
    }

}
