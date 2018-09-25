package com.maple.cse308.controller;

import com.maple.cse308.entity.*;
import com.maple.cse308.repository.CriticRepository;
import com.maple.cse308.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.List;

@Controller
@RequestMapping("/")
public class MovieController {

    @Autowired
    private MovieServiceImpl movieService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ReportServiceImpl reportService;

    @Autowired
    private CriticServiceImpl criticService;

    @GetMapping("/movies")
    public String movies(Model model) {
        model.addAttribute("listType", "OPENING THIS WEEK");
        model.addAttribute("selectedList", movieService.getMoviesOutNow());
        return "movies";
    }

    @RequestMapping("/movie_details")
    public String movieDetails(@RequestParam(value = "id", required = false) int id, Model model) {
        model.addAttribute("comingSoonList", movieService.getMoviesComingSoon());
        model.addAttribute("outNowList", movieService.getMoviesOutNow());
        model.addAttribute("topBoxOfficeList", movieService.getTopBoxOffice());
        MovieReviewUser review = null;
        try {
            User user = userService.getCurrentUser();
            List<MovieReviewUser> l = movieService.getUserMovieReviewsByUserAndMovie(user.getUserId(), id);
            if (!l.isEmpty()) {
                review = l.get(0);
            }
        } catch (Exception e) {

        }

        List<MovieReviewCritic> mrcList = movieService.getCriticMovieReviewsByMovie(id);
        Float criticRating = 0F;
        for(MovieReviewCritic mrc : mrcList){
            if(mrc.getRating() != null) {
                criticRating += mrc.getRating();
            }
        }
        criticRating = mrcList.size() > 0 ? criticRating/mrcList.size() : 0F;
        List<MovieReviewUser> mruList = movieService.getUserMovieReviewsByMovie(id);
        Float userRating = 0F;
        for(MovieReviewUser mru : mruList){
            if(mru.getRating() != null) {
                userRating += mru.getRating();
            }
        }
        userRating = mruList.size() > 0 ? userRating/mruList.size() : 0F;
        DecimalFormat df = new DecimalFormat("#.#");
        model.addAttribute("movie", movieService.getMovieDetails(id));
        model.addAttribute("review", review);
        model.addAttribute("criticReviews", mrcList);
        model.addAttribute("criticRating", df.format(criticRating));
        model.addAttribute("userReviews", mruList);
        model.addAttribute("userRating", df.format(userRating));
        model.addAttribute("screenshots", movieService.getMovieScreenShots(id));
        model.addAttribute("trailers", movieService.getMovieTrailers(id));
        model.addAttribute("actors", movieService.getMovieActors(id));
        return "movie_details";
    }

    @GetMapping("/movies/opening")
    public String moviesOpeningSoon(Model model) {
        model.addAttribute("listType", "OPENING THIS WEEK");
        model.addAttribute("selectedList", movieService.getMoviesOutNow());
        return "movies";
    }

    @GetMapping("/movies/topbox")
    public String moviesTopBoxOffice(Model model) {
        model.addAttribute("listType", "TOP BOX OFFICE");
        model.addAttribute("selectedList", movieService.getTopBoxOffice());
        model.addAttribute("topbox","topbox");
        return "movies";
    }

    @GetMapping("/movies/coming")
    public String moviesComingSoon(Model model) {
        model.addAttribute("listType", "COMING SOON");
        model.addAttribute("selectedList", movieService.getMoviesComingSoon());
        return "movies";
    }

    @GetMapping("/movies/highest")
    public String highestRated(Model model){
        model.addAttribute("listType", "HIGHEST RATED");
        model.addAttribute("selectedList", movieService.getAllTimeHighestRated());
        return "movies";
    }

    @PostMapping("/postMovieReview")
    public String postReview(@ModelAttribute MovieReviewUser reviewUser, Model model){
        User user = userService.getCurrentUser();
        if(userService.confirmCurrentRole("ROLE_CRITIC")){
            Critic critic = criticService.getCriticByUser(user);
            MovieReviewCritic movieReviewCritic = new MovieReviewCritic();
            movieReviewCritic.setMovieId(reviewUser.getMovieId());
            movieReviewCritic.setCriticId(critic.getCriticId());
            movieReviewCritic.setMovieId(reviewUser.getMovieId());
            movieReviewCritic.setRating(reviewUser.getRating());
            movieReviewCritic.setReview(reviewUser.getReview());
            movieService.addCriticMovieReview(movieReviewCritic);
        } else {
            reviewUser.setUserId(user.getUserId());
            movieService.addUserMovieReview(reviewUser);
        }

        model.addAttribute("title", "Success");
        model.addAttribute("body", "Successfully posted your review!");
        return "movie_details :: serverResponseModalContent";
    }

    @GetMapping("/movies/certfied")
    public String moviesCertifiedFresh(Model model) {
        model.addAttribute("certifiedFresh", movieService.getCertifiedFresh());
        return "movies";
    }

    @PostMapping("/editMovieReview")
    public String editReview(@ModelAttribute MovieReviewUser reviewUser, Model model){
        int userId = userService.getCurrentUser().getUserId();
        MovieReviewUser review = movieService.getUserMovieReviewsByUserAndMovie(userId, reviewUser.getMovieId()).get(0);
        review.setRating(reviewUser.getRating());
        review.setReview(reviewUser.getReview());
        movieService.editUserMovieReview(review);
        List<MovieReviewUser> mruList = movieService.getUserMovieReviewsByMovie(reviewUser.getMovieId());
        Float userRating = 0F;
        for(MovieReviewUser mru : mruList){
            if(mru.getRating() != null) {
                userRating += mru.getRating();
            }
        }
        userRating = mruList.size() > 0 ? userRating/mruList.size() : 0F;
        model.addAttribute("userRating", userRating);
        model.addAttribute("title", "Success");
        model.addAttribute("body", "Successfully edited your review!");
        return "movie_details :: serverResponseModalContent";
    }

    @GetMapping("/movie/reviewForm")
    public String updateMovieReviewForm(@RequestParam(value = "movieId") int movieId, Model model) {
        model.addAttribute("movie", movieService.getMovieDetails(movieId));
        int userId = userService.getCurrentUser().getUserId();
        model.addAttribute("review", movieService.getUserMovieReviewsByUserAndMovie(userId, movieId).get(0));
        List<MovieReviewCritic> mrcList = movieService.getCriticMovieReviewsByMovie(movieId);
        Float criticRating = 0F;
        for(MovieReviewCritic mrc : mrcList){
            if(mrc.getRating() != null) {
                criticRating += mrc.getRating();
            }
        }
        criticRating = mrcList.size() > 0 ? criticRating/mrcList.size() : 0F;
        List<MovieReviewUser> mruList = movieService.getUserMovieReviewsByMovie(movieId);
        Float userRating = 0F;

        DecimalFormat df = new DecimalFormat("#.#");
        for(MovieReviewUser mru : mruList){
            if(mru.getRating() != null) {
                userRating += mru.getRating();
            }
        }
        userRating = mruList.size() > 0 ? userRating/mruList.size() : 0F;
        model.addAttribute("criticReviews", movieService.getCriticMovieReviewsByMovie(movieId));
        model.addAttribute("userReviews", movieService.getUserMovieReviewsByMovie(movieId));
        model.addAttribute("criticRating", df.format(criticRating));
        model.addAttribute("userRating", df.format(userRating));
        return "movie_details :: reviewForm";
    }

    @GetMapping("/movie/reviews")
    public String updateMovieReviews(@RequestParam(value = "movieId") int movieId, Model model) {
        model.addAttribute("movie", movieService.getMovieDetails(movieId));
        model.addAttribute("criticReviews", movieService.getCriticMovieReviewsByMovie(movieId));
        model.addAttribute("userReviews", movieService.getUserMovieReviewsByMovie(movieId));
        return "movie_details :: reviews";
    }


    @PostMapping("/movie/addToWantToSeeList")
    public String addToWantToSeeList(@RequestParam(value = "movieId") int movieId, Model model){
        Movie movie = movieService.getMovieDetails(movieId);
        try {
            userService.addToWantToSeeList(movie);
        } catch (Exception e) {
            model.addAttribute("title", "Warning");
            model.addAttribute("body", "Warning: You cannot add a movie that is currently on your ignore list");
            return "movie_details :: serverResponseModalContent";

        }
        model.addAttribute("title", "Success");
        model.addAttribute("body", "Successfully added to your Want To See List!");
        return "movie_details :: serverResponseModalContent";
    }

    @PostMapping("/movie/addToDontWantToSeeList")
    public String addToDontWantToSeeList(@RequestParam(value = "movieId") int movieId, Model model){
        Movie movie = movieService.getMovieDetails(movieId);
        try {
            userService.addToDontWantToSeeList(movie);
        } catch (Exception e) {
            model.addAttribute("title", "Warning");
            model.addAttribute("body", "Warning: You cannot add a movie that is currently on your watch list");
            return "movie_details :: serverResponseModalContent";
        }
        model.addAttribute("title", "Success");
        model.addAttribute("body", "Successfully added to your Not Interested List!");
        return "movie_details :: serverResponseModalContent";
    }

    @PostMapping("/movie/deleteFromWantToSeeList")
    public String deleteFromWantToSeeList(@RequestParam(value = "id") int movieId, Model model)  {
        Movie movie = movieService.getMovieDetails(movieId);
        try {
            userService.removeFromWantToSeeList(movie);
        } catch (Exception e) {

        }
        model.addAttribute("title", "Success");
        model.addAttribute("body", "Successfully deleted from your Want To See List!");
        return "profile :: serverResponseModalContent";
    }

    @PostMapping("/movie/deleteFromNotInterestedList")
    public String deleteFromNotInterestList(@RequestParam(value = "id") int movieId, Model model) {
        Movie movie = movieService.getMovieDetails(movieId);
        try {
            userService.removeFromDontWantToSeeList(movie);
        } catch (Exception e) {
        }
        model.addAttribute("title", "Success");
        model.addAttribute("body", "Successfully deleted from your Not Interest List!");
        return "profile :: serverResponseModalContent";
    }

    @PostMapping("/movie/deleteMovieUserReview")
    public String deleteMovieUserReview(@RequestParam(value = "id") int reviewId, Model model) {
        movieService.deleteUserMovieReview(reviewId);
        model.addAttribute("title", "Success");
        model.addAttribute("body", "Successfully deleted your review!");
        return "profile :: serverResponseModalContent";
    }

    @GetMapping("/movie/wantToSeeMovieList")
    public String getWantToSeeMovie(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        return "profile :: wantToSeeMovie";
    }

    @GetMapping("/movie/notInterestedList")
    public String getNotInterestMovie(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        return "profile :: notInterestedMovie";
    }

    @GetMapping("/movie/userMovieReviews")
    public String getUserMovieReviews(Model model) {
        User user = userService.getCurrentUser();
        List<MovieReviewUser> movieReviews = movieService.getUserMovieReviewsByUser(user.getUserId());
        model.addAttribute("movieReviews", movieReviews);
        return "profile :: movieReviews";
    }

    @RequestMapping("/movie_trailers")
    public String allMovieTrailers(@RequestParam(value = "id", required = false) int id, Model model) {
        model.addAttribute("movie", movieService.getMovieDetails(id));
        model.addAttribute("trailers", movieService.getMovieTrailers(id));
        return "movie_trailers";
    }

    @RequestMapping("/movie_all_critics")
    public String allMovieCriticReviews(@RequestParam(value = "id", required = false) int id, Model model) {
        model.addAttribute("movie", movieService.getMovieDetails(id));
        model.addAttribute("criticReviews", movieService.getCriticMovieReviewsByMovie(id));
        return "movie_all_critics";
    }

    @RequestMapping("/movie_all_actors")
    public String allMovieActors(@RequestParam(value = "id", required = false) int id, Model model) {
        model.addAttribute("movie", movieService.getMovieDetails(id));
        model.addAttribute("actors", movieService.getMovieActors(id));
        return "movie_all_actors";
    }


    @PostMapping("/movie/reportUserReview")
    public String reportMovieUserReview(@RequestParam(value = "id") int reviewId, @RequestParam(value = "reason") String reason, Model model)  {
        reportService.addUserMovieReport(reviewId, reason);
        model.addAttribute("title", "Success");
        model.addAttribute("body", "Successfully reported the user review!");
        return "movie_details :: serverResponseModalContent";
    }

    @PostMapping("/movie/reportCriticReview")
    public String reportMovieCriticReview(@RequestParam(value = "id") int reviewId, @RequestParam(value = "reason") String reason, Model model) {
        reportService.addCriticMovieReport(reviewId, reason);
        model.addAttribute("title", "Success");
        model.addAttribute("body", "Successfully reported the critic review!");
        return "movie_details :: serverResponseModalContent";
    }

}























