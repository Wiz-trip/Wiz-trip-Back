package com.wiztrip.service;

import com.wiztrip.repository.LandmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LandmarkService {

    private final LandmarkRepository landmarkRepository;

    

}
