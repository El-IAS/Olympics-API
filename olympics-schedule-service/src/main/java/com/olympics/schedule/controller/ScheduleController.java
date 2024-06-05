package com.olympics.schedule.controller;

import com.olympics.schedule.model.Event;
import com.olympics.schedule.model.Schedule;
import com.olympics.schedule.model.Site;
import com.olympics.schedule.model.Sport;
import com.olympics.schedule.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<List<Schedule>> getSchedules(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String sport,
            @RequestParam(required = false) String date) {

        List<Schedule> schedules = scheduleService.findSchedules(
                Optional.ofNullable(id),
                Optional.ofNullable(name),
                Optional.ofNullable(sport),
                Optional.ofNullable(date)
        );

        if (schedules.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(schedules, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Schedule> getScheduleById(@PathVariable String id) {
        return scheduleService.getScheduleById(id)
                .map(schedule -> new ResponseEntity<>(schedule, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }



    @GetMapping("/findSports")
    public ResponseEntity<List<Sport>> findSportsByDateAndSite(
            @RequestParam Optional<String> site,
            @RequestParam Optional<String> date) {
        List<Sport> sports = scheduleService.findSportsByDateAndSite(site, date);
        if (!sports.isEmpty()) {
            return new ResponseEntity<>(sports, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint to find sites by sport and/or date
    @GetMapping("/findSites")
    public ResponseEntity<List<Site>> findSitesBySportAndDate(
            @RequestParam Optional<String> sport,
            @RequestParam Optional<String> date) {
        List<Site> sites = scheduleService.findSitesBySportAndDate(sport, date);
        if (!sites.isEmpty()) {
            return new ResponseEntity<>(sites, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Schedule> addSchedule(@RequestBody Schedule schedule) {
        return new ResponseEntity<>(scheduleService.addSchedule(schedule), HttpStatus.CREATED);
    }

    @PostMapping("/all")
    public ResponseEntity<Schedule> addSchedules(@RequestBody List<Schedule> schedules) {

        for (Schedule schedule:schedules) {
            scheduleService.addSchedule(schedule);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{site}/{sport}")
    public ResponseEntity<Schedule> updateSchedule(@PathVariable String site, @PathVariable String sport, @RequestBody List<Event> events) {
        Schedule updatedSchedule = scheduleService.updateSchedule(site, sport, events);
        if (updatedSchedule != null) {
            return new ResponseEntity<>(updatedSchedule, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteSchedule(
            @RequestParam(required = false) String site,
            @RequestParam(required = false) String sport,
            @RequestParam(required = false) String event) {

        if (site != null && sport != null && event != null) {
            scheduleService.deleteEvent(site, sport, event);
        } else if (site != null && sport != null) {
            scheduleService.deleteScheduleBySiteAndSport(site, sport);
        } else if (site != null) {
            scheduleService.deleteScheduleBySite(site);
        } else if (sport != null) {
            scheduleService.deleteScheduleBySport(sport);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
