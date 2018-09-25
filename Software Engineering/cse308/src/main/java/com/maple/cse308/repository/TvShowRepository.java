package com.maple.cse308.repository;

import com.maple.cse308.entity.TvShow;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface TvShowRepository extends CrudRepository<TvShow, Integer> {

    TvShow findTvShowByTvId(int tvId);

    List<TvShow> findAllByTitleContainingIgnoreCase(String search);

    TvShow save(TvShow tvShow);

    boolean existsByTitleAndPremierDate(String title, Date premierDate);

    List<TvShow> findTop12ByOrderByPremierDateDesc();

    List<TvShow> findTop12ByOrderByRatingDesc();
}
