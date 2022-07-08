package com.example.demo.services;

import com.example.demo.dtos.UserDTO;
import com.example.demo.entities.UserEntity;
import com.example.demo.mappers.UserMapper;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserSerivce {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final UserMapper mapper;

    @Override
    public Optional<UserDTO> getUser(int userId) {
        return userRepository.findById(userId).map(mapper::toDto);
    }

    @Override
    public UserDTO createUser(UserDTO dto) {
        UserEntity entity = new UserEntity();
        entity.setUsername(dto.getUsername());
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity.setRole(roleRepository.findByRole(dto.getRole().name()));
        return mapper.toDto(userRepository.save(entity));
    }
}
