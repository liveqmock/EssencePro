package com.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CollectionUtil {

    public final static int COUNT = 15;

    /**
     * Joins the elements of the provided array into a single string containing
     * a list of CSV elements.
     * 
     * @param list
     *            The list of values to join together.
     * @param separator
     *            The separator character.
     * @return The CSV text.
     */
    // 把list集合合并成为一个字符串，用separator进行各个元素之间的区分。
    public static String join(String separator, Collection<String> list) {
        StringBuffer csv = new StringBuffer();
        Iterator<String> it = list.iterator();
        for (int i = 0; it.hasNext(); i++) {
            if (i > 0) {
                csv.append(separator);
            }
            csv.append((String) it.next());
        }
        return csv.toString();
    }

    /**
     * 
     * @description : 判断obj是否在集合list中
     * @author : 玄承勇
     * @datetime: 2014-5-2 下午12:00:28
     * @param:
     * @param:
     * @return ： boolean
     */
    @SuppressWarnings("rawtypes")
    public static boolean ArrayCheck(List Array, Object obj) {
        if (Array == null)
            return false;
        int size = Array.size();
        for (int i = 0; i < size; i++) {
            if (obj == null || Array.get(i) == null) {
                return false;
            } else {
                if (obj.equals(Array.get(i)))
                    return true;
            }
        }
        return false;
    }

    @SuppressWarnings("rawtypes")
    public static boolean inArray(Object obj, List arr) {
        int size = arr.size();
        for (int i = 0; i < size; i++) {
            if (arr.get(i).equals(obj)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static List keepCount(List list) {
        if (list == null) {
            return null;
        } else if (list.size() <= COUNT) {
            return list;
        } else {
            List res = new ArrayList();
            for (int i = 0; i < COUNT; i++) {
                res.add(list.get(i));
            }
            return res;
        }
    }

    /**
     * 判断一个集合是不是为空
     * 
     * @param c
     * @return
     * @author 玄承勇
     * @date 2014-7-2 上午8:16:58
     */
    public static boolean isEmpty(Collection<?> c) {
        if (c == null) {
            return true;
        } else if (c.size() == 0) {
            return true;
        }
        return false;
    }

}
