package com.maple.cse308.repository;

import com.maple.cse308.entity.ReportMovieCriticReview;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportMovieCriticReviewRepository extends CrudRepository<ReportMovieCriticReview, Integer>{
    ReportMovieCriticReview save(ReportMovieCriticReview reportMovieCriticReview);
    List<ReportMovieCriticReview> findAll();
    ReportMovieCriticReview findByReviewId(Integer reviewId);
    void delete(ReportMovieCriticReview reportMovieCriticReview);
}
