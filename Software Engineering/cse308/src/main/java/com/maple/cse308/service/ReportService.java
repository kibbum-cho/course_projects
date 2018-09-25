package com.maple.cse308.service;

import com.maple.cse308.entity.*;

import java.util.List;

public interface ReportService {

    void addCriticReport(Integer criticId, String reason);

    void addUserReport(Integer userId, String reason);

    void addCriticMovieReport(Integer reviewId, String reason);

    void addUserMovieReport(Integer reviewId, String reason);

    void addCriticTvReport(Integer reviewId, String reason);

    void addUserTvReport(Integer reviewId, String reason);

    void deleteCriticReport(Integer criticId);

    void deleteUserReport(Integer userId);

    void deleteCriticMovieReport(Integer reviewId);

    void deleteUserMovieReport(Integer reviewId);

    void deleteCriticTvReport(Integer reviewId);

    void deleteUserTvReport(Integer reviewId);

    List<ReportCritic> getCriticReports();

    List<ReportUser> getUserReports();

    List<ReportMovieCriticReview> getCriticMovieReports();

    List<ReportMovieUserReview> getUserMovieReports();

    List<ReportTvCriticReview> getCriticTvReports();

    List<ReportTvUserReview> getUserTvReports();


}
