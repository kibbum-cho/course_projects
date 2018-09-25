package com.maple.cse308.service;

import com.maple.cse308.entity.*;

import java.util.List;

public interface TvService {

    TvShow getTvShowDetails(int tvId);

    void addCriticTvReview(TvReviewCritic TvReviewCritic);

    void editCriticTvReview(TvReviewCritic TvReviewCritic);

    void deleteCriticTvReview(int reviewId);

    void addUserTvReview(TvReviewUser TvReviewUser);

    void editUserTvReview(TvReviewUser TvReviewUser);

    void deleteUserTvReview(int reviewId);

    List<TvReviewCritic> getCriticTvReviewsByTvShow(int tvId) throws Exception;

    List<TvReviewCritic> getCriticTvReviewsByCritic(int criticId) throws Exception;

    List<TvReviewUser> getUserTvReviewsByTvShow(int tvId) throws Exception;

    List<TvReviewUser> getUserTvReviewsByUser(int userId) throws Exception;

    List<TvShow> findAllByTitleContainingIgnoreCase(String search);

    List<TvShow> tvSearch(String search);

    List<TvShow> getOpenThisWeek();

    List<TvShow> getPopularTv();

    List<TvScreenshot> getTvScreenshots(int id);

    List<TvActor> getTvActors(int id);

    List<Creator> getTvCreator(int id);

    List<TvReviewUser> getUserTvReviewsByUserAndTv(int userId, int tvId);
}
