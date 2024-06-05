package com.olympics.sport.service;

import com.olympics.sport.model.Sport;
import com.olympics.sport.repository.SportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SportService {

    @Autowired
    private SportRepository sportRepository;

    public List<Sport> getAllSports() {
        return sportRepository.findAll();
    }

    public Optional<Sport> getSportById(Long id) {
        return sportRepository.findById(id);
    }

    public Optional<Sport> getSportByName(String name) {
        return sportRepository.findByNom(name);
    }

    public Sport addSport(Sport sport) {
        return sportRepository.save(sport);
    }

    public Sport updateSport(Long id, Sport updatedSport) {
        if (sportRepository.existsById(id)) {
            updatedSport.setId(id);
            return sportRepository.save(updatedSport);
        }
        return null;
    }

    public void deleteSport(Long id) {
        sportRepository.deleteById(id);
    }
}
