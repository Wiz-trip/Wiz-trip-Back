package com.wiztrip.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wiztrip.domain.LandmarkEntity;
import com.wiztrip.dto.LandmarkDto;
import com.wiztrip.service.LandmarkService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("landmarks")
public class LandmarkController {

    private final LandmarkService landmarkService;


    @Operation(summary = "모든 관광지 조회", description =
            """
            관광타입 (12-> 관광지) 로 필터링 한 모든 관광지를 보여줍니다. 
            한 페이지의 결과수는 100개로 하였습니다.         
            """
    )
    @GetMapping
    public ResponseEntity<List<LandmarkDto.LandmarkApiResponseDto>> getAllLandmarks() {
        try {
            List<LandmarkDto.LandmarkApiResponseDto> landmarks = landmarkService.getLandmarksFromApi(100);
            return ResponseEntity.ok(landmarks);
        } catch (Exception e) {
            // 오류 처리, 예를 들어 API 호출 실패
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @Operation(summary = "상세 관광지 조회",
            description = """
                   < 대분류(cat1) -> 중분류(cat2) -> 소분류(cat3) >
                    
                    * 대분류 A01: 자연관광지
                        * 중분류 A0101: 자연관광지
                            * A01010100: 국립공원
                            * A01010200: 도립공원
                            * A01010300: 군립공원
                            * A01010400: 산
                            * A01010500: 자연생태관광지
                            * A01010600: 자연휴양림
                            * A01010700: 수목원
                            * A01010800: 폭포
                            * A01010900: 계곡
                            * A01011000: 약수터
                            * A01011100: 해안절경
                            * A01011200: 해수욕장
                            * A01011300: 섬
                            * A01011400: 항구/포구
                            * A01011600: 등대
                            * A01011700: 호수
                            * A01011800: 강
                            * A01011900: 동굴
                        * 중분류 A0102: 관광자원
                            * A01020100: 희귀동.식물
                            * A01020200: 기암괴석
                    * 대분류 A02: 역사관광지
                        * 중분류 A0201: 역사관광지
                            * A02010100: 고궁
                            * A02010200: 성
                            * A02010300: 문
                            * A02010400: 고택
                            * A02010500: 생가
                            * A02010600: 민속마을
                            * A02010700: 유적지/사적지
                            * A02010800: 사찰
                            * A02010900: 종교성지
                            * A02011000: 안보관광
                        * 중분류 A0202: 휴양관광지
                            * A02020200: 관광단지
                            * A02020300: 온천/욕장/스파
                            * A02020400: 이색찜질방
                            * A02020500: 헬스투어
                            * A02020600: 테마공원
                            * A02020700: 공원
                            * A02020800: 유람선/잠수함관광
                        * 중분류 A0203: 체험관광지
                            * A02030100: 농.산.어촌 체험
                            * A02030200: 전통체험
                            * A02030300: 산사체험
                            * A02030400: 이색체험
                            * A02030600: 이색거리
                        * 중분류 A0204: 산업관광지
                            * A02040400: 발전소
                            * A02040600: 식음료
                            * A02040800: 기타
                            * A02040900: 전자-반도체
                            * A02041000: 자동차
                        * 중분류 A0205: 건축/조형물
                            * A02050100: 다리/대교
                            * A02050200: 기념탑/기념비/전망대
                            * A02050300: 분수
                            * A02050400: 동상
                            * A02050500: 터널
                            * A02050600: 유명건물
                    """
                    )
    @GetMapping("/landmarks")
    public ResponseEntity<List<LandmarkDto.LandmarkApiDetailResponseDto>> getLandmarksByContentTypeId(
            @RequestParam String cat1,
            @RequestParam String cat2,
            @RequestParam String cat3)
            throws URISyntaxException, JsonProcessingException {
        List<LandmarkDto.LandmarkApiDetailResponseDto> landmarks = landmarkService.getLandmarksByContentTypeId(cat1, cat2, cat3);
        return ResponseEntity.ok(landmarks);
    }


    // 페이징
    @Operation(summary = "여행지 페이징",
            description = """
            <파라미터 설명>
            * pageNo : 페이지 번호 
            * numOfRows : 보여질 데이터의 개수
            
            <ResponseBody>
            content: 페이지 요청에 의해 반환된 랜드마크(또는 기타 엔터티)의 실제 목록. 이 목록의 각 항목은 데이터베이스의 하나의 레코드를 나타냄

            pageable: 정렬 순서, 페이지 번호 및 페이지 크기를 포함하여 페이지 매김에 대한 세부 정보를 포함

            totalPages: 사용 가능한 총 페이지 수

            totalElements: 전체 데이터세트의 총 요소 또는 레코드 수

            size, number: 현재 페이지 크기와 페이지 번호를 반영

            first, last: 현재 페이지가 첫 번째인지 마지막인지 나타내는 값

            sort: 데이터에 적용된 정렬에 대한 정보

            empty: 현재 페이지가 비어 있는지 여부를 나타냄
            """)
    @GetMapping("/paging")
    public ResponseEntity<Page<LandmarkDto.LandmarkApiResponseDto>> getLandmarks(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int numOfRows,
            @RequestParam(defaultValue = "id") String sort) throws URISyntaxException, JsonProcessingException {

        Pageable pageable = PageRequest.of(pageNo, numOfRows, Sort.by(sort));
        Page<LandmarkDto.LandmarkApiResponseDto> landmarks = landmarkService.getLandmarksPagingApi(pageable);
        return ResponseEntity.ok(landmarks);
    }

}