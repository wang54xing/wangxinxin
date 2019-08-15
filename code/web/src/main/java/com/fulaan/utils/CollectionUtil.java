package com.fulaan.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by wang_xinxin on 2015/4/25.
 */
public class CollectionUtil {

    public static <T> Iterable<T> safeList(Iterable<T> iterable) {
        return iterable == null ? Collections.<T> emptyList() : iterable;
    }

    public static List<?> removeDuplicateWithOrder(List<?> list) {
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            Object element = iter.next();
            if (set.add(element))
                newList.add(element);
        }
        list.clear();
        list.addAll(newList);
        return list;
    }

    public static Integer[] getDistinct(int num[]) {
        List<Integer> list = new java.util.ArrayList<Integer>();
        for (int i = 0; i < num.length; i++) {
            if (!list.contains(num[i])) {
                list.add(num[i]);
            }
        }
        return list.toArray(new Integer[0]);
    }
}
