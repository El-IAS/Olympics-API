package com.olympics.site.service;

import com.olympics.site.model.PlanEvent;
import com.olympics.site.model.Site;
import com.olympics.site.model.SiteRelationship;
import com.olympics.site.repository.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class SiteService {

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public List<Site> getAllSites() {
        return siteRepository.findAll();
    }

    public Optional<Site> getSiteById(Long id) {
        return siteRepository.findById(id);
    }

    public Optional<Site> getSiteByName(String name) {
        return siteRepository.findByNom(name);
    }

    public Site addSite(Site site) {
        return siteRepository.save(site);
    }

    public Site updateSite(Long id, Site updatedSite) {
        if (siteRepository.existsById(id)) {
            updatedSite.setId(id);
            return siteRepository.save(updatedSite);
        }
        return null;
    }

    public void deleteSite(Long id) {
        siteRepository.deleteById(id);
    }

    // Redis operations
    public void setRelationship(SiteRelationship relationship) {
        String site1 = relationship.getSite1();
        String site2 = relationship.getSite2();

        if (relationship.getDistance() != null) {
            redisTemplate.opsForValue().set("distance:" + site1 + ":" + site2, relationship.getDistance());
        }
        if (relationship.getTravelTimeByCar() != null) {
            redisTemplate.opsForValue().set("tempsTrajetVoiture:" + site1 + ":" + site2, relationship.getTravelTimeByCar());
        }
        if (relationship.getTravelTimeByTransport() != null) {
            redisTemplate.opsForValue().set("tempsTrajetTransport:" + site1 + ":" + site2, relationship.getTravelTimeByTransport());
        }
    }

    public SiteRelationship getRelationship(String site1, String site2) {
        String distanceStr = (String) redisTemplate.opsForValue().get("distance:" + site1 + ":" + site2);
        String travelTimeByCarStr = (String) redisTemplate.opsForValue().get("tempsTrajetVoiture:" + site1 + ":" + site2);
        String travelTimeByTransportStr = (String) redisTemplate.opsForValue().get("tempsTrajetTransport:" + site1 + ":" + site2);

        Double distance = distanceStr != null ? Double.valueOf(distanceStr) : null;
        Double travelTimeByCar = travelTimeByCarStr != null ? Double.valueOf(travelTimeByCarStr) : null;
        Double travelTimeByTransport = travelTimeByTransportStr != null ? Double.valueOf(travelTimeByTransportStr) : null;

        return new SiteRelationship(site1, site2, distance, travelTimeByCar, travelTimeByTransport);
    }

    public List<SiteRelationship> getAllRelationships() {
        Set<String> keys = redisTemplate.keys("distance:*:*");
        List<SiteRelationship> relationships = new ArrayList<>();

        if (keys != null) {
            for (String key : keys) {
                String[] parts = key.split(":");
                if (parts.length == 3) {
                    String site1 = parts[1];
                    String site2 = parts[2];
                    SiteRelationship relationship = getRelationship(site1, site2);
                    relationships.add(relationship);
                }
            }
        }

        return relationships;
    }

    public SiteRelationship getTotalTravelTimes(List<PlanEvent> planEvents) {
        double totalDistance = 0;
        double totalTravelTimeByCar = 0;
        double totalTravelTimeByTransport = 0;

        for (int i = 0; i < planEvents.size() - 1; i++) {
            String site1 = planEvents.get(i).getSite();
            String site2 = planEvents.get(i + 1).getSite();

            SiteRelationship relationship = getRelationship(site1, site2);

            if (relationship.getDistance() != null) {
                totalDistance += relationship.getDistance();
            }
            if (relationship.getTravelTimeByCar() != null) {
                totalTravelTimeByCar += relationship.getTravelTimeByCar();
            }
            if (relationship.getTravelTimeByTransport() != null) {
                totalTravelTimeByTransport += relationship.getTravelTimeByTransport();
            }
        }

        return new SiteRelationship(null, null, totalDistance, totalTravelTimeByCar, totalTravelTimeByTransport);
    }
}
