package com.maple.cse308.entity;

import javax.persistence.*;

@Entity
@Table(name="report_tv_user_review")
public class ReportTvUserReview {

    @Id
    @Column(name="reviewId")
    private Integer reviewId;

    @Column(name="reason")
    private String reason;

    @OneToOne
    @JoinColumn(name = "reviewId", insertable = false, updatable = false)
    private TvReviewUser tvReviewUser;

    public TvReviewUser getTvReviewUser() {
        return tvReviewUser;
    }

    public void setTvReviewUser(TvReviewUser tvReviewUser) {
        this.tvReviewUser = tvReviewUser;
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
