//package com.wiztrip;
//
//import com.wiztrip.constant.Address;
//import com.wiztrip.constant.Category;
//import com.wiztrip.domain.*;
//import com.wiztrip.dto.LikeboxDto;
//import com.wiztrip.repository.*;
//import com.wiztrip.service.LikeboxService;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.event.EventListener;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//import org.springframework.validation.annotation.Validated;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//@Component
//@RequiredArgsConstructor
//@Validated
//public class Init {
//
//    private final UserRepository userRepository;
//
//    private final PasswordEncoder passwordEncoder;
//
//    private final TripRepository tripRepository;
//
//    private final TripUserRepository tripUserRepository;
//
//    private final PlanRepository planRepository;
//
//    private final MemoRepository memoRepository;
//
//    private final ReviewRepository reviewRepository;
//
//    private final LandmarkRepository landmarkRepository;
//
//    private final LikeboxService likeboxService;
//
//
//    @EventListener(ApplicationReadyEvent.class)
//    @Transactional
//    public void init() {
//        for (int i = 0; i < 5; i++) {
//            UserEntity user = new UserEntity();
//            user.setUsername("testusername" + i);
//            user.setPassword(passwordEncoder.encode("testpassword" + i));
//            user.setEmail("test" + i + "@test.com");
//            user.setNickname("testnickname" + i);
//            // todo: image 추가
//            userRepository.save(user);
//        }
//
//        for (int i = 0; i < 5; i++) {
//            TripEntity trip = new TripEntity();
//            trip.setDestination("testdestination" + i);
//            LocalDate localDate = LocalDate.of(2023, 12, 1+i);
//            trip.setStartDate(localDate);
//            trip.setFinishDate(localDate);
//
//            UserEntity owner = userRepository.findByUsername("testusername" + i).orElse(null);
//            trip.setOwner(owner);
//            trip.setFinished(true);
//
//            tripRepository.save(trip);
//        }
//
//        List<UserEntity> userEntityList = userRepository.findAll();
//        List<TripEntity> tripEntityList = tripRepository.findAll();
//
//        for (int i = 0; i < 5; i++){
//            for (int k = 0; k <= i; k++) {
//                TripUserEntity tripUser = new TripUserEntity();
//                tripUser.setUser(userEntityList.get(i));
//                tripUser.setTrip(tripEntityList.get(k));
//                tripUserRepository.save(tripUser);
//            }
//        }
//
//        for (int i = 0; i < 5; i++){
//            for (int k = 0; k <= i; k++)  {
//                PlanEntity plan = new PlanEntity();
//                plan.setUser(userEntityList.get(i));
//                plan.setTrip(tripEntityList.get(k));
//
//                Address address = new Address("testplanroadnameaddress" + i, "testplanlocaladdress" + i);
//                plan.setAddress(address);
//
//                plan.setContent("testplancontent" + k);
//
//                Category[] category = Category.values();
//                Random random = new Random();
//                Category selectCategory = category[random.nextInt(category.length)];
//                plan.setCategory(selectCategory);
//
//                LocalDate localDate = LocalDate.of(2023, 12, 1+k);
//                LocalTime localTime = LocalTime.of(1+k, 30);
//                LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
//                plan.setStartTime(localDateTime);
//                plan.setFinishTime(localDateTime);
//
//                planRepository.save(plan);
//            }
//        }
//
//        for (int i = 0; i < 5; i++){
//            for (int k = 0; k <= i; k++)  {
//                MemoEntity memo = new MemoEntity();
//                memo.setUser(userEntityList.get(i));
//                memo.setTrip(tripEntityList.get(k));
//
//                memo.setTitle("testmemotitle" + k);
//                memo.setContent("testmemocontent" + k);
//                memo.setUrl("testmemourl" + k);
//
//                Category[] category = Category.values();
//                Random random = new Random();
//                Category selectCategory = category[random.nextInt(category.length)];
//                memo.setCategory(selectCategory);
//
//                memoRepository.save(memo);
//            }
//        }
//
//        for (int i = 0; i < 5; i++){
//            for (int k = 0; k <= i; k++)  {
//                ReviewEntity review = new ReviewEntity();
//                review.setUser(userEntityList.get(i));
//                review.setTrip(tripEntityList.get(k));
//                review.setContent("testreviewcontent" + k);
//                // todo: image 추가
//                reviewRepository.save(review);
//            }
//        }
//
//        for (int i = 0; i < 5; i++) {
//            LandmarkEntity landmark = new LandmarkEntity();
//            landmark.setName("testlandmark" + i);
//
//            Address address = new Address("testlandmarkroadnameaddress" + i, "testlandmarklocaladdress" + i);
//            landmark.setAddress(address);
//
//            // todo: image 추가
//            landmarkRepository.save(landmark);
//        }
//
//        List<LandmarkEntity> landmarkEntityList = landmarkRepository.findAll();
//        for (int i = 0; i < 5; i++) {
//            for (int k = 0; k <= i; k++) {
//                likeboxService.addLike(userEntityList.get(i), LikeboxDto.LikePostDto.builder()
//                        .landmarkIdList(new ArrayList<>(List.of(new Long[]{landmarkEntityList.get(k).getId()})))
//                        .build());
//            }
//        }
//    }
//}
