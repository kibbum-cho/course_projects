package com.maple.cse308.repository;

import com.maple.cse308.entity.MovieScreenshot;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieScreenshotRepository extends CrudRepository<MovieScreenshot, Integer> {

    List<MovieScreenshot> findAllByMovieId(Integer movieId);
}
