package com.maple.cse308.entity;

import javax.persistence.*;

@Entity
@Table(name = "ActorScreenshot")
public class ActorScreenshot {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "actorScreenshotId")
    private Integer actorScreenShotId;

    @Column(name = "actorId")
    private Integer actorId;

    @Column(name = "screenshot")
    private String screenshot;

    @Column(name = "thumbNail")
    private String thumbNail;

    public Integer getActorScreenShotId() {
        return actorScreenShotId;
    }

    public void setActorScreenShotId(Integer actorScreenShotId) {
        this.actorScreenShotId = actorScreenShotId;
    }

    public Integer getActorId() {
        return actorId;
    }

    public void setActorId(Integer actorId) {
        this.actorId = actorId;
    }

    public String getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(String screenshot) {
        this.screenshot = screenshot;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    public void setThubNail(String thumbNail) {
        this.thumbNail = thumbNail;
    }

}
