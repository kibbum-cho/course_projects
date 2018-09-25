package com.maple.cse308.repository;

import com.maple.cse308.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

    Set<Role> findByRole(String role);
}
