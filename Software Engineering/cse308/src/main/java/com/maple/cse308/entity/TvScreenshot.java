package com.maple.cse308.entity;

import javax.persistence.*;

@Entity
@Table(name = "TvScreenshot")
public class TvScreenshot {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tvScreenshotId")
    private Integer tvScreenshotId;

    @Column(name = "tvId")
    private Integer tvId;

    @Column(name = "screenshot")
    private String screenshot;

    public Integer getTvScreenshotId() {
        return tvScreenshotId;
    }

    public void setTvScreenshotId(Integer tvScreenshotId) {
        this.tvScreenshotId = tvScreenshotId;
    }

    public Integer getTvId() {
        return tvId;
    }

    public void setTvId(Integer tvId) {
        this.tvId = tvId;
    }

    public String getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(String screenshot) {
        this.screenshot = screenshot;
    }
}
