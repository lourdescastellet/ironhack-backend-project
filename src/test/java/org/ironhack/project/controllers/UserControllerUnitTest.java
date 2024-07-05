package org.ironhack.project.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.ironhack.project.dtos.AdminCreationRequest;
import org.ironhack.project.dtos.ArtistCreationRequest;
import org.ironhack.project.dtos.CustomerCreationRequest;
import org.ironhack.project.dtos.VenueCreationRequest;
import org.ironhack.project.models.classes.Admin;
import org.ironhack.project.models.classes.Artist;
import org.ironhack.project.models.classes.Customer;
import org.ironhack.project.models.classes.Venue;
import org.ironhack.project.models.enums.Genre;
import org.ironhack.project.repositories.CustomerRepository;
import org.ironhack.project.repositories.AdminRepository;
import org.ironhack.project.repositories.ArtistRepository;
import org.ironhack.project.repositories.VenueRepository;

import org.ironhack.project.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserControllerUnitTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private VenueRepository venueRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void registerCustomer_Success() {

        CustomerCreationRequest request = new CustomerCreationRequest();
        request.setName("Customer 1");
        request.setEmail("customer1@ironhack.com");
        request.setPassword("password");
        request.setCustomerAddress("Address 1");
        request.setPaymentMethod("Credit Card");

        Customer savedCustomer = new Customer();
        savedCustomer.setUserId(1);
        savedCustomer.setName(request.getName());
        savedCustomer.setEmail(request.getEmail());
        savedCustomer.setPassword(request.getPassword());
        savedCustomer.setCustomerAddress(request.getCustomerAddress());
        savedCustomer.setPaymentMethod(request.getPaymentMethod());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashedPassword");
        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        ResponseEntity<?> responseEntity = userController.registerCustomer(request);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(savedCustomer, responseEntity.getBody());
    }

    @Test
    void registerCustomer_Failure_InternalServerError() {
        CustomerCreationRequest request = new CustomerCreationRequest();
        request.setName("Customer 1");
        request.setEmail("customer1@example.com");
        request.setPassword("password");
        request.setCustomerAddress("Address 1");
        request.setPaymentMethod("Credit Card");

        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashedPassword");
        when(customerRepository.save(any(Customer.class))).thenThrow(new RuntimeException("Database connection failed"));

        userController.registerCustomer(request);
    }

    @Test
    void registerAdmin_Success() {
        AdminCreationRequest request = new AdminCreationRequest();
        request.setName("Admin 1");
        request.setEmail("admin1@example.com");
        request.setPassword("password");

        Admin savedAdmin = new Admin();
        savedAdmin.setUserId(1);
        savedAdmin.setName(request.getName());
        savedAdmin.setEmail(request.getEmail());
        savedAdmin.setPassword(request.getPassword());

        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashedPassword");
        when(adminRepository.save(any(Admin.class))).thenReturn(savedAdmin);

        ResponseEntity<?> responseEntity = userController.registerAdmin(request);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(savedAdmin, responseEntity.getBody());
    }

    @Test
    void registerArtist_Success() {
        ArtistCreationRequest request = new ArtistCreationRequest();
        request.setName("Artist 1");
        request.setEmail("artist1@example.com");
        request.setPassword("password");
        request.setArtistName("Artist Name");
        request.setGenre(Genre.ROCK);

        Artist savedArtist = new Artist();
        savedArtist.setUserId(1);
        savedArtist.setName(request.getName());
        savedArtist.setEmail(request.getEmail());
        savedArtist.setPassword(request.getPassword());
        savedArtist.setArtistName(request.getArtistName());
        savedArtist.setGenre(request.getGenre());

        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashedPassword");
        when(artistRepository.save(any(Artist.class))).thenReturn(savedArtist);

        ResponseEntity<?> responseEntity = userController.registerArtist(request);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(savedArtist, responseEntity.getBody());
    }

    @Test
    void registerVenue_Success() {
        VenueCreationRequest request = new VenueCreationRequest();
        request.setName("Venue 1");
        request.setEmail("venue1@example.com");
        request.setPassword("password");
        request.setVenueName("Venue Name");
        request.setVenueAddress("Address 1");
        request.setVenueCity("City");
        request.setVenueCapacity(100);

        Venue savedVenue = new Venue();
        savedVenue.setUserId(1);
        savedVenue.setName(request.getName());
        savedVenue.setEmail(request.getEmail());
        savedVenue.setPassword(request.getPassword());
        savedVenue.setVenueName(request.getVenueName());
        savedVenue.setVenueAddress(request.getVenueAddress());
        savedVenue.setVenueCity(request.getVenueCity());
        savedVenue.setVenueCapacity(request.getVenueCapacity());

        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashedPassword");
        when(venueRepository.save(any(Venue.class))).thenReturn(savedVenue);

        ResponseEntity<?> responseEntity = userController.registerVenue(request);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(savedVenue, responseEntity.getBody());
    }

    @Test
    void deleteUser_UserFound_NoContent() {
        Integer userId = 1;

        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted successfully");

        doNothing().when(userService).deleteUserById(userId);

        ResponseEntity<String> responseEntity = userController.deleteUser(userId, null);

        assertEquals(expectedResponse.getStatusCode(), responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }

    @Test
    void deleteUser_UserNotFound_NotFound() {
        Integer userId = 999;
        EntityNotFoundException exception = new EntityNotFoundException("User not found with id: " + userId);

        doThrow(exception).when(userService).deleteUserById(userId);

        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + exception.getMessage());

        ResponseEntity<String> responseEntity = userController.deleteUser(userId, null);

        assertEquals(expectedResponse.getStatusCode(), responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }

    @Test
    void deleteUser_AccessDenied_Forbidden() {
        Integer userId = 1;
        AccessDeniedException exception = new AccessDeniedException("Access denied");

        doThrow(exception).when(userService).deleteUserById(userId);

        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied: " + exception.getMessage());

        ResponseEntity<String> responseEntity = userController.deleteUser(userId, null);

        assertEquals(expectedResponse.getStatusCode(), responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }

    @Test
    void deleteUser_InternalServerError() {
        Integer userId = 1;
        RuntimeException exception = new RuntimeException("Internal server error");

        doThrow(exception).when(userService).deleteUserById(userId);

        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete user: " + exception.getMessage());

        ResponseEntity<String> responseEntity = userController.deleteUser(userId, null);

        assertEquals(expectedResponse.getStatusCode(), responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }
}

