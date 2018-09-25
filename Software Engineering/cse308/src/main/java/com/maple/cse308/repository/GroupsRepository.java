package com.maple.cse308.repository;

import com.maple.cse308.entity.Critic;
import com.maple.cse308.entity.Groups;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface GroupsRepository extends CrudRepository<Groups, Integer> {

    Groups save(Groups g);

    Boolean existsByGroupName(String groupName);

    Groups findByGroupName(String groupName);

    List<Groups> findAll();

    @Query(value="SELECT * FROM critic WHERE critic_id IN" +
            "(SELECT critic_id FROM critic_groups_table WHERE group_id = ?1)", nativeQuery = true)
    List<Critic> findAllCriticByGroupId(Integer groupId);

}
