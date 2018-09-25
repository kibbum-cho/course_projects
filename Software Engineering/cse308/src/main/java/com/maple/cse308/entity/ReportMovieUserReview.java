package com.maple.cse308.entity;

import javax.persistence.*;


@Entity
@Table(name="report_movie_user_review")
public class ReportMovieUserReview {

    @Id
    @Column(name="reviewId")
    private Integer reviewId;

    @Column(name="reason")
    private String reason;

    @OneToOne
    @JoinColumn(name = "reviewId", insertable = false, updatable = false)
    private MovieReviewUser movieReviewUser;

    public MovieReviewUser getMovieReviewUser() {
        return movieReviewUser;
    }

    public void setMovieReviewUser(MovieReviewUser movieReviewUser) {
        this.movieReviewUser = movieReviewUser;
    }

    public Integer getReviewId() {
        return reviewId;
    }

    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
