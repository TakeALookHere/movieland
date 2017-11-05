package com.miskevich.movieland.web.util;

import com.miskevich.movieland.web.dto.RateDto;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.Security;
import java.util.List;

@Service
public class RateReader {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private RestTemplate restTemplate = new RestTemplate();

    @Value("${nbu.url}")
    private String nbuUrl;

    public List<RateDto> getCurrentRates() {
        //Required do avoid error: Could not generate DH keypair on SSL
        Security.insertProviderAt(new BouncyCastleProvider(), 1);

        ResponseEntity<List<RateDto>> rateResponse =
                restTemplate.exchange(nbuUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<RateDto>>() {
                });
        LOG.info("Current NBU rates were received");
        List<RateDto> rateDtos = rateResponse.getBody();
        LOG.debug("Current rates: " + rateDtos);
        return rateDtos;
    }
}
