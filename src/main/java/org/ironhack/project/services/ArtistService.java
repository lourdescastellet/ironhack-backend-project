package org.ironhack.project.services;

import jakarta.validation.Valid;
import org.ironhack.project.dtos.ArtistDTO;
import org.ironhack.project.dtos.ArtistUpdateRequest;
import org.ironhack.project.models.classes.Artist;
import org.ironhack.project.repositories.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public List<ArtistDTO> findAll() {
        return artistRepository.findAll().stream()
                .map(artist -> {
            ArtistDTO artistDTO = new ArtistDTO();
            artistDTO.setArtistName(artist.getArtistName());
            artistDTO.setGenre(artist.getGenre());
            return artistDTO;
        }).collect(Collectors.toList());
    }

    public Optional<ArtistDTO> findById(Integer id) {
        return artistRepository.findById(id).map(artist -> {
            ArtistDTO artistDTO = new ArtistDTO();
            artistDTO.setArtistName(artist.getArtistName());
            artistDTO.setGenre(artist.getGenre());
            return artistDTO;
        });
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
                artist.setPassword(passwordEncoder.encode(artistUpdateRequest.getPassword()));
            }
            if (artistUpdateRequest.getArtistName() != null) {
                artist.setArtistName(artistUpdateRequest.getArtistName());
            }
            if (artistUpdateRequest.getGenre() != null) {
                artist.setGenre(artistUpdateRequest.getGenre());
            }

            Artist updatedArtist = artistRepository.save(artist);
            System.out.println("updated artist: " + updatedArtist);
            return updatedArtist;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist not found with this User Id.");
        }
    }

    public void deleteById(Integer userId) {
        Optional<Artist> optionalArtist = artistRepository.findById(userId);
        optionalArtist.ifPresentOrElse(
                artist -> artistRepository.delete(artist),
                () -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist not found with this User Id.");
                }
        );
    }
}
