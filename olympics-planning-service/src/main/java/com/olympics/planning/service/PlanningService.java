package com.olympics.planning.service;

import com.olympics.planning.model.PlanEvent;
import com.olympics.planning.model.Planning;
import com.olympics.planning.repository.PlanningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PlanningService {

    @Autowired
    private PlanningRepository planningRepository;

    public List<Planning> getAllPlannings() {
        return planningRepository.findAll();
    }

    public Optional<Planning> getPlanningById(String id) {
        return planningRepository.findById(id);
    }

    public List<Planning> getPlanningsByUsername(String username) {
        return planningRepository.findByUsername(username);
    }

    public Planning createPlanning(Planning planning) {
        List<Planning> existingPlannings = planningRepository.findByUsername(planning.getUsername());
        if (existingPlannings.isEmpty()) {
            // No existing planning for the user, create a new one
            return planningRepository.save(planning);
        } else {
            // User already has a planning, append new events
            Planning existingPlanning = existingPlannings.get(0);
            Set<String> existingEventNames = existingPlanning.getEvents().stream()
                    .map(PlanEvent::getEventName)
                    .collect(Collectors.toSet());

            List<PlanEvent> newEvents = planning.getEvents().stream()
                    .filter(event -> !existingEventNames.contains(event.getEventName()))
                    .collect(Collectors.toList());

            existingPlanning.getEvents().addAll(newEvents);
            return planningRepository.save(existingPlanning);
        }
    }

    public Planning updatePlanning(String id, Planning updatedPlanning) {
        if (planningRepository.existsById(id)) {
            updatedPlanning.setId(id);
            return planningRepository.save(updatedPlanning);
        }
        return null;
    }

    public void deletePlanning(String id) {
        planningRepository.deleteById(id);
    }
}
