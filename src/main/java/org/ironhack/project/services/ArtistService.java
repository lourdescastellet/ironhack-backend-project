package org.ironhack.project.services;

import org.ironhack.project.dtos.ArtistDTO;
import org.ironhack.project.models.classes.Artist;
import org.ironhack.project.repositories.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository;

    public List<Artist> findAll() {
        return artistRepository.findAll();
    }

    public Optional<Artist> findById(Integer id) {
        return artistRepository.findById(id);
    }

    public Artist create(Artist artist) {
        return artistRepository.save(artist);
    }

    public Artist update(Integer userId, ArtistDTO artistDTO) {
        Artist existingArtist = artistRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Artist not found with this user id."));

       existingArtist.setUserName(artistDTO.getUserName());
       existingArtist.setName(artistDTO.getName());
       existingArtist.setEmail(artistDTO.getEmail());
       existingArtist.setPassword(artistDTO.getPassword());
       existingArtist.setArtistName(artistDTO.getArtistName());
       existingArtist.setGenre(artistDTO.getGenre());

        return artistRepository.save(existingArtist);
    }

    public void deleteById(Integer id) {
        artistRepository.deleteById(id);
    }
}
