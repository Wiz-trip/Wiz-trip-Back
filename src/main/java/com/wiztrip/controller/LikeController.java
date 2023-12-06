package com.wiztrip.controller;

import com.wiztrip.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("like")
public class LikeController {

     private LikeService likeService;



}

