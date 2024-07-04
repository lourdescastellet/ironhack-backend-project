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
import org.springframework.security.core.userdetails.UserDetailsService;
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

        // set the session creation policy to stateless, to not maintain sessions in the server
        http.sessionManagement().sessionCreationPolicy(STATELESS);

        // set up authorization for different request matchers and user roles
        http.authorizeHttpRequests((requests) -> requests
                        // Public access to Concert endpoints
                        .requestMatchers(HttpMethod.GET, "/api/concerts/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/register/**").permitAll()

                        // Admin endpoints restricted to ADMIN role
                        .requestMatchers(HttpMethod.POST, "/api/admin/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/admin/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/admin/**").hasAuthority("ADMIN")

                        // Venue endpoints restricted to VENUE role
                        .requestMatchers(HttpMethod.POST, "/api/venue/**").hasAuthority("VENUE")
                        .requestMatchers(HttpMethod.PUT, "/api/venue/**").hasAuthority("VENUE")
                        .requestMatchers(HttpMethod.DELETE, "/api/venue/**").hasAuthority("VENUE")

                        // Artist endpoints restricted to ARTIST role
                        .requestMatchers(HttpMethod.POST, "/api/artist/**").hasAuthority("ARTIST")
                        .requestMatchers(HttpMethod.PUT, "/api/artist/**").hasAuthority("ARTIST")
                        .requestMatchers(HttpMethod.DELETE, "/api/artist/**").hasAuthority("ARTIST")

                        // Customer endpoints restricted to CUSTOMER role
                        .requestMatchers(HttpMethod.POST, "/api/customer/**").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.PUT, "/api/customer/**").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.DELETE, "/api/customer/**").hasAuthority("CUSTOMER")

                        // Booking endpoints
                        .requestMatchers(HttpMethod.GET, "/api/booking/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/booking/**").hasAuthority("CUSTOMER")
                        // .requestMatchers(HttpMethod.PUT, "/api/booking/**").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.DELETE, "/api/booking/**").hasAuthority("CUSTOMER")
                        .anyRequest().authenticated()); // All other requests require authentication

        // add the custom authentication filter to the http security object
        http.addFilter(customAuthenticationFilter);

        // Add the custom authorization filter before the standard authentication filter.
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        // Build the security filter chain to be returned.
        return http.build();

    }
}

