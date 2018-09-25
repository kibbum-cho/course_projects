package com.maple.cse308.repository;

import com.maple.cse308.entity.ReportUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportUserRepository extends CrudRepository<ReportUser, Integer> {
    ReportUser save(ReportUser reportUser);
    List<ReportUser> findAll();
    ReportUser findByUserId(Integer userId);
    void delete(ReportUser reportUser);
}
