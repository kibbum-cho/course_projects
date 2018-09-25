package com.maple.cse308.repository;

import com.maple.cse308.entity.ReportMovieUserReview;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportMovieUserReviewRepository extends CrudRepository<ReportMovieUserReview, Integer>{
    ReportMovieUserReview save(ReportMovieUserReview reportMovieUserReview);
    List<ReportMovieUserReview> findAll();
    ReportMovieUserReview findByReviewId(Integer reviewId);
    void delete(ReportMovieUserReview reportMovieUserReview);
}
