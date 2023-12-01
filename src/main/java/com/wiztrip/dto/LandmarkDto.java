package com.wiztrip.dto;

import com.wiztrip.constant.Address;
import com.wiztrip.constant.Image;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter @Setter
public class LandmarkDto {
    private Long id;
    private String name;
    private Address address;
    private List<Image> imageList;

}
