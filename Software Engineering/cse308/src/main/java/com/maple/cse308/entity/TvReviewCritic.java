package com.maple.cse308.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "TvReviewCritic")
public class TvReviewCritic implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "reviewId")
    private Integer reviewId;

    @Column(name = "criticId")
    private Integer criticId;

    @Column(name = "tvId")
    private Integer tvId;

    @Column(name = "rating")
    private Float rating;

    @Column(name = "review", length = 2048)
    private String review;

    @Column(name = "link")
    private String link;

    @OneToOne
    @JoinColumn(name = "criticId", insertable = false, updatable = false)
    private Critic critic;

    public Integer getCriticId() {
        return criticId;
    }

    public void setCriticId(Integer criticId) {
        this.criticId = criticId;
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

    public Integer getReviewId() {
        return reviewId;
    }

    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }

    public Critic getCritic() {
        return critic;
    }

    public void setCritic(Critic critic) {
        this.critic = critic;
    }

    public Integer getTvId() {
        return tvId;
    }

    public void setTvId(Integer tvId) {
        this.tvId = tvId;
    }
}
