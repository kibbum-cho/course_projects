package com.maple.cse308.repository;

import com.maple.cse308.entity.Follow;
import com.maple.cse308.entity.FollowIdentity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends CrudRepository<Follow, FollowIdentity>{
    Follow save(Follow follow);
    void delete(Follow follow);
    List<Follow> findAllByFollowIdentityUserId(Integer userId);
    List<Follow> findAllByFollowIdentityFollowingId(Integer userId);
    Follow findByFollowIdentity(FollowIdentity followIdentity);
}
