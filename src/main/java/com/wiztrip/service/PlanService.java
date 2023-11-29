package com.wiztrip.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PlanService {

    @Transactional
    public void createPlan() {

    }

    public void getPlan() {

    }

    public void getAllPlan() {

    }

    @Transactional
    public void updatePlan() {

    }

    @Transactional
    public void deletePlan() {

    }
}
