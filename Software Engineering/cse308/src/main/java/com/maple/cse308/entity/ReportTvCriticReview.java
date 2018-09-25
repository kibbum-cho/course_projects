package com.maple.cse308.entity;

import javax.persistence.*;

@Entity
@Table(name="report_tv_critic_review")
public class ReportTvCriticReview {

    @Id
    @Column(name="reviewId")
    private Integer reviewId;

    @Column(name="reason")
    private String reason;

    @OneToOne
    @JoinColumn(name = "reviewId", insertable = false, updatable = false)
    private TvReviewCritic tvReviewCritic;

    public TvReviewCritic getTvReviewCritic() {
        return tvReviewCritic;
    }

    public void setTvReviewCritic(TvReviewCritic tvReviewCritic) {
        this.tvReviewCritic = tvReviewCritic;
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
