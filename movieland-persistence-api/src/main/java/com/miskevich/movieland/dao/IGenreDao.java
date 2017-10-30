package com.miskevich.movieland.dao;

import com.miskevich.movieland.entity.Genre;

import java.util.List;

public interface IGenreDao {
    List<Genre> getAll();
}