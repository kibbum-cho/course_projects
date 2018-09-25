package com.maple.cse308.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "Season")
public class Season {

    @EmbeddedId
    private SeasonIdentity season;

    @Column(name = "poster")
    private String poster;

    @Column(name = "overview", length = 2048)
    private String overview;

    @Column(name = "premierDate")
    private Date premierDate;

    @Column(name = "rating")
    private Float rating;

    @Column(name = "ratingCount")
    private Integer ratingCount;

    public SeasonIdentity getSeason() {
        return season;
    }

    public void setSeason(SeasonIdentity season) {
        this.season = season;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Date getPremierDate() {
        return premierDate;
    }

    public void setPremierDate(Date premierDate) {
        this.premierDate = premierDate;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Integer getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }


}
