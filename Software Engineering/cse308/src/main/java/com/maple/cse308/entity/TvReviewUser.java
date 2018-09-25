package com.maple.cse308.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "TvReviewUser")
public class TvReviewUser implements Serializable {

    @Id
    @Column(name = "reviewId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int reviewId;

    @Column(name = "userId")
    private int userId;

    @Column(name = "tvId")
    private int tvId;

    @Column(name = "rating", nullable = false)
    private Float rating;

    @Column(name = "review", length = 512)
    private String review;

    @Column(name = "link")
    private String link;

    @OneToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "tvId", insertable = false, updatable = false)
    private TvShow tv;

    public TvShow getTv() {
        return tv;
    }

    public void setTv(TvShow tv) {
        this.tv = tv;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        System.out.println(userId + 1);
        this.userId = userId;
    }

    public int getTvId() {
        return tvId;
    }

    public void setTvId(int tvId) {
        this.tvId = tvId;
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
}
