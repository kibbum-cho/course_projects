package com.maple.cse308.service;


import com.maple.cse308.entity.*;
import com.maple.cse308.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    MovieReviewCriticRepository movieReviewCriticRepository;
    @Autowired
    MovieReviewUserRepository movieReviewUserRepository;
    @Autowired
    MovieRepository movieRepository;

    @Autowired
    MovieScreenshotRepository movieScreenshotRepository;
    @Autowired
    MovieTrailerRepository movieTrailerRepository;
    @Autowired
    MovieActorRepository movieActorRepository;
    @Autowired
    GenreRepository genreRepository;
    @Autowired
    AcademyAwardRepository academyAwardRepository;
    @Autowired
    GroupsRepository groupsRepository;

    @Override
    public Movie getMovieDetails(int movieId) {
        Movie movie = movieRepository.findMovieByMovieId(movieId);
        return movie;
    }

    @Override
    public List<MovieReviewCritic> getCriticMovieReviewsByMovie(int movieId) {
        HashSet<MovieReviewCritic> set = movieReviewCriticRepository.findAllByMovieId(movieId);
        List<MovieReviewCritic> movieReviewCritics = new LinkedList();
        movieReviewCritics.addAll(set);
        return movieReviewCritics;
    }

    @Override
    public List<MovieReviewCritic> getCriticMovieReviewsByCritic(int criticId) {
        HashSet<MovieReviewCritic> set = movieReviewCriticRepository.findAllByCriticId(criticId);
        List<MovieReviewCritic> movieReviewCritics = new LinkedList();
        movieReviewCritics.addAll(set);
        return movieReviewCritics;
    }

    @Override
    public List<MovieReviewUser> getUserMovieReviewsByMovie(int movieId) {
        HashSet<MovieReviewUser> set = movieReviewUserRepository.findAllByMovieId(movieId);
        List<MovieReviewUser> movieReviewUsers = new LinkedList();
        movieReviewUsers.addAll(set);
        return movieReviewUsers;
    }

    @Override
    public List<MovieReviewUser> getUserMovieReviewsByUser(int userId) {
        HashSet<MovieReviewUser> set = movieReviewUserRepository.findAllByUserId(userId);
        List<MovieReviewUser> movieReviewUsers = new LinkedList();
        movieReviewUsers.addAll(set);
        return movieReviewUsers;
    }

    @Override
    public List<Movie> getMoviesComingSoon() {
        Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        List<Movie> movies = movieRepository.findTop12ByReleaseDateAfter(date);
        movies.sort(Comparator.comparing(Movie::getReleaseDate));
        return movies;
    }

    @Override
    public List<Movie> getMoviesOutNow() {
        Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        List<Movie> movies = movieRepository.findTop12OrderByReleaseDateLessThanDesc(date);
        movies.sort(Comparator.comparing(Movie::getReleaseDate)
                .reversed());
        return movies;

    }

    @Override
    public List<Movie> getTopBoxOffice() {
        Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        List<Movie> movies = movieRepository.findTop40OrderByReleaseDateLessThanDesc(date);
        movies.sort(Comparator.comparingInt(Movie::getRevenueUnformatted)
                .reversed());
        movies = movies.subList(0,12);
        return movies;
    }
    @Override
    public List<Movie> movieSearch(String search) {
        //String needs to be parsed, and removed for duplcates.
        String[] searchString;
        if (search.contains(" ")) {
            searchString = search.split(" ");
        } else {
            searchString = new String[1];
            searchString[0] = search;
        }
        String longest = searchString[0];
        for (String string : searchString) {
            if (string.length() > longest.length()) {
                longest = string;
            }
        }

        List<Movie> movieList = movieRepository.findAllByTitleContainingIgnoreCase(longest);
        List<Movie> resultList = new LinkedList();
        resultList.addAll(movieList);
        for (String string : searchString) {
            string = string.toLowerCase();
            for (Movie movie : movieList) {
                String movieTitle = movie.getTitle().toLowerCase();
                if (!movieTitle.contains(string)) {
                    resultList.remove(movie);
                }
            }
        }
        return resultList;
    }

    @Override
    public void addUserMovieReview(MovieReviewUser movieReviewUser) {
        movieReviewUserRepository.save(movieReviewUser);
    }

    @Override
    public void editUserMovieReview(MovieReviewUser movieReviewUser) {
        movieReviewUserRepository.save(movieReviewUser);
    }

    @Override
    public void deleteUserMovieReview(int reviewId) {
        movieReviewUserRepository.deleteByReviewId(reviewId);
    }

    @Override
    public void addCriticMovieReview(MovieReviewCritic movieReviewCritic) {
        movieReviewCriticRepository.save(movieReviewCritic);

    }

    @Override
    public void editCriticMovieReview(MovieReviewCritic movieReviewCritic) {
        movieReviewCriticRepository.save(movieReviewCritic);
    }

    @Override
    public void deleteCriticMovieReview(MovieReviewCritic movieReviewCritic) {
        movieReviewCriticRepository.delete(movieReviewCritic);
    }

    @Override
    public List<Movie> findAllByTitleContainingIgnoreCase(String search) {
        return movieRepository.findAllByTitleContainingIgnoreCase(search);
    }

    @Override
    public List<Movie> getAllTimeHighestRated() {
        List<Movie> highestRated = movieRepository.findTop20OrderByRatingAvgLessThanEqual();
        Collections.sort(highestRated, (o1, o2) -> o2.getRatingAvg().compareTo(o1.getRatingAvg()));
        return highestRated;
    }

    @Override
    public List<MovieScreenshot> getMovieScreenShots(int movieId) {
        return movieScreenshotRepository.findAllByMovieId(movieId);
    }

    @Override
    public List<MovieTrailer> getMovieTrailers(int movieId) {
        return movieTrailerRepository.findAllByMovieId(movieId);
    }

    @Override
    public List<MovieActor> getMovieActors(int movieId) {
        return movieActorRepository.findAllByMovieId(movieId);
    }

    @Override
    public float getAverageUserRating(int movieId) {
        float divisor = 0;
        float value = 0;
        HashSet<MovieReviewUser> movieSet = movieReviewUserRepository.findAllByMovieId(movieId);
        for (MovieReviewUser movieReviewUser : movieSet) {
            divisor++;
            value = value + movieReviewUser.getRating();
        }
        value = value / divisor;
        return value;
    }

    @Override
    public List<Movie> movieAdvancedSearch(String search, String[] genre, String start, String end) throws ParseException {
        //String needs to be parsed, and removed for duplicates.
        String[] searchString;
        if (search.contains(" ")) {
            searchString = search.split(" ");
        } else {
            searchString = new String[1];
            searchString[0] = search;
        }
        String longest = searchString[0];
        for (String string : searchString) {
            if (string.length() > longest.length()) {
                longest = string;
            }
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsed = format.parse(start);
        java.sql.Date startDate = new java.sql.Date(parsed.getTime());
        parsed = format.parse(end);
        java.sql.Date endDate = new java.sql.Date(parsed.getTime());
        List<Movie> movieList = movieRepository.findAllByTitleContainingIgnoreCaseAndReleaseDateBetween(longest, startDate, endDate);
        List<Movie> resultList = new LinkedList();
        resultList.addAll(movieList);
        for (String string : searchString) {
            string = string.toLowerCase();
            for (Movie movie : movieList) {
                String movieTitle = movie.getTitle().toLowerCase();
                if (!movieTitle.contains(string)) {
                    resultList.remove(movie);
                }
            }
        }

        for(Movie movie : movieList){
            Set<Genre> genreList = movie.getGenre();
            for(String genreString : genre){
                if(!genreList.contains(genreString)){
                    if(resultList.contains(movie)){
                        resultList.remove(movie);
                    }
                }
            }
        }

        return resultList;
    }

    @Override
    public List<Movie> getCertifiedFresh(){
        List<Movie> certifiedFresh = movieRepository.findAllByRatingAvgGreaterThanEqualAndRatingCountGreaterThanEqual(7.5F, 80);
        return certifiedFresh;
    }

    @Override

    public List<MovieReviewUser> getUserMovieReviewsByUserAndMovie(int userId, int movieId) {
        HashSet<MovieReviewUser> set = movieReviewUserRepository.findByUserIdAndMovieId(userId, movieId);
        List<MovieReviewUser> userMovieReview = new LinkedList();
        userMovieReview.addAll(set);
        return userMovieReview;
    }


    @Override
    public List<Genre> getGenres(){
        return genreRepository.findAll();
    }

    @Override
    public List<AcademyAward> getAwardByYear(Integer year){
        return academyAwardRepository.findAllByYear(year);
    }

    @Override
    public List<Groups> getAllGroups(){
        return groupsRepository.findAll();
    }

    @Override
    public List<Critic> getCriticsByGroup(Integer groupId){
        return groupsRepository.findAllCriticByGroupId(groupId);
    }

}
