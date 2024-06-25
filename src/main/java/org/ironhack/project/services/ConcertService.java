package org.ironhack.project.services;

import org.ironhack.project.models.classes.Concert;
import org.ironhack.project.repositories.ConcertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConcertService {

    @Autowired
    private ConcertRepository concertRepository;

    public List<Concert> findAll() {
        return concertRepository.findAll();
    }

    public Optional<Concert> findById(Integer id) {
        return concertRepository.findById(id);
    }

    public Concert create(Concert concert) {
        return concertRepository.save(concert);
    }

    public void deleteById(Integer id) {
        concertRepository.deleteById(id);
    }

}
