package com.wiztrip.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ListDto<T> {
    @Schema(description = "list의 generic type",example = "~~ResponseDto")
    String type;
    Integer listSize;
    List<T> list = new ArrayList<>();

    public ListDto(List<T> list) {
        this.type = !list.isEmpty()?list.get(0).getClass().getSimpleName():null;
        this.listSize = list.size();
        this.list = list;
    }
}
