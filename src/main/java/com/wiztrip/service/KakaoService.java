package com.wiztrip.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wiztrip.Info.KakaoResponse;
import com.wiztrip.Info.KakaoTokenIfo;
import com.wiztrip.domain.UserEntity;
import com.wiztrip.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.type.TypeReference;


import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KakaoService {

    private final UserRepository userRepository;

    @Transactional
    public KakaoResponse getKakaoUserData(String code) throws JsonProcessingException {
        KakaoTokenIfo tokenInfo = getKakaoToken(code);
        Map<String, Object> userInfo = getKakaoUserProfile(tokenInfo.getAccess_token());

        KakaoResponse kakaoResponse = new KakaoResponse();
        kakaoResponse.setAccess_Token(tokenInfo.getAccess_token());
        kakaoResponse.setRefresh_Token(tokenInfo.getRefresh_token());
        kakaoResponse.setUserInfo(userInfo);

        saveOrUpdateKakaoUser(userInfo);

        return kakaoResponse;
    }

    private KakaoTokenIfo getKakaoToken(String code) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        RestTemplate rt = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "c2b3cda0315bd5e42a85ba7cacb0fa43"); // Replace with your client id
        params.add("redirect_uri", "http://localhost:8080/oauth/kakao/callback");
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        return objectMapper.readValue(response.getBody(), KakaoTokenIfo.class);
    }

    private Map<String, Object> getKakaoUserProfile(String accessToken) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        RestTemplate rt = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        headers.setBearerAuth(accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);

        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        return objectMapper.readValue(response.getBody(), new TypeReference<>() {
        });
    }

    private UserEntity mapKakaoDataToUserEntity(Map<String, Object> kakaoData) {
        UserEntity user = new UserEntity();

        // Assuming email is used as username and no password is set
        String email = (String) ((Map<String, Object>) kakaoData.get("kakao_account")).get("email");
        user.setUsername(email);
        user.setEmail(email);

        // Handling the nickname and profile image
        String nickname = (String) ((Map<String, Object>) kakaoData.get("properties")).get("nickname");
        user.setNickname(nickname);

//        String profileImageUrl = (String) ((Map<String, Object>) ((Map<String, Object>) kakaoData.get("kakao_account")).get("profile")).get("profile_image_url");
//        Image image = new Image(); // Assuming Image is an @Embeddable entity
//        image.setUrl(profileImageUrl);
//        user.setImage(image);

        return user;
    }


    public void saveOrUpdateKakaoUser(Map<String, Object> kakaoUserData) {
        UserEntity user = mapKakaoDataToUserEntity(kakaoUserData);

        // Check if user already exists in the database
        Optional<UserEntity> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            // Update existing user
            UserEntity updateUser = existingUser.get();
            updateUser.setNickname(user.getNickname());
            updateUser.setImage(user.getImage());
            userRepository.save(updateUser);
        } else {
            // Save new user
            userRepository.save(user);
        }
    }


}
