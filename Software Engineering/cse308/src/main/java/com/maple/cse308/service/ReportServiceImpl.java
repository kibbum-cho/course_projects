package com.maple.cse308.service;

import com.maple.cse308.entity.*;
import com.maple.cse308.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService{

    @Autowired
    ReportCriticRepository reportCriticRepository;

    @Autowired
    ReportUserRepository reportUserRepository;

    @Autowired
    ReportMovieCriticReviewRepository reportMovieCriticReviewRepository;

    @Autowired
    ReportMovieUserReviewRepository reportMovieUserReviewRepository;

    @Autowired
    ReportTvCriticReviewRepository reportTvCriticReviewRepository;

    @Autowired
    ReportTvUserReviewRepository reportTvUserReviewRepository;


    @Override
    public void addCriticReport(Integer criticId, String reason) {
        ReportCritic reportCritic = new ReportCritic();
        reportCritic.setCriticId(criticId);
        reportCritic.setReason(reason);
        reportCriticRepository.save(reportCritic);
    }

    @Override
    public void addUserReport(Integer userId, String reason) {
        ReportUser reportUser= new ReportUser();
        reportUser.setUserId(userId);
        reportUser.setReason(reason);
        reportUserRepository.save(reportUser);
    }

    @Override
    public void addCriticMovieReport(Integer reviewId, String reason) {
        ReportMovieCriticReview reportMovieCriticReview = new ReportMovieCriticReview();
        reportMovieCriticReview.setReviewId(reviewId);
        reportMovieCriticReview.setReason(reason);
        reportMovieCriticReviewRepository.save(reportMovieCriticReview);
    }

    @Override
    public void addUserMovieReport(Integer reviewId, String reason) {
        ReportMovieUserReview reportMovieUserReview = new ReportMovieUserReview();
        reportMovieUserReview.setReviewId(reviewId);
        reportMovieUserReview.setReason(reason);
        reportMovieUserReviewRepository.save(reportMovieUserReview);
    }

    @Override
    public void addCriticTvReport(Integer reviewId, String reason) {
        ReportTvCriticReview reportTvCriticReview = new ReportTvCriticReview();
        reportTvCriticReview.setReviewId(reviewId);
        reportTvCriticReview.setReason(reason);
        reportTvCriticReviewRepository.save(reportTvCriticReview);
    }

    @Override
    public void addUserTvReport(Integer reviewId, String reason) {
        ReportTvUserReview reportTvUserReview = new ReportTvUserReview();
        reportTvUserReview.setReviewId(reviewId);
        reportTvUserReview.setReason(reason);
        reportTvUserReviewRepository.save(reportTvUserReview);
    }

    @Override
    public void deleteCriticReport(Integer criticId) {
        ReportCritic reportCritic = reportCriticRepository.findByCriticId(criticId);
        reportCriticRepository.delete(reportCritic);
    }

    @Override
    public void deleteUserReport(Integer userId) {
        ReportUser reportUser = reportUserRepository.findByUserId(userId);
        reportUserRepository.delete(reportUser);
    }

    @Override
    public void deleteCriticMovieReport(Integer reviewId) {
        ReportMovieCriticReview reportMovieCriticReview = reportMovieCriticReviewRepository.findByReviewId(reviewId);
        reportMovieCriticReviewRepository.delete(reportMovieCriticReview);
    }

    @Override
    public void deleteUserMovieReport(Integer reviewId) {
        ReportMovieUserReview reportMovieUserReview = reportMovieUserReviewRepository.findByReviewId(reviewId);
        reportMovieUserReviewRepository.delete(reportMovieUserReview);
    }

    @Override
    public void deleteCriticTvReport(Integer reviewId) {
        ReportTvCriticReview reportTvCriticReview = reportTvCriticReviewRepository.findByReviewId(reviewId);
        reportTvCriticReviewRepository.delete(reportTvCriticReview);
    }

    @Override
    public void deleteUserTvReport(Integer reviewId) {
        ReportTvUserReview reportTvUserReview = reportTvUserReviewRepository.findByReviewId(reviewId);
        reportTvUserReviewRepository.delete(reportTvUserReview);
    }

    @Override
    public List<ReportCritic> getCriticReports() {
        return reportCriticRepository.findAll();
    }

    @Override
    public List<ReportUser> getUserReports() {
        return reportUserRepository.findAll();
    }

    @Override
    public List<ReportMovieCriticReview> getCriticMovieReports() {
        return reportMovieCriticReviewRepository.findAll();
    }

    @Override
    public List<ReportMovieUserReview> getUserMovieReports() {
        return reportMovieUserReviewRepository.findAll();
    }

    @Override
    public List<ReportTvCriticReview> getCriticTvReports() {
        return reportTvCriticReviewRepository.findAll();
    }

    @Override
    public List<ReportTvUserReview> getUserTvReports() {
        return reportTvUserReviewRepository.findAll();
    }
}
