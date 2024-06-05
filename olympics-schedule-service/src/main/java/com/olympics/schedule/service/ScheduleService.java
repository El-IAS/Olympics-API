package com.olympics.schedule.service;

import com.olympics.schedule.model.Event;
import com.olympics.schedule.model.Schedule;
import com.olympics.schedule.model.Site;
import com.olympics.schedule.model.Sport;
import com.olympics.schedule.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private RestTemplate restTemplate;

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public Optional<Schedule> getScheduleById(String id) {
        return scheduleRepository.findById(id);
    }

    public List<Schedule> findSchedules(Optional<String> id, Optional<String> name, Optional<String> sport, Optional<String> date) {
        List<Schedule> schedules = scheduleRepository.findAll();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
        boolean isDateTime = date.isPresent() && date.get().contains("T");

        return schedules.stream()
                .filter(schedule -> id.map(s -> s.equals(schedule.getId())).orElse(true))
                .filter(schedule -> name.map(n -> n.equalsIgnoreCase(schedule.getSite())).orElse(true))
                .filter(schedule -> sport.map(s -> s.equalsIgnoreCase(schedule.getSport())).orElse(true))
                .filter(schedule -> date.map(d -> {
                    if (isDateTime) {
                        LocalDateTime queryDateTime = LocalDateTime.parse(d, dateTimeFormatter);
                        return schedule.getCalendrier().stream()
                                .anyMatch(event -> {
                                    LocalDateTime eventStart = LocalDateTime.parse(event.getDebut(), dateTimeFormatter);
                                    LocalDateTime eventEnd = LocalDateTime.parse(event.getFin(), dateTimeFormatter);
                                    return (queryDateTime.isEqual(eventStart) || queryDateTime.isEqual(eventEnd))
                                            || (queryDateTime.isAfter(eventStart) && queryDateTime.isBefore(eventEnd));
                                });
                    } else {
                        LocalDate queryDate = LocalDate.parse(d, dateFormatter);
                        return schedule.getCalendrier().stream()
                                .anyMatch(event -> {
                                    LocalDate eventStart = LocalDateTime.parse(event.getDebut(), dateTimeFormatter).toLocalDate();
                                    LocalDate eventEnd = LocalDateTime.parse(event.getFin(), dateTimeFormatter).toLocalDate();
                                    return (queryDate.isEqual(eventStart) || queryDate.isEqual(eventEnd))
                                            || (queryDate.isAfter(eventStart) && queryDate.isBefore(eventEnd));
                                });
                    }
                }).orElse(true))
                .collect(Collectors.toList());
    }

    // Method to find sports by date and/or site
    public List<Sport> findSportsByDateAndSite(Optional<String> site, Optional<String> date) {
        List<Schedule> schedules = scheduleRepository.findAll();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
        boolean isDateTime = date.isPresent() && date.get().contains("T");

        List<String> sportsNames = schedules.stream()
                .filter(schedule -> site.map(s -> s.equals(schedule.getSite())).orElse(true))
                .filter(schedule -> date.map(d -> {
                    if (isDateTime) {
                        LocalDateTime queryDateTime = LocalDateTime.parse(d, dateTimeFormatter);
                        return schedule.getCalendrier().stream()
                                .anyMatch(event -> {
                                    LocalDateTime eventStart = LocalDateTime.parse(event.getDebut(), dateTimeFormatter);
                                    LocalDateTime eventEnd = LocalDateTime.parse(event.getFin(), dateTimeFormatter);
                                    return (queryDateTime.isEqual(eventStart) || queryDateTime.isEqual(eventEnd))
                                            || (queryDateTime.isAfter(eventStart) && queryDateTime.isBefore(eventEnd));
                                });
                    } else {
                        LocalDate queryDate = LocalDate.parse(d, dateFormatter);
                        return schedule.getCalendrier().stream()
                                .anyMatch(event -> {
                                    LocalDate eventStart = LocalDateTime.parse(event.getDebut(), dateTimeFormatter).toLocalDate();
                                    LocalDate eventEnd = LocalDateTime.parse(event.getFin(), dateTimeFormatter).toLocalDate();
                                    return (queryDate.isEqual(eventStart) || queryDate.isEqual(eventEnd))
                                            || (queryDate.isAfter(eventStart) && queryDate.isBefore(eventEnd));
                                });
                    }
                }).orElse(true))
                .map(Schedule::getSport)
                .distinct()
                .collect(Collectors.toList());

        List<Sport> sports = new ArrayList<>();
        try {
            String url = "http://localhost:8082/api/sports/names?names=" + String.join(",", sportsNames);
            ResponseEntity<Sport[]> response = restTemplate.getForEntity(url, Sport[].class);
            if (response.getStatusCode() == HttpStatus.OK) {
                sports.addAll(Arrays.asList(response.getBody()));
            }
        } catch (HttpClientErrorException e) {
            System.err.println("Error fetching sports: " + e.getMessage());
        }
        return sports;
    }

    // Method to find sites by sport and/or date
    public List<Site> findSitesBySportAndDate(Optional<String> sport, Optional<String> date) {
        List<Schedule> schedules = scheduleRepository.findAll();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
        boolean isDateTime = date.isPresent() && date.get().contains("T");

        List<String> siteNames = schedules.stream()
                .filter(schedule -> sport.map(s -> s.equals(schedule.getSport())).orElse(true))
                .filter(schedule -> date.map(d -> {
                    if (isDateTime) {
                        LocalDateTime queryDateTime = LocalDateTime.parse(d, dateTimeFormatter);
                        return schedule.getCalendrier().stream()
                                .anyMatch(event -> {
                                    LocalDateTime eventStart = LocalDateTime.parse(event.getDebut(), dateTimeFormatter);
                                    LocalDateTime eventEnd = LocalDateTime.parse(event.getFin(), dateTimeFormatter);
                                    return (queryDateTime.isEqual(eventStart) || queryDateTime.isEqual(eventEnd))
                                            || (queryDateTime.isAfter(eventStart) && queryDateTime.isBefore(eventEnd));
                                });
                    } else {
                        LocalDate queryDate = LocalDate.parse(d, dateFormatter);
                        return schedule.getCalendrier().stream()
                                .anyMatch(event -> {
                                    LocalDate eventStart = LocalDateTime.parse(event.getDebut(), dateTimeFormatter).toLocalDate();
                                    LocalDate eventEnd = LocalDateTime.parse(event.getFin(), dateTimeFormatter).toLocalDate();
                                    return (queryDate.isEqual(eventStart) || queryDate.isEqual(eventEnd))
                                            || (queryDate.isAfter(eventStart) && queryDate.isBefore(eventEnd));
                                });
                    }
                }).orElse(true))
                .map(Schedule::getSite)
                .distinct()
                .collect(Collectors.toList());

        List<Site> sites = new ArrayList<>();
        try {
            String url = "http://localhost:8081/api/sites/names?names=" + String.join(",", siteNames);
            ResponseEntity<Site[]> response = restTemplate.getForEntity(url, Site[].class);
            if (response.getStatusCode() == HttpStatus.OK) {
                sites.addAll(Arrays.asList(response.getBody()));
            }
        } catch (HttpClientErrorException e) {
            System.err.println("Error fetching sites: " + e.getMessage());
        }
        return sites;
    }

    public Schedule addSchedule(Schedule schedule) {
        Optional<Schedule> existingSchedule = scheduleRepository.findBySiteAndSport(schedule.getSite(), schedule.getSport());
        if (existingSchedule.isPresent()) {
            Schedule currentSchedule = existingSchedule.get();
            List<Event> currentEvents = currentSchedule.getCalendrier();

            for (Event newEvent : schedule.getCalendrier()) {
                boolean exists = currentEvents.stream().anyMatch(event ->
                        event.getNom().equals(newEvent.getNom()) &&
                                event.getDebut().equals(newEvent.getDebut()) &&
                                event.getFin().equals(newEvent.getFin()));
                if (!exists) {
                    currentEvents.add(newEvent);
                }
            }
            currentSchedule.setCalendrier(currentEvents);
            return scheduleRepository.save(currentSchedule);
        } else {
            return scheduleRepository.save(schedule);
        }
    }

    public Schedule updateSchedule(String site, String sport, List<Event> events) {
        Optional<Schedule> existingSchedule = scheduleRepository.findBySiteAndSport(site, sport);
        if (existingSchedule.isPresent()) {
            Schedule schedule = existingSchedule.get();
            schedule.setCalendrier(events);
            return scheduleRepository.save(schedule);
        }
        return null;
    }

    public void deleteScheduleBySite(String site) {
        List<Schedule> schedules = scheduleRepository.findBySite(site);
        scheduleRepository.deleteAll(schedules);
    }

    public void deleteScheduleBySport(String sport) {
        List<Schedule> schedules = scheduleRepository.findBySport(sport);
        scheduleRepository.deleteAll(schedules);
    }

    public void deleteScheduleBySiteAndSport(String site, String sport) {
        Optional<Schedule> existingSchedule = scheduleRepository.findBySiteAndSport(site, sport);
        existingSchedule.ifPresent(scheduleRepository::delete);
    }

    public void deleteEvent(String site, String sport, String eventName) {
        Optional<Schedule> existingSchedule = scheduleRepository.findBySiteAndSport(site, sport);
        if (existingSchedule.isPresent()) {
            Schedule schedule = existingSchedule.get();
            List<Event> events = schedule.getCalendrier();
            events.removeIf(event -> event.getNom().equals(eventName));
            schedule.setCalendrier(events);
            scheduleRepository.save(schedule);
        }
    }
}
