package com.miskevich.movieland.service.impl;

import com.miskevich.movieland.service.IRateService;
import com.miskevich.movieland.service.dto.RateDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RateService implements IRateService {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    @Autowired
    private IRateService rateService;

    @Override
    @SuppressWarnings("unchecked")
    public List<RateDto> getAll() {
        LOG.info("Start get all rates");
        long startTime = System.currentTimeMillis();
        List<RateDto> rates = rateService.getAll();
        LOG.info("Finish get all rates. It took {} ms", System.currentTimeMillis() - startTime);
        return rates;
    }
}
