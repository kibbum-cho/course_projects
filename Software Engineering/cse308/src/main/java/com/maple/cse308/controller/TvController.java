package com.maple.cse308.controller;

import com.maple.cse308.entity.*;
import com.maple.cse308.repository.EpisodeRepository;
import com.maple.cse308.repository.SeasonRepository;
import com.maple.cse308.service.CriticServiceImpl;
import com.maple.cse308.service.ReportServiceImpl;
import com.maple.cse308.service.TvServiceImpl;
import com.maple.cse308.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class TvController {

    @Autowired
    private TvServiceImpl tvService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    SeasonRepository seasonRepository;

    @Autowired
    EpisodeRepository episodeRepository;

    @Autowired
    private ReportServiceImpl reportService;

    @Autowired
    private CriticServiceImpl criticService;

    @GetMapping("/tv")
    public String tv(Model model) {
        model.addAttribute("listType", "NEW TV TONIGHT");
        model.addAttribute("selectedList", tvService.getOpenThisWeek());
        return "tv";
    }

    @GetMapping("/tv/open")
    public String tvsopen(Model model) {
        model.addAttribute("listType", "NEW TV TONIGHT");
        model.addAttribute("selectedList", tvService.getOpenThisWeek());
        return "tv";
    }

    @GetMapping("/tv/popular")
    public String tvsPopular(Model model){
        model.addAttribute("listType", "MOST POPULAR MOVIE");
        model.addAttribute("selectedList", tvService.getPopularTv());
        return "tv";
    }

    @RequestMapping("/tv_details")
    public String tvDetails(@RequestParam(value = "id", required = false) int id, Model model) {
        model.addAttribute("popularList", tvService.getPopularTv());
        model.addAttribute("outNowList", tvService.getOpenThisWeek());
        TvReviewUser review = null;
        try {
            User user = userService.getCurrentUser();
            List<TvReviewUser> l = tvService.getUserTvReviewsByUserAndTv(user.getUserId(), id);
            if (!l.isEmpty()) {
                review = l.get(0);
            }
        } catch (Exception e) {

        }
        model.addAttribute("tv", tvService.getTvShowDetails(id));

        /*
        List<SeasonIdentity> seasonIdList = seasonRepository.findAllBySeasonTvId(id);
        List seasons = new LinkedList();
        for(SeasonIdentity seasonId : seasonIdList){
            seasons.add(seasonRepository.findBySeason(seasonId));
        }

        model.addAttribute("seasons", seasons);


        SeasonIdentity seasonId = new SeasonIdentity(1, id);
        List<EpisodeIdentity> episodeIdList = episodeRepository.findAllByEpisodeIdSeasonId(seasonId);
        List episodeList = new LinkedList();
        for(EpisodeIdentity episode : episodeIdList){
            episodeList.add(episodeRepository.findByEpisodeId(episode));

        }

        model.addAttribute("episodes", episodeList);
        */

        model.addAttribute("review", review);
        model.addAttribute("criticReviews", tvService.getCriticTvReviewsByTvShow(id));
        model.addAttribute("userReviews", tvService.getUserTvReviewsByTvShow(id));
        model.addAttribute("screenshots", tvService.getTvScreenshots(id));
        model.addAttribute("actors", tvService.getTvActors(id));
        return "tv_details";
    }


    @PostMapping("/postTvReview")
    public String postReviewTv(@ModelAttribute TvReviewUser reviewUser, Model model) {
        User user = userService.getCurrentUser();
        if(userService.confirmCurrentRole("ROLE_CRITIC")){
            Critic critic = criticService.getCriticByUser(user);
            TvReviewCritic tvReviewCritic = new TvReviewCritic();
            tvReviewCritic.setTvId(reviewUser.getTvId());
            tvReviewCritic.setCriticId(critic.getCriticId());
            tvReviewCritic.setTvId(reviewUser.getTvId());
            tvReviewCritic.setRating(reviewUser.getRating());
            tvReviewCritic.setReview(reviewUser.getReview());
            tvService.addCriticTvReview(tvReviewCritic);
        } else {
            reviewUser.setUserId(user.getUserId());
            tvService.addUserTvReview(reviewUser);
        }
        model.addAttribute("title", "Success");
        model.addAttribute("body", "Successfully posted your review!");
        return "tv_details :: serverResponseModalContent";
    }

    @PostMapping("/editTvReview")
    public String editReviewTv(@ModelAttribute TvReviewUser reviewUser, Model model) {
        int userId = userService.getCurrentUser().getUserId();
        TvReviewUser review = tvService.getUserTvReviewsByUserAndTv(userId, reviewUser.getTvId()).get(0);
        review.setRating(reviewUser.getRating());
        review.setReview(reviewUser.getReview());
        tvService.editUserTvReview(review);
        model.addAttribute("title", "Success");
        model.addAttribute("body", "Successfully edited your review!");
        return "tv_details :: serverResponseModalContent";
    }

    @GetMapping("/tv/reviewForm")
    public String updatetvReviewFormTv(@RequestParam(value = "tvId") int tvId, Model model) {
        model.addAttribute("tv", tvService.getTvShowDetails(tvId));
        int userId = userService.getCurrentUser().getUserId();
        model.addAttribute("review", tvService.getUserTvReviewsByUserAndTv(userId, tvId).get(0));
        return "tv_details :: reviewForm";
    }

    @GetMapping("/tv/reviews")
    public String updatetvReviewsTv(@RequestParam(value = "tvId") int tvId, Model model) {
        model.addAttribute("tv", tvService.getTvShowDetails(tvId));
        model.addAttribute("criticReviews", tvService.getCriticTvReviewsByTvShow(tvId));
        model.addAttribute("userReviews", tvService.getUserTvReviewsByTvShow(tvId));
        return "tv_details :: reviews";
    }


    @PostMapping("/tv/addToWantToSeeList")
    public String addToWantToSeeListTv(@RequestParam(value = "tvId") int tvId, Model model) {
        TvShow tv = tvService.getTvShowDetails(tvId);
        try {
            userService.addToWantToSeeListTv(tv);
        } catch (Exception e) {
            model.addAttribute("title", "Warning");
            model.addAttribute("body", "You cannot add a TV show that is currently on your ignore list");
            return "tv_details :: serverResponseModalContent";

        }
        model.addAttribute("title", "Success");
        model.addAttribute("body", "Successfully added to your Want To See List!");
        return "tv_details :: serverResponseModalContent";
    }

    @PostMapping("/tv/addToDontWantToSeeList")
    public String addToDontWantToSeeListTv(@RequestParam(value = "tvId") int tvId, Model model) {
        TvShow tv = tvService.getTvShowDetails(tvId);
        try {
            userService.addToDontWantToSeeListTv(tv);
        } catch (Exception e) {
            model.addAttribute("title", "Warning");
            model.addAttribute("body", "You cannot add a TV show that is currently on your watch list");
            return "tv_details :: serverResponseModalContent";
        }
        model.addAttribute("title", "Success");
        model.addAttribute("body", "Successfully added to your Not Interested List!");
        return "tv_details :: serverResponseModalContent";
    }

    @PostMapping("/tv/deleteFromWantToSeeList")
    public String deleteFromWantToSeeListTv(@RequestParam(value = "id") int tvId, Model model) {
        TvShow tv = tvService.getTvShowDetails(tvId);
        try {
            userService.removeFromWantToSeeListTv(tv);
        } catch (Exception e) {

        }
        model.addAttribute("title", "Success");
        model.addAttribute("body", "Successfully deleted from your Want To See List!");
        return "profile :: serverResponseModalContent";
    }

    @PostMapping("/tv/deleteFromNotInterestedList")
    public String deleteFromNotInterestListTv(@RequestParam(value = "id") int tvId, Model model) {
        TvShow tv = tvService.getTvShowDetails(tvId);
        try {
            userService.removeFromDontWantToSeeListTv(tv);
        } catch (Exception e) {
        }
        model.addAttribute("title", "Success");
        model.addAttribute("body", "Successfully deleted from your Not Interest List!");
        return "profile :: serverResponseModalContent";
    }

    @PostMapping("/tv/deleteTvUserReview")
    public String deletetvUserReviewTv(@RequestParam(value = "id") int reviewId, Model model) {
        tvService.deleteUserTvReview(reviewId);
        model.addAttribute("title", "Success");
        model.addAttribute("body", "Successfully deleted your review!");
        return "profile :: serverResponseModalContent";
    }

    @GetMapping("/tv/wantToSeeList")
    public String getWantToSeeTv(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        return "profile :: wantToSeeTv";
    }

    @GetMapping("/tv/notInterestedList")
    public String getNotInterestTv(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        return "profile :: notInterestedTv";
    }

    @GetMapping("/tv/userTvReviews")
    public String getUserTvReviews(Model model) {
        User user = userService.getCurrentUser();
        List<TvReviewUser> tvReviews = tvService.getUserTvReviewsByUser(user.getUserId());
        model.addAttribute("tvReviews", tvReviews);
        return "profile :: tvReviews";
    }

    @RequestMapping("/tv_all_critics")
    public String allTvCriticReviews(@RequestParam(value = "id", required = false) int id, Model model) {
        model.addAttribute("tv", tvService.getTvShowDetails(id));
        model.addAttribute("criticReviews", tvService.getCriticTvReviewsByTvShow(id));
        return "tv_all_critics";
    }

    @RequestMapping("/tv_all_actors")
    public String allTvActors(@RequestParam(value = "id", required = false) int id, Model model) {
        model.addAttribute("tv", tvService.getTvShowDetails(id));
        model.addAttribute("actors", tvService.getTvActors(id));
        return "tv_all_actors";
    }

    @PostMapping("/tv/reportUserReview")
    public String reportMovieUserReview(@RequestParam(value = "id") int reviewId, @RequestParam(value = "reason") String reason, Model model) {
        reportService.addUserTvReport(reviewId, reason);
        model.addAttribute("title", "Success");
        model.addAttribute("body", "Successfully reported the user review!");
        return "movie_details :: serverResponseModalContent";
    }

    @PostMapping("/tv/reportCriticReview")
    public String reportMovieCriticReview(@RequestParam(value = "id") int reviewId, @RequestParam(value = "reason") String reason, Model model) {
        reportService.addCriticTvReport(reviewId, reason);
        model.addAttribute("title", "Success");
        model.addAttribute("body", "Successfully reported the critic review!");
        return "movie_details :: serverResponseModalContent";
    }

}
