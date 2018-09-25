package com.maple.cse308.entity;

import javax.persistence.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Set;

@Entity
@Table(name = "TvShow")
public class TvShow {

    @Id
    @Column(name = "tvId")
    private Integer tvId;

    @Column(name = "title")
    private String title;

    @Column(name = "premierDate")
    private Date premierDate;

    @Column(name = "poster")
    private String poster;

    @Column(name = "rating")
    private Float rating;

    @Column(name = "overview", length = 2048)
    private String overview;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "tvGenre", joinColumns = @JoinColumn(name = "tvId"), inverseJoinColumns = @JoinColumn(name = "genreId"))
    private Set<Genre> genre;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "tvCreator", joinColumns = @JoinColumn(name = "tvId"), inverseJoinColumns = @JoinColumn(name = "creatorId"))
    private Set<Creator> creators;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "tvId")
    private Set<TvScreenshot> tvScreenshots;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "tvId")
    private Set<TvReviewCritic> criticReviews;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "tvId")
    private Set<TvReviewUser> userReviews;

    public Float getRating() {
        return rating;
    }

    public Set<TvReviewCritic> getCriticReviews() {
        return criticReviews;
    }

    public Integer getTvId() {
        return tvId;
    }

    public void setTvId(Integer tvId) {
        this.tvId = tvId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getPremierDate() {
        return premierDate;
    }

    public void setPremierDate(Date premierDate) {
        this.premierDate = premierDate;
    }

    public String getDateString() {
        SimpleDateFormat dataFormat = new SimpleDateFormat("MMM dd, yyyy");
        String date = dataFormat.format(premierDate);
        return date;
    }

    public String getDateForList() {
        SimpleDateFormat dataFormat = new SimpleDateFormat("MMM dd");
        String date = dataFormat.format(premierDate);
        return date;
    }

    public String getReleaseYear() {
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy");
        String year = dataFormat.format(premierDate);
        return year;
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

    public Set<Genre> getGenre() {
        return genre;
    }

    public void setGenre(Set<Genre> genre) {
        this.genre = genre;
    }

    public Set<Creator> getCreators() {
        return creators;
    }

    public void setCreators(Set<Creator> creators) {
        this.creators = creators;
    }

    public Set<TvScreenshot> getTvScreenshots() {
        return tvScreenshots;
    }

    public void setTvScreenshots(Set<TvScreenshot> tvScreenshots) {
        this.tvScreenshots = tvScreenshots;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public void setCriticReviews(Set<TvReviewCritic> criticReviews) {
        this.criticReviews = criticReviews;
    }

    public Set<TvReviewUser> getUserReviews() {
        return userReviews;
    }

    public void setUserReviews(Set<TvReviewUser> userReviews) {
        this.userReviews = userReviews;
    }
}