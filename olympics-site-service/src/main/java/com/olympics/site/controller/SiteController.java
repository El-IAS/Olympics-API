package com.olympics.site.controller;

import com.olympics.site.model.PlanEvent;
import com.olympics.site.model.Site;
import com.olympics.site.model.SiteRelationship;
import com.olympics.site.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sites")
public class SiteController {

    @Autowired
    private SiteService siteService;

    @GetMapping
    public List<Site> getAllSites() {
        return siteService.getAllSites();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Site> getSiteById(@PathVariable Long id) {
        return siteService.getSiteById(id)
                .map(site -> new ResponseEntity<>(site, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/names")
    public ResponseEntity<List<Site>> getSitesByName(@RequestParam List<String> names) {
        List<Site> sites = names.stream()
                .map(name -> siteService.getSiteByName(name))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        return new ResponseEntity<>(sites, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Site> addSite(@RequestBody Site site) {
        return new ResponseEntity<>(siteService.addSite(site), HttpStatus.CREATED);
    }

    @PostMapping("/all")
    public ResponseEntity<Site> addSites(@RequestBody List<Site> sites) {
        for (Site site:sites) {
            siteService.addSite(site);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Site> updateSite(@PathVariable Long id, @RequestBody Site updatedSite) {
        Site site = siteService.updateSite(id, updatedSite);
        if (site != null) {
            return new ResponseEntity<>(site, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSite(@PathVariable Long id) {
        siteService.deleteSite(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Redis operations
    @PostMapping("/relationship")
    public ResponseEntity<Void> setRelationship(@RequestBody SiteRelationship relationship) {
        siteService.setRelationship(relationship);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/relationship/all")
    public ResponseEntity<Void> setRelationships(@RequestBody List<SiteRelationship> relationships) {
        for (SiteRelationship relationship:relationships) {
            siteService.setRelationship(relationship);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/relationship")
    public ResponseEntity<SiteRelationship> getRelationship(@RequestParam String site1, @RequestParam String site2) {
        SiteRelationship relationship = siteService.getRelationship(site1, site2);
        return new ResponseEntity<>(relationship, HttpStatus.OK);
    }

    @GetMapping("/relationships")
    public ResponseEntity<List<SiteRelationship>> getAllRelationships() {
        List<SiteRelationship> relationships = siteService.getAllRelationships();
        return new ResponseEntity<>(relationships, HttpStatus.OK);
    }

    @PostMapping("/totalTravelTimes")
    public ResponseEntity<SiteRelationship> getTotalTravelTimes(@RequestBody List<PlanEvent> planEvents) {
        SiteRelationship totalTravelTimes = siteService.getTotalTravelTimes(planEvents);
        return new ResponseEntity<>(totalTravelTimes, HttpStatus.OK);
    }
}
