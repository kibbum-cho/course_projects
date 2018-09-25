package com.maple.cse308.repository;

import com.maple.cse308.entity.ReportTvUserReview;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportTvUserReviewRepository extends CrudRepository<ReportTvUserReview, Integer>{
    ReportTvUserReview save(ReportTvUserReview reportMovieUserReview);
    List<ReportTvUserReview> findAll();
    ReportTvUserReview findByReviewId(Integer reviewId);
    void delete(ReportTvUserReview reportMovieUserReview);
}
