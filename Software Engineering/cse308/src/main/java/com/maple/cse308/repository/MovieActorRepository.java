package com.maple.cse308.repository;

import com.maple.cse308.entity.MovieActor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieActorRepository extends CrudRepository<MovieActor, Integer> {

    List<MovieActor> findAllByMovieId(Integer movieId);

    List<MovieActor> findAllByActorId(Integer actorId);
}
