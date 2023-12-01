package com.wiztrip.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ListDto {
    Integer listSize;
    List<?> list = new ArrayList<>();

    public ListDto(List<?> list) {
        this.list = list;
        this.listSize = list.size();
    }
}
