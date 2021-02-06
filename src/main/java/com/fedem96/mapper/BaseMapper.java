package com.fedem96.mapper;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseMapper<T, dtoT> {

    public abstract dtoT convert(T obj);

    public List<dtoT> convert(List<T> list){
        return list.stream().map(this::convert).collect(Collectors.toList());
    }
}
