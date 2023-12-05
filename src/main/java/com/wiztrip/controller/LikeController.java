package com.wiztrip.controller;

import com.wiztrip.dto.TripDto;
import com.wiztrip.service.LikeService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@NoArgsConstructor
@RestController("like")
public class LikeController {

     private LikeService likeService;



}

