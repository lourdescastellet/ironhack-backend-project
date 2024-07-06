package org.ironhack.project;

import org.ironhack.project.models.classes.*;
import org.ironhack.project.models.enums.Genre;
import org.ironhack.project.repositories.AdminRepository;
import org.ironhack.project.repositories.ArtistRepository;
import org.ironhack.project.repositories.ConcertRepository;
import org.ironhack.project.repositories.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class IronhackBackendProjectApplication {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ConcertRepository concertRepository;

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(IronhackBackendProjectApplication.class, args);
        IronhackBackendProjectApplication app = context.getBean(IronhackBackendProjectApplication.class);
        app.initializeData();
    }

    private void initializeData() {
        createAdmins();
        createVenues();
        createArtists();
        createConcerts();
    }

    private void createAdmins() {
        Admin admin1 = new Admin();
        admin1.setName("Admin A");
        admin1.setEmail("admina@ironhack.com");
        admin1.setPassword("password");
        adminRepository.save(admin1);
    }

    private void createVenues() {
        Venue venue1 = new Venue();
        venue1.setName("Venue A");
        venue1.setEmail("venuea@ironhack.com");
        venue1.setPassword("password");
        venue1.setVenueName("Venue X");
        venue1.setVenueAddress("Address X");
        venue1.setVenueCity("City X");
        venue1.setVenueCapacity(500);
        venueRepository.save(venue1);

        Venue venue2 = new Venue();
        venue2.setName("Venue B");
        venue2.setEmail("venueb@ironhack.com");
        venue2.setPassword("password");
        venue2.setVenueName("Venue Y");
        venue2.setVenueAddress("Address Y");
        venue2.setVenueCity("City Y");
        venue2.setVenueCapacity(1500);
        venueRepository.save(venue2);

        Venue venue3 = new Venue();
        venue3.setName("Venue C");
        venue3.setEmail("venuec@ironhack.com");
        venue3.setPassword("password");
        venue3.setVenueName("Venue Z");
        venue3.setVenueAddress("Address Z");
        venue3.setVenueCity("City Z");
        venue3.setVenueCapacity(50);
        venueRepository.save(venue3);
    }

    private void createArtists() {
        Artist artist1 = new Artist();
        artist1.setName("Artist A");
        artist1.setEmail("artistA@example.com");
        artist1.setPassword("password");
        artist1.setArtistName("Artist A");
        artist1.setGenre(Genre.ROCK);
        artistRepository.save(artist1);

        Artist artist2 = new Artist();
        artist2.setName("Artist B");
        artist2.setEmail("artistB@example.com");
        artist2.setPassword("password");
        artist2.setArtistName("Artist B");
        artist2.setGenre(Genre.POP);
        artistRepository.save(artist2);

        Artist artist3 = new Artist();
        artist3.setName("Artist C");
        artist3.setEmail("artistC@example.com");
        artist3.setPassword("password");
        artist3.setArtistName("Artist C");
        artist3.setGenre(Genre.JAZZ);
        artistRepository.save(artist3);
    }


    private void createConcerts() {
        Concert concert1 = new Concert();
        concert1.setConcertName("Concert 1");
        concert1.setArtist(artistRepository.findById(5).orElseThrow());
        concert1.setVenue(venueRepository.findById(2).orElseThrow());
        concertRepository.save(concert1);

        Concert concert2 = new Concert();
        concert2.setConcertName("Concert 2");
        concert2.setArtist(artistRepository.findById(6).orElseThrow());
        concert2.setVenue(venueRepository.findById(3).orElseThrow());
        concertRepository.save(concert2);

        Concert concert3 = new Concert();
        concert3.setConcertName("Concert 3");
        concert3.setArtist(artistRepository.findById(7).orElseThrow());
        concert3.setVenue(venueRepository.findById(4).orElseThrow());
        concertRepository.save(concert3);
    }
}