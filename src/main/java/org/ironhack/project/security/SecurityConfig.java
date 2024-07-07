package org.ironhack.project.security;

import lombok.RequiredArgsConstructor;
import org.ironhack.project.security.filters.CustomAuthenticationFilter;
import org.ironhack.project.security.filters.CustomAuthorizationFilter;
import org.ironhack.project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    private UserService userDetailsService;

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBuilder.getOrBuild());

        // set the URL that the filter should process
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");

        // disable CSRF protection because we are using tokens, not session
        http.csrf().disable();

        // set the session creation policy too stateless to not maintain sessions in the server
        http.sessionManagement().sessionCreationPolicy(STATELESS);

        // set up authorization for different request matchers and user roles
        http.authorizeHttpRequests((requests) -> requests

                // Public endpoints
                .requestMatchers(HttpMethod.GET, "/api/concerts/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/register/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/artist/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/venue/**").permitAll()

                // Admin
                .requestMatchers(HttpMethod.GET, "/api/admin/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/admin/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/admin/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/user/**").hasAuthority("ADMIN")

                // Venue
                .requestMatchers(HttpMethod.PUT, "/api/venue/**").hasAnyAuthority("VENUE", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/venue/**").hasAnyAuthority("VENUE","ADMIN")

                // Artist
                .requestMatchers(HttpMethod.PUT, "/api/artist/**").hasAnyAuthority("ARTIST","ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/artist/**").hasAnyAuthority("ARTIST","ADMIN")

                // Customer
                .requestMatchers(HttpMethod.PUT, "/api/customer/**").hasAnyAuthority("CUSTOMER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/customer/**").hasAnyAuthority("CUSTOMER", "ADMIN")

                // Concert
                .requestMatchers(HttpMethod.PUT, "/api/concerts/**").hasAnyAuthority("ADMIN", "ARTIST","VENUE")

                .anyRequest().hasAuthority("ADMIN"));

        // add the custom authentication filter to the http security object
        http.addFilter(customAuthenticationFilter);

        // Add the custom authorization filter before the standard authentication filter.
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        // Build the security filter chain to be returned.
        return http.build();

    }
}

