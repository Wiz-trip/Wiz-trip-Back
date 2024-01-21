package com.wiztrip.tourapi;

import com.wiztrip.repository.LandmarkRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("tourapi")
public class TourApiController {

    private final TourApiService tourApiService;
    private final LandmarkRepository landmarkRepository;
    private final TourApiTool tourApiTool;


    @Operation(summary = "tour api 관광지 저장", description =
            """
            tour api로부터 데이터를 받아와 DB에 저장합니다.
            """
    )
    @GetMapping("save-to-repository")
    public ResponseEntity<String> getAllLandmarks(
        @RequestParam Integer num
    ) {
        try {
            tourApiService.getLandmarksFromApi(num);
            return ResponseEntity.ok("done");
        } catch (Exception e) {
            // 오류 처리, 예를 들어 API 호출 실패
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}