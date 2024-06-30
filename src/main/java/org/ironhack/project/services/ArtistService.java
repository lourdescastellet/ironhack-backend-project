package org.ironhack.project.services;

import jakarta.validation.Valid;
import org.ironhack.project.dtos.ArtistRequest;
import org.ironhack.project.dtos.ArtistUpdateRequest;
import org.ironhack.project.models.classes.Artist;
import org.ironhack.project.repositories.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public Artist update(Integer userId, @Valid ArtistUpdateRequest artistUpdateRequest) {

        Optional<Artist> optionalArtist = artistRepository.findById(userId);

        if (optionalArtist.isPresent()) {
            Artist artist = optionalArtist.get();

            if (artistUpdateRequest.getName() != null) {
                artist.setName(artistUpdateRequest.getName());
            }
            if (artistUpdateRequest.getEmail() != null) {
                artist.setEmail(artistUpdateRequest.getEmail());
            }
            if (artistUpdateRequest.getPassword() != null) {
                artist.setPassword(artistUpdateRequest.getPassword());
            }
            if (artistUpdateRequest.getArtistName() != null) {
                artist.setArtistName(artistUpdateRequest.getArtistName());
            }
            if (artistUpdateRequest.getGenre() != null) {
                artist.setGenre(artistUpdateRequest.getGenre());
            }

            artistRepository.save(artist);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist not found with this User Id.");
        }
        return null;
    }

    public void deleteById(Integer id) {
        artistRepository.deleteById(id);
    }
}
