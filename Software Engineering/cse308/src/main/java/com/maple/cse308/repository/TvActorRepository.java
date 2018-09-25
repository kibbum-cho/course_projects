package com.maple.cse308.repository;

import com.maple.cse308.entity.TvActor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TvActorRepository extends CrudRepository<TvActor, Integer> {
    public List<TvActor> findAllByTvId(int id);
    public List<TvActor> findAllByActorId(int id);
}
