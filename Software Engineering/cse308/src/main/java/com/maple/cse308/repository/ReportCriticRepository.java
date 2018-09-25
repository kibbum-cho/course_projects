package com.maple.cse308.repository;

import com.maple.cse308.entity.ReportCritic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportCriticRepository extends CrudRepository<ReportCritic, Integer>{
    ReportCritic save(ReportCritic reportCritic);
    List<ReportCritic> findAll();
    ReportCritic findByCriticId(Integer criticId);
    void delete(ReportCritic reportCritic);

}
