package com.maple.cse308.service;

import com.maple.cse308.entity.*;
import com.maple.cse308.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

@Service
public class TvServiceImpl implements TvService {

    @Autowired
    TvReviewCriticRepository tvReviewCriticRepository;
    @Autowired
    TvReviewUserRepository tvReviewUserRepository;
    @Autowired
    private TvShowRepository tvShowRepository;
    @Autowired
    private TvScreenshotRepository tvScreenshotRepository;
    @Autowired
    private TvActorRepository tvActorRepository;
    @Autowired
    private CreatorRepository creatorRepository;
    @Autowired
    private SeasonRepository seasonRepository;
    @Autowired
    private EpisodeRepository episodeRepository;


    @Override
    public TvShow getTvShowDetails(int tvId) {
        TvShow tvShow = tvShowRepository.findTvShowByTvId(tvId);
        return tvShow;
    }

    @Override
    public void addCriticTvReview(TvReviewCritic tvReviewCritic) {
        tvReviewCriticRepository.save(tvReviewCritic);
    }

    @Override
    public void editCriticTvReview(TvReviewCritic tvReviewCritic) {
        tvReviewCriticRepository.save(tvReviewCritic);
    }

    @Override
    public void deleteCriticTvReview(int reviewId) {
        tvReviewCriticRepository.deleteByReviewId(reviewId);
    }

    @Override
    public void addUserTvReview(TvReviewUser tvReviewUser) {
        tvReviewUserRepository.save(tvReviewUser);
    }

    @Override
    public void editUserTvReview(TvReviewUser tvReviewUser) {
        tvReviewUserRepository.save(tvReviewUser);

    }

    @Override
    public void deleteUserTvReview(int reviewId) {
        tvReviewUserRepository.deleteByReviewId(reviewId);
    }

    @Override
    public List<TvReviewCritic> getCriticTvReviewsByTvShow(int tvId) {
        HashSet<TvReviewCritic> set = tvReviewCriticRepository.findAllByTvId(tvId);
        List<TvReviewCritic> list = new LinkedList();
        list.addAll(set);
        return list;

    }

    @Override
    public List<TvReviewCritic> getCriticTvReviewsByCritic(int criticId) {
        HashSet<TvReviewCritic> set = tvReviewCriticRepository.findAllByCriticId(criticId);
        List<TvReviewCritic> list = new LinkedList();
        list.addAll(set);
        return list;
    }

    @Override
    public List<TvReviewUser> getUserTvReviewsByTvShow(int tvId) {
        HashSet<TvReviewUser> set = tvReviewUserRepository.findAllByTvId(tvId);
        List<TvReviewUser> list = new LinkedList();
        list.addAll(set);
        return list;
    }

    @Override
    public List<TvReviewUser> getUserTvReviewsByUser(int userId) {
        HashSet<TvReviewUser> set = tvReviewUserRepository.findAllByUserId(userId);
        List<TvReviewUser> list = new LinkedList();
        list.addAll(set);
        return list;
    }

    @Override
    public List<TvShow> findAllByTitleContainingIgnoreCase(String search) {
        return tvShowRepository.findAllByTitleContainingIgnoreCase(search);
    }

    @Override
    public List<TvShow> tvSearch(String search) {
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

        List<TvShow> tvShowList = tvShowRepository.findAllByTitleContainingIgnoreCase(longest);
        List<TvShow> resultList = new LinkedList();
        resultList.addAll(tvShowList);
        for (String string : searchString) {
            string = string.toLowerCase();
            for (TvShow tvShow : tvShowList) {
                String tvShowTitle = tvShow.getTitle().toLowerCase();
                if (!tvShowTitle.contains(string)) {
                    resultList.remove(tvShow);
                }
            }
        }
        return resultList;
    }

    @Override
    public List<TvShow> getOpenThisWeek(){
        return tvShowRepository.findTop12ByOrderByPremierDateDesc();
    }

    @Override
    public List<TvShow> getPopularTv(){
        return tvShowRepository.findTop12ByOrderByRatingDesc();
    }

    @Override
    public List<TvScreenshot> getTvScreenshots(int id){
        return tvScreenshotRepository.findAllByTvId(id);
    }

    @Override
    public List<TvActor> getTvActors(int id){
        return tvActorRepository.findAllByTvId(id);
    }

    @Override
    public List<Creator> getTvCreator(int id){return creatorRepository.findAllByCreatorId(id);}

    @Override
    public List<TvReviewUser> getUserTvReviewsByUserAndTv(int userId, int tvId) {
        HashSet<TvReviewUser> set = tvReviewUserRepository.findByUserIdAndTvId(userId, tvId);
        List<TvReviewUser> userMovieReview = new LinkedList();
        userMovieReview.addAll(set);
        return userMovieReview;
    }


}
