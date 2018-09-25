package com.maple.cse308.repository;

import com.maple.cse308.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    User findByUsername(String username);

    User findByEmail(String email);

    User findByResetToken(String token);

    User findByUserId(Integer userId);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}
