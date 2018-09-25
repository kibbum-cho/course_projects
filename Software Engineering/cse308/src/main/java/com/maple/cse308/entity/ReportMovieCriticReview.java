package com.maple.cse308.entity;

import javax.persistence.*;

@Entity
@Table(name="report_movie_critic_review")
public class ReportMovieCriticReview {

    @Id
    @Column(name="reviewId")
    private Integer reviewId;

    @Column(name="reason")
    private String reason;


    @OneToOne
    @JoinColumn(name = "reviewId", insertable = false, updatable = false)
    private MovieReviewCritic movieReviewCritic;

    public MovieReviewCritic getMovieReviewCritic() {
        return movieReviewCritic;
    }

    public void setMovieReviewCritic(MovieReviewCritic movieReviewCritic) {
        this.movieReviewCritic = movieReviewCritic;
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
