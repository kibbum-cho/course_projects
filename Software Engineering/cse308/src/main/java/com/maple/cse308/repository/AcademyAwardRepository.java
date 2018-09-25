package com.maple.cse308.repository;

import com.maple.cse308.entity.AcademyAward;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcademyAwardRepository extends CrudRepository<AcademyAward, Integer>{
    List<AcademyAward> findAllByYear(Integer year);
}
