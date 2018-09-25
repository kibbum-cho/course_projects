package com.maple.cse308.repository;

import com.maple.cse308.entity.ActorScreenshot;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActorScreenshotRepository extends CrudRepository<ActorScreenshot, Integer> {
    List<ActorScreenshot> findAllByActorId(Integer actorId);
}
