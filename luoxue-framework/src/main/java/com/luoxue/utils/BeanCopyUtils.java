package com.luoxue.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {
    private BeanCopyUtils(){}
    public static <T> T beanCopy(Object source, Class<T> clazz){
        T result = null;
        try {
            result = clazz.newInstance();
            BeanUtils.copyProperties(source, result);
        } catch (Exception e) {
           e.printStackTrace();
        }
        return result;
    }
    public static <k,T> List<T> beanCopyList(List<k> source, Class<T> clazz){
        return source.stream()
                .map(o -> beanCopy(o, clazz))
                .collect(Collectors.toList());
    }
}
