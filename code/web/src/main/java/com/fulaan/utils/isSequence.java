package com.fulaan.utils;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by wang_xinxin on 2018/6/8.
 */
public class isSequence {

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(8);
        list.add(9);
        list.add(10);
        list.add(12);
        list.add(13);
        list.add(15);


        List<Integer> list2 = new ArrayList<Integer>();
        list2.addAll(list);
        isSequence(list,2);
        list2.removeAll(list);
        if (list2!=null && list2.size()!=0) {
            for (Integer t : list2) {
                System.out.println(t);
            }
        }
    }

    // 参数 ，List集合：想要查找连续数字的集合，max：想要找几个连续数字

// 返回值：返回删除连续数字之后的集合，（要是想要获取连续序列的集合可以自行修改返回值就是多个LIst<Integer> seq ）

    private static List<Integer> isSequence(List<Integer> pokers, int max) {


        Collections.sort(pokers); // 先将集合中的元素排序
        if(!CollectionUtils.isEmpty(pokers) && pokers.size() >= max){



            for(int index = 0; index < pokers.size(); index++){
                List<Integer> seq = new ArrayList<Integer>(max);

                int currValue = pokers.get(index);

                for(int j = currValue; j < currValue + max; j ++){

                    if(pokers.contains(j)){ //  判断当前元素的下一个是否在list集合里面如果存在将 为true存在map里
                        seq.add(j); // 将连续的存在seq中
                    }
                }
                if(seq.size() == max){

                    for(Integer num : seq){

                        pokers.remove(pokers.indexOf(num)); // 根据元素的索引位置删除元素 ， 只想删除一个元素seq对应的一个元素，并且不删除重复元素的时候，

// 我使用的是poker.remove(int index) 根据索引删除（

//                        注释：1，不可以直接使用pokers.remove(Object o) 这个方法，会发生数组越界，
//
//                        2，也不可以使用removeAll(Collenction<T> c)
//
//                        举例:List<Integer> a = new ArrayList<>(Array.asList(new Integer(1,2,3,4,5,6,7,1,1,1)));
//
//                        List<Integer> b = new ArrayList<>(Array.asList(new Integer(1)));
//
//                        用a.removeAll(b),就会把集合a里面的所有的元素1，都会删除。

                    }
                }
            }
        }
        return pokers;
    }

}
