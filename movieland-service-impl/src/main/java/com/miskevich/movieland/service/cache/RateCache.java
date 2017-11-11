package com.miskevich.movieland.service.cache;

import com.miskevich.movieland.service.IRateService;
import com.miskevich.movieland.service.dto.RateDto;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class RateCache implements IRateService {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private RestTemplate restTemplate = new RestTemplate();
    private volatile List<RateDto> rates;

    @Value("${nbu.url}")
    private String nbuUrl;
//    @Autowired
//    private String nbuUrl;

    @Override
    @SuppressWarnings("unchecked")
    public List<RateDto> getAll() {
        List<RateDto> copy = new ArrayList<>();
        copy.addAll(rates);
        return copy;
    }

    @PostConstruct
    private void initCache() {
        //Required do avoid error: Could not generate DH keypair on SSL
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
        LOG.info("Start get all rates from NBU API");
        long startTime = System.currentTimeMillis();

        ResponseEntity<List<RateDto>> rateResponse =
                restTemplate.exchange(nbuUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<RateDto>>() {
                });

        rates = rateResponse.getBody();
        LOG.info("Finish get all rates from NBU API. It took {} ms", System.currentTimeMillis() - startTime);
        LOG.debug("Current rates: " + rates);
        LOG.info("Rate cache was initialized");
    }

    @Scheduled(cron = "${nbu.cache.crone.refresh.timeout.test}")
    private void initCacheRefreshTask() {
        initCache();
    }

}
