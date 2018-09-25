package com.maple.cse308.service;

import com.maple.cse308.entity.*;

import java.text.ParseException;
import java.util.List;

public interface MovieService {

    Movie getMovieDetails(int movieId);

    List<MovieReviewCritic> getCriticMovieReviewsByMovie(int movieId) throws Exception;

    List<MovieReviewCritic> getCriticMovieReviewsByCritic(int criticId) throws Exception;

    List<MovieReviewUser> getUserMovieReviewsByMovie(int movieId) throws Exception;

    List<MovieReviewUser> getUserMovieReviewsByUser(int userId) throws Exception;

    List<MovieReviewUser> getUserMovieReviewsByUserAndMovie(int userId, int movieId);

    List<Movie> getMoviesComingSoon();

    List<Movie> getMoviesOutNow();

    List<Movie> getTopBoxOffice();

    void addCriticMovieReview(MovieReviewCritic movieReviewCritic);

    void editCriticMovieReview(MovieReviewCritic movieReviewCritic);

    void deleteCriticMovieReview(MovieReviewCritic movieReviewCritic);

    void addUserMovieReview(MovieReviewUser movieReviewUser);

    void editUserMovieReview(MovieReviewUser movieReviewUser);

    void deleteUserMovieReview(int reviewId);

    List<Movie> movieSearch(String search);

    List<Movie> findAllByTitleContainingIgnoreCase(String search);

    List<Movie> getAllTimeHighestRated();

    List<MovieScreenshot> getMovieScreenShots(int movieId);

    List<MovieTrailer> getMovieTrailers(int movieId);

    List<MovieActor> getMovieActors(int movieId);

    float getAverageUserRating(int movieId);

    List<Movie> movieAdvancedSearch(String search, String[] genre, String start, String end) throws ParseException;

    List<Movie> getCertifiedFresh();

    List<Genre> getGenres();

    List<AcademyAward> getAwardByYear(Integer year);

    List<Groups> getAllGroups();

    List<Critic> getCriticsByGroup(Integer groupId);
}

