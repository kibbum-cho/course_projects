package com.maple.cse308.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "MovieReviewUser")
public class MovieReviewUser implements Serializable {

    @Id
    @Column(name = "reviewId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int reviewId;

    @Column(name = "userId")
    private int userId;

    @Column(name = "movieId")
    private int movieId;

    @Column(name = "rating", nullable = false)
    private Float rating;

    @Column(name = "review", length = 2048)
    private String review;

    @Column(name = "link")
    private String link;

    @OneToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "movieId", insertable = false, updatable = false)
    private Movie movie;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        System.out.println(userId + 1);
        this.userId = userId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
