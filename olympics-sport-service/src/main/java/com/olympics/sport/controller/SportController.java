package com.olympics.sport.controller;

import com.olympics.sport.model.Sport;
import com.olympics.sport.service.SportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sports")
public class SportController {

    @Autowired
    private SportService sportService;

    @GetMapping
    public List<Sport> getAllSports() {
        return sportService.getAllSports();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sport> getSportById(@PathVariable Long id) {
        return sportService.getSportById(id)
                .map(sport -> new ResponseEntity<>(sport, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/names")
    public ResponseEntity<List<Sport>> getSportsByName(@RequestParam List<String> names) {
        List<Sport> sports = names.stream()
                .map(name -> sportService.getSportByName(name))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        return new ResponseEntity<>(sports, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Sport> addSport(@RequestBody Sport sport) {
        return new ResponseEntity<>(sportService.addSport(sport), HttpStatus.CREATED);
    }

    @PostMapping("/all")
    public ResponseEntity<Sport> addSports(@RequestBody List<Sport> sports) {
        for (Sport sport:sports) {
            sportService.addSport(sport);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sport> updateSport(@PathVariable Long id, @RequestBody Sport updatedSport) {
        Sport sport = sportService.updateSport(id, updatedSport);
        if (sport != null) {
            return new ResponseEntity<>(sport, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSport(@PathVariable Long id) {
        sportService.deleteSport(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
