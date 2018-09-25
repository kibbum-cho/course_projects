package com.maple.cse308.repository;

import com.maple.cse308.entity.TvScreenshot;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TvScreenshotRepository extends CrudRepository<TvScreenshot, Integer> {
    List<TvScreenshot> findAllByTvId(Integer Id);
}
