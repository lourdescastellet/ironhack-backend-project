package org.ironhack.project.services;

import jakarta.validation.Valid;
import org.ironhack.project.dtos.ArtistRequest;
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

    public Artist create(@Valid ArtistRequest artistRequest) {
        Artist artist = new Artist();
        artist.setName(artistRequest.getName());
        artist.setEmail(artistRequest.getEmail());
        artist.setPassword(artistRequest.getPassword());
        artist.setGenre(artistRequest.getGenre());

        return artistRepository.save(artist);
    }

    public Artist update(Integer userId, ArtistRequest artistRequest) {
        Artist existingArtist = artistRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Artist not found with this user id."));

       existingArtist.setName(artistRequest.getName());
       existingArtist.setEmail(artistRequest.getEmail());
       existingArtist.setPassword(artistRequest.getPassword());
       existingArtist.setArtistName(artistRequest.getArtistName());
       existingArtist.setGenre(artistRequest.getGenre());

        return artistRepository.save(existingArtist);
    }

    public void deleteById(Integer id) {
        artistRepository.deleteById(id);
    }
}
