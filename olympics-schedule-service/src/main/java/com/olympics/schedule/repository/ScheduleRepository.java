package com.olympics.schedule.repository;

import com.olympics.schedule.model.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends MongoRepository<Schedule, String> {
    Optional<Schedule> findBySiteAndSport(String site, String sport);
    List<Schedule> findBySite(String site);
    List<Schedule> findBySport(String sport);
    Optional<Schedule> findBySiteAndSportAndCalendrierNom(String site, String sport, String nom);
}
