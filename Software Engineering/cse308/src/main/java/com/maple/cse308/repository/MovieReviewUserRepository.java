package com.maple.cse308.repository;

import com.maple.cse308.entity.MovieReviewUser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.HashSet;

@Repository
public interface MovieReviewUserRepository extends CrudRepository<MovieReviewUser, Integer> {

    HashSet<MovieReviewUser> findAllByMovieId(int movieId);

    HashSet<MovieReviewUser> findAllByUserId(int userId);

    MovieReviewUser findByReviewId(int reviewId);

    HashSet<MovieReviewUser> findByUserIdAndMovieId(int userId, int movieId);

    @Modifying
    @Transactional
    @Query(value="DELETE FROM movie_review_user WHERE review_id = ?1", nativeQuery = true)
    void deleteByReviewId(Integer reviewId);
}
