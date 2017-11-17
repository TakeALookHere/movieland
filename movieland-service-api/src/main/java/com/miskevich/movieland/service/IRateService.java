package com.miskevich.movieland.service;

import com.miskevich.movieland.dto.RateDto;

import java.util.List;

public interface IRateService {
    List<RateDto> getAll();
}
