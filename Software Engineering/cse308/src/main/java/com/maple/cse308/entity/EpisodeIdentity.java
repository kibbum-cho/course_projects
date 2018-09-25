package com.maple.cse308.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EpisodeIdentity implements Serializable {

    @NotNull
    private SeasonIdentity seasonId;

    @NotNull
    @Column(name = "episode")
    private Integer episodeNum;

    public EpisodeIdentity(){}

    public EpisodeIdentity(SeasonIdentity seasonId, Integer episodeNum) {
        this.seasonId = seasonId;
        this.episodeNum = episodeNum;
    }

    public SeasonIdentity getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(SeasonIdentity seasonId) {
        this.seasonId = seasonId;
    }

    public Integer getEpisodeNum() {
        return episodeNum;
    }

    public void setEpisodeNum(Integer episodeNum) {
        this.episodeNum = episodeNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EpisodeIdentity)) return false;
        EpisodeIdentity that = (EpisodeIdentity) o;
        return Objects.equals(getSeasonId(), that.getSeasonId()) &&
                Objects.equals(getEpisodeNum(), that.getEpisodeNum());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getSeasonId(), getEpisodeNum());
    }
}
