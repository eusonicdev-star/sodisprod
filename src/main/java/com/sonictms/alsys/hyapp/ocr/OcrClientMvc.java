package com.sonictms.alsys.hyapp.ocr;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class OcrClientMvc {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl = "http://localhost:8000/ocr";

    public String ocr(byte[] imageBytes) {
        ByteArrayResource resource = new ByteArrayResource(imageBytes) {
            @Override public String getFilename() { return "image.jpg"; }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", resource);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

        return restTemplate.postForObject(baseUrl, request, String.class);
    }
}
