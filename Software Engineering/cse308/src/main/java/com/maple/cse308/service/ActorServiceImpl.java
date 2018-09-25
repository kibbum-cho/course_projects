package com.maple.cse308.service;

import com.maple.cse308.entity.Actor;
import com.maple.cse308.entity.ActorScreenshot;
import com.maple.cse308.entity.MovieActor;
import com.maple.cse308.entity.TvActor;
import com.maple.cse308.repository.ActorRepository;
import com.maple.cse308.repository.ActorScreenshotRepository;
import com.maple.cse308.repository.MovieActorRepository;
import com.maple.cse308.repository.TvActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class ActorServiceImpl implements ActorService {

    @Autowired
    ActorRepository actorRepository;

    @Autowired
    ActorScreenshotRepository actorScreenshotRepository;

    @Autowired
    MovieActorRepository movieActorRepository;

    @Autowired
    TvActorRepository tvActorRepository;

    @Override
    public Actor getActorDetails(int actorId) {
        Actor actor = actorRepository.findActorByActorId(actorId);
        return actor;
    }

    @Override
    public List<Actor> actorSearch(String search) {
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

        List<Actor> actorList = actorRepository.findAllByFirstNameOrMiddleNameOrLastNameIgnoreCase(longest, longest, longest);
        List<Actor> resultList = new LinkedList();
        resultList.addAll(actorList);
        for (String string : searchString) {
            string = string.toLowerCase();
            for (Actor actor : actorList) {
                String first = actor.getFirstName().toLowerCase();
                String last = actor.getLastName().toLowerCase();
                if (actor.getMiddleName() != null) {
                    String middle = actor.getMiddleName().toLowerCase();
                    if (!first.contains(string) && !middle.contains(string) && !last.contains(string)) {
                        resultList.remove(actor);
                    }
                } else {
                    if (!first.contains(string) && !last.contains(string)) {
                        resultList.remove(actor);
                    }
                }


            }
        }

        return resultList;
    }

    @Override
    public Actor getActor(int actorId) {
        return actorRepository.findActorByActorId(actorId);
    }

    @Override
    public List<ActorScreenshot> getActorScreenshots(int actorId) {
        return actorScreenshotRepository.findAllByActorId(actorId);
    }

    @Override
    public List<MovieActor> getActorMovies(int actorId) {
        return movieActorRepository.findAllByActorId(actorId);
    }

    @Override
    public List<TvActor> getActorTVs(int actorId) {
        return tvActorRepository.findAllByActorId(actorId);
    }


}
