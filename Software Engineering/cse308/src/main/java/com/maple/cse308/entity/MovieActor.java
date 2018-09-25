package com.maple.cse308.entity;

import javax.persistence.*;

@Entity
@Table(name = "MovieActor")
public class MovieActor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "movieActorId")
    private Integer movieActorId;

    @Column(name = "movieId")
    private Integer movieId;

    @Column(name = "actorId")
    private Integer actorId;

    @Column(name = "role")
    private String role;

    @OneToOne
    @JoinColumn(name = "actorId", insertable = false, updatable = false)
    private Actor actor;

    @OneToOne
    @JoinColumn(name = "movieId", insertable = false, updatable = false)
    private Movie movie;

    public Integer getMovieActorId() {
        return movieActorId;
    }

    public void setMovieActorId(Integer movieActorId) {
        this.movieActorId = movieActorId;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public Integer getActorId() {
        return actorId;
    }

    public void setActorId(Integer actorId) {
        this.actorId = actorId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
