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

    public AuthResponse callRequestAccessToken(AuthRequest request) {
        HttpEntity<MultiValueMap<String, String>> requestEntity = createFormRequestToken(request);
        ResponseEntity<AuthResponse> response = null;
        try {
            response = restTemplate.postForEntity(
                    getAccessTokenUrl,
                    requestEntity,
                    AuthResponse.class
            );
        } catch (HttpClientErrorException e) {
            System.out.println(e.getMessage());
            return null;
        }
        System.out.println("response.getBody() = " + response);
        return response.getBody();
    }

    private HttpEntity<MultiValueMap<String, String>> createFormRequestToken(AuthRequest authRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> formParamMap = new LinkedMultiValueMap<>();
        formParamMap.add("code", authRequest.getCode());
        formParamMap.add("client_id", clientId);
        formParamMap.add("client_secret", clientSecret);
        formParamMap.add("grant_type", grantType);
        formParamMap.add("redirect_uri", redirectUri);
        return new HttpEntity<>(formParamMap, headers);
    }
}