package org.ironhack.project.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ironhack.project.dtos.AdminCreationRequest;
import org.ironhack.project.dtos.ArtistCreationRequest;
import org.ironhack.project.dtos.CustomerCreationRequest;
import org.ironhack.project.dtos.VenueCreationRequest;
import org.ironhack.project.models.classes.Customer;
import org.ironhack.project.models.enums.Genre;
import org.ironhack.project.repositories.AdminRepository;
import org.ironhack.project.repositories.ArtistRepository;
import org.ironhack.project.repositories.CustomerRepository;
import org.ironhack.project.repositories.VenueRepository;
import org.ironhack.project.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class UserControllerIntegrationTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserService userService;

    private Customer customer;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        customer = new Customer();
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPassword("password");
        customer.setCustomerAddress("123 Main St");
        customer.setPaymentMethod("Visa");
        customer = customerRepository.save(customer);
    }

    @AfterEach
    public void tearDown() {
        customerRepository.deleteAll();
        adminRepository.deleteAll();
        artistRepository.deleteAll();
        venueRepository.deleteAll();
    }

    @Test
    void registerCustomer_Success() throws Exception {
        CustomerCreationRequest request = new CustomerCreationRequest();
        request.setName("Customer 1");
        request.setEmail("customer1@example.com");
        request.setPassword("password");
        request.setCustomerAddress("Address 1");
        request.setPaymentMethod("Visa");

        mockMvc.perform(post("/api/user/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void registerAdmin_Success() throws Exception {
        AdminCreationRequest request = new AdminCreationRequest();
        request.setName("Admin 1");
        request.setEmail("admin1@example.com");
        request.setPassword("password");

        mockMvc.perform(post("/api/user/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void registerArtist_Success() throws Exception {
        ArtistCreationRequest request = new ArtistCreationRequest();
        request.setName("Artist 1");
        request.setEmail("artist1@example.com");
        request.setPassword("password");
        request.setArtistName("ArtistName");
        request.setGenre(Genre.POP);

        mockMvc.perform(post("/api/user/artist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void registerVenue_Success() throws Exception {
        VenueCreationRequest request = new VenueCreationRequest();
        request.setName("Venue 1");
        request.setEmail("venue1@example.com");
        request.setPassword("password");
        request.setVenueName("VenueName");
        request.setVenueAddress("VenueAddress");
        request.setVenueCity("VenueCity");
        request.setVenueCapacity(100);

        mockMvc.perform(post("/api/user/venue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    public void deleteUser_UserExists_ReturnsNoContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/{userId}", customer.getUserId()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteUser_UserDoesNotExist_ReturnsNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/{userId}", 999))
                .andExpect(status().isNotFound());
    }


}
