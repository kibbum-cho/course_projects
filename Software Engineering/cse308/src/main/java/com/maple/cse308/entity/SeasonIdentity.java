package com.maple.cse308.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SeasonIdentity implements Serializable {

    @Column(name = "season")
    private Integer seasonNum;

    @Column(name = "tvId")
    private Integer tvId;

    public SeasonIdentity() {

    }

    public SeasonIdentity(Integer seasonNum, Integer tvId) {
        this.seasonNum = seasonNum;
        this.tvId = tvId;
    }

    public Integer getSeasonNum() {
        return seasonNum;
    }

    public void setSeasonNum(Integer seasonNum) {
        this.seasonNum = seasonNum;
    }

    public Integer getTvId() {
        return tvId;
    }

    public void setTvId(Integer tvId) {
        this.tvId = tvId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SeasonIdentity)) return false;
        SeasonIdentity that = (SeasonIdentity) o;
        return Objects.equals(getSeasonNum(), that.getSeasonNum()) &&
                Objects.equals(getTvId(), that.getTvId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSeasonNum(), getTvId());
    }
}
