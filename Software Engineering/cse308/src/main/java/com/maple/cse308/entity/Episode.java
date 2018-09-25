package com.maple.cse308.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Episode")
public class Episode {

    @EmbeddedId
    private EpisodeIdentity episodeId;

    @Column(name = "title")
    private String title;

    @Column(name = "overview", length = 2048)
    private String overview;

    public EpisodeIdentity getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(EpisodeIdentity episodeId) {
        this.episodeId = episodeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}
