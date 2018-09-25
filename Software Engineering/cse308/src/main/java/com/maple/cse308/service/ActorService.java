package com.maple.cse308.service;

import com.maple.cse308.entity.Actor;
import com.maple.cse308.entity.ActorScreenshot;
import com.maple.cse308.entity.MovieActor;
import com.maple.cse308.entity.TvActor;

import java.util.List;

public interface ActorService {

    Actor getActorDetails(int actor_id);

    List<Actor> actorSearch(String search);

    Actor getActor(int actorId);

    List<ActorScreenshot> getActorScreenshots(int actorId);

    List<MovieActor> getActorMovies(int actorId);

    List<TvActor> getActorTVs(int actorId);
}
