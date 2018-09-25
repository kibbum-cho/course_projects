package com.maple.cse308.repository;

import com.maple.cse308.entity.MovieReviewCritic;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.HashSet;

@Repository
public interface MovieReviewCriticRepository extends CrudRepository<MovieReviewCritic, Integer> {

    HashSet<MovieReviewCritic> findAllByMovieId(int movieId);

    HashSet<MovieReviewCritic> findAllByCriticId(int criticId);

    @Modifying
    @Transactional
    @Query(value="DELETE FROM movie_review_critic WHERE review_id = ?1", nativeQuery = true)
    void deleteByReviewId(Integer reviewId);
}
