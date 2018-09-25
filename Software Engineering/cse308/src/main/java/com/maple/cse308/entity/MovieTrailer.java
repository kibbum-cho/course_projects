package com.maple.cse308.entity;

import javax.persistence.*;

@Entity
@Table(name = "MovieTrailer")
public class MovieTrailer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "movieTrailerId")
    private Integer movieTrailerId;

    @Column(name = "movieId")
    private Integer movieId;

    @Column(name = "video")
    private String video;

    @Column(name = "videoThumb")
    private String videoThumb;

    public Integer getMovieTrailerId() {
        return movieTrailerId;
    }

    public void setMovieTrailerId(Integer movieTrailerId) {
        this.movieTrailerId = movieTrailerId;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideoThumb() {
        return videoThumb;
    }

    public void setVideoThumb(String videoThumb) {
        this.videoThumb = videoThumb;
    }

}
