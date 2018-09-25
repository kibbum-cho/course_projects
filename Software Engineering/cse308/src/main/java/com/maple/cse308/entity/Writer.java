package com.maple.cse308.entity;

import javax.persistence.*;

@Entity
@Table(name = "Writer")
public class Writer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "writerId")
    private Integer writerId;

    @Column(name = "movieId")
    private int movieId;

    @Column(name = "writer")
    private String writer;

    public Integer getWriterId() {
        return writerId;
    }

    public void setWriterId(Integer writerId) {
        this.writerId = writerId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

}
