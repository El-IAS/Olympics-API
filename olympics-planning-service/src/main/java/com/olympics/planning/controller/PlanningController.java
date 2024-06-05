package com.olympics.planning.controller;

import com.olympics.planning.model.Planning;
import com.olympics.planning.service.PlanningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plannings")
public class PlanningController {

    @Autowired
    private PlanningService planningService;

    @GetMapping
    public List<Planning> getAllPlannings() {
        return planningService.getAllPlannings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Planning> getPlanningById(@PathVariable String id) {
        return planningService.getPlanningById(id)
                .map(planning -> new ResponseEntity<>(planning, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/user/{username}")
    public List<Planning> getPlanningsByUsername(@PathVariable String username) {
        return planningService.getPlanningsByUsername(username);
    }

    @PostMapping
    public ResponseEntity<Planning> createPlanning(@RequestBody Planning planning) {
        return new ResponseEntity<>(planningService.createPlanning(planning), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Planning> updatePlanning(@PathVariable String id, @RequestBody Planning updatedPlanning) {
        Planning planning = planningService.updatePlanning(id, updatedPlanning);
        if (planning != null) {
            return new ResponseEntity<>(planning, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlanning(@PathVariable String id) {
        planningService.deletePlanning(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
