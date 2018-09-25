package com.maple.cse308.entity;

import javax.persistence.*;

@Entity
@Table(name="academy_award")
public class AcademyAward {

    @Id
    @Column(name="award_id")
    private Integer awardId;

    @Column(name="award_title")
    private String awardTitle;

    @Column(name="year")
    private Integer year;

    @OneToOne
    @JoinColumn(name="movie_id")
    private Movie movie;

    public String getAwardTitle() {
        return awardTitle;
    }

    public void setAwardTitle(String awardTitle) {
        this.awardTitle = awardTitle;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Integer getAwardId() {
        return awardId;
    }

    public void setAwardId(Integer awardId) {
        this.awardId = awardId;
    }
}
