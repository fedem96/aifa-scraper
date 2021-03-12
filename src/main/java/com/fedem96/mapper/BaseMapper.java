package com.fedem96.mapper;

import com.fedem96.dto.BaseDto;
import com.fedem96.model.BaseEntity;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseMapper<T extends BaseEntity, dtoT extends BaseDto> {

    public abstract dtoT convert(T obj);

    public List<dtoT> convert(Collection<T> list){
        return list.stream().map(this::convert).collect(Collectors.toList());
    }
}
