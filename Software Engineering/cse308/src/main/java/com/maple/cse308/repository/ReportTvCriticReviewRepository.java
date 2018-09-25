package com.maple.cse308.repository;

import com.maple.cse308.entity.ReportTvCriticReview;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportTvCriticReviewRepository extends CrudRepository<ReportTvCriticReview, Integer>{
    ReportTvCriticReview save(ReportTvCriticReview reportTvCriticReview);
    List<ReportTvCriticReview> findAll();
    ReportTvCriticReview findByReviewId(Integer reviewId);
    void delete(ReportTvCriticReview reportTvCriticReview);
}
