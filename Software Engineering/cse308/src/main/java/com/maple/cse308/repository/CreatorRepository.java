package com.maple.cse308.repository;

import com.maple.cse308.entity.Creator;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreatorRepository extends CrudRepository<Creator, Integer> {
    List<Creator> findAllByCreatorId(int id);
}
