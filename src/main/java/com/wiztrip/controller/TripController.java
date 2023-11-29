package com.wiztrip.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("trips")
public class TripController {

    @PostMapping
    public void createTrip() {

    }

    @GetMapping
    public void getTrip() {

    }

    @PatchMapping
    public void updateTrip() {

    }

    @DeleteMapping
    public void deleteTrip() {

    }




}
