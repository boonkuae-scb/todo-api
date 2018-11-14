package com.bk.todos.services;

import com.bk.todos.model.AuthRequest;
import com.bk.todos.model.AuthResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


@Service
@Slf4j
public class AuthService {

    @Value("${line.token.token-url}")
    private String getAccessTokenUrl;

    @Value("${line.token.channel-id}")
    private String clientId;

    @Value("${line.token.channel-secret}")
    private String clientSecret;

    @Value("${line.token.grant-type}")
    private String grantType;

    @Value("${line.token.redirect-uri}")
    private String redirectUri;

    private RestTemplate restTemplate;

    @Autowired
    public AuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public AuthResponse getAccessToken(AuthRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        log.info("code "+request.getCode());
        log.info("clientId "+ clientId);
        log.info("clientSecret "+ clientSecret);
        log.info("grantType "+ grantType);
        log.info("redirectUri "+redirectUri);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("code", request.getCode());
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("grant_type", grantType);
        map.add("redirect_uri", redirectUri);

        HttpEntity<MultiValueMap<String, String>> requestForm = new HttpEntity<>(map, headers);

        ResponseEntity<AuthResponse> response = null;
        try {
            response = restTemplate.postForEntity(getAccessTokenUrl, requestForm , AuthResponse.class);
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }

        assert response != null;
        return response.getBody();
    }
}