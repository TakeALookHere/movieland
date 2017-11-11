package com.miskevich.movieland.web.dto;

public class ErrorDto {
    private String title;
    private String detail;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "ErrorDto{" +
                "title='" + title + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }
}
