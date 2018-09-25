package com.maple.cse308.repository;

import com.maple.cse308.entity.TvReviewUser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.HashSet;

@Repository
public interface TvReviewUserRepository extends CrudRepository<TvReviewUser, Integer> {

    HashSet<TvReviewUser> findAllByTvId(int tvId);

    HashSet<TvReviewUser> findAllByUserId(int userId);

    TvReviewUser findByReviewId(int reviewId);

    HashSet<TvReviewUser> findByUserIdAndTvId(int userId, int tvId);

    @Modifying
    @Transactional
    @Query(value="DELETE FROM tv_review_user WHERE review_id = ?1", nativeQuery = true)
    void deleteByReviewId(Integer reviewId);
}
