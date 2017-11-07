package com.miskevich.movieland.entity;

public class Review {

    private long id;
    private String text;
    private Movie movie;
    private User user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Review review = (Review) o;

        if (id != review.id) return false;
        if (text != null ? !text.equals(review.text) : review.text != null) return false;
        if (movie != null ? !movie.equals(review.movie) : review.movie != null) return false;
        return user != null ? user.equals(review.user) : review.user == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (movie != null ? movie.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", movie=" + movie +
                ", user=" + user +
                '}';
    }
}
