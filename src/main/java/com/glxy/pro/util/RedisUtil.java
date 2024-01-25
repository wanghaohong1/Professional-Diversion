package com.glxy.pro.util;


import java.util.ArrayList;
import java.util.List;

/**
 * @author Alonha
 * @create 2023-10-14-14:33
 */
public class RedisUtil<E> {


    /**
     * 获取未命中的id
     * @param ids 查数据库获取到的id
     * @param redisList 根据ids查缓存，获得的查询结果
     * @return
     */
    public List<String> unGetIds(List<String> ids, List<E> redisList) {
        // 因为要频繁指定索引获取值，链表查询速率低，转为数组
        Object[] idsArray = ids.toArray();
        // 存储未命中id在ids中的索引位置
        ArrayList<Integer> unGetList = new ArrayList<>();
        // 存储未命中的id
        ArrayList<String> unGetIds = new ArrayList<>();
        // 获取所有未命中id在ids中的索引位置
        int index = 0;
        for (E e : redisList) {

            if (e == null) {
                unGetList.add(index);
            }
            index++;
        }
        // 遍历ids获取未命中的id
        for (Integer i : unGetList) {
            unGetIds.add(idsArray[i].toString());
        }
        return unGetIds;
    }

    // 那要是在获取完未命中的数据之后，有一个线程，把这个数据删掉了（删的时候，也会删redis，那么不是看谁快的问题？）
    // 然后遍历student的时候存入缓存，那不就缓存了一个无用的缓存


    // 批量将未命中的数据存入缓存


}
