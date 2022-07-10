package com.example.demo.services.database;

import com.example.demo.entities.UserEntity;
import com.example.demo.repositories.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class DatabaseUserDetailsService implements UserDetailsService {

    private final @NonNull UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new DatabaseUserDetails(user.getUsername(), user.getPassword(), user.getRole().getRole());
    }
}
