package com.maple.cse308.repository;

import com.maple.cse308.entity.Movie;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Integer> {

    Movie findMovieByMovieId(int movieId);

    List<Movie> findTop12ByReleaseDateAfter(Date date);

    List<Movie> findTop10ByReleaseDateBefore(Date date);

    List<Movie> findTop10ByRatingAvg(List<Movie> movieList);

    Movie save(Movie m);

    boolean existsByTitleAndReleaseDate(String title, Date releaseDate);

    Movie findByTitleAndReleaseDate(String title, Date releaseDate);

    List<Movie> findAllByTitleContainingIgnoreCase(String search);

    List<Movie> findAll();

    List<Movie> findAllByRatingAvgGreaterThanEqualAndRatingCountGreaterThanEqual(float ratingAvg, int ratingCount);

    @Query(value="SELECT * FROM movie WHERE rating_avg<=10  ORDER BY rating_avg DESC LIMIT 20", nativeQuery=true)
    List<Movie> findTop20OrderByRatingAvgLessThanEqual();

    List<Movie> findAllByTitleContainingIgnoreCaseAndReleaseDateBetween(String search, Date startDate, Date endDate);

    @Query(value="SELECT * FROM movie WHERE release_date <= ?1 ORDER BY release_date DESC LIMIT 12", nativeQuery=true)
    List<Movie> findTop12OrderByReleaseDateLessThanDesc(Date date);

    @Query(value="SELECT * FROM movie WHERE release_date <= ?1 ORDER BY release_date DESC LIMIT 40", nativeQuery=true)
    List<Movie> findTop40OrderByReleaseDateLessThanDesc(Date date);
}
