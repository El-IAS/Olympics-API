package com.olympics.planning.repository;

import com.olympics.planning.model.Planning;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanningRepository extends MongoRepository<Planning, String> {
    List<Planning> findByUsername(String username);
}
