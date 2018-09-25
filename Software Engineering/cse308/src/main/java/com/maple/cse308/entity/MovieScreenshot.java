package com.maple.cse308.entity;

import javax.persistence.*;

@Entity
@Table(name = "MovieScreenshot")
public class MovieScreenshot {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "movieScreenshotId")
    private Integer movieScreenshotId;

    @Column(name = "movieId")
    private Integer movieId;

    @Column(name = "screenshot")
    private String screenshot;

    public Integer getMovieScreenshotId() {
        return movieScreenshotId;
    }

    public void setMovieScreenshotId(Integer movieScreenshotId) {
        this.movieScreenshotId = movieScreenshotId;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public String getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(String screenshot) {
        this.screenshot = screenshot;
    }

}
