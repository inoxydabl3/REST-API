package com.example.demo.services.user;

import com.example.demo.dtos.UserCreationDTO;
import com.example.demo.dtos.UserDTO;
import com.example.demo.entities.UserEntity;
import com.example.demo.mappers.UserMapper;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.utils.Role;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final @NonNull PasswordEncoder passwordEncoder;

    private final @NonNull UserRepository userRepository;
    private final @NonNull RoleRepository roleRepository;

    private final @NonNull UserMapper mapper;

    @Override
    public Optional<UserDTO> getUser(int userId) {
        return userRepository.findById(userId).map(mapper::toDto);
    }

    @Override
    public UserDTO createUser(UserCreationDTO user) {
        UserEntity entity = mapper.toEntity(user.toBuilder().password(passwordEncoder.encode(user.getPassword())).build(),
                roleRepository.findByRole(user.getRole().name()));
        return mapper.toDto(userRepository.save(entity));
    }

    @Override
    public Optional<UserDTO> updateUser(int userId, UserCreationDTO user) {
        Optional<UserEntity> entityToUpdate = userRepository.findById(userId);
        if (entityToUpdate.isPresent()) {
            if (user.getPassword() != null) {
                user = user.toBuilder().password(passwordEncoder.encode(user.getPassword())).build();
            }
            entityToUpdate = Optional.of(userRepository.save(mapper.updateUser(entityToUpdate.get(), user,
                    roleRepository.findByRole(user.getRole().name()))));
        }
        return entityToUpdate.map(mapper::toDto);
    }

    @Override
    public Optional<UserDTO> deleteUser(int userId) {
        Optional<UserEntity> entityToDelete = userRepository.findById(userId);
        if (entityToDelete.isPresent()) {
            userRepository.deleteById(userId);
        }
        return entityToDelete.map(mapper::toDto);
    }

    @Override
    public Optional<UserDTO> changeStats(int userId, Role role) {
        Optional<UserEntity> entity = userRepository.findById(userId);
        if (entity.isPresent() && !role.name().equals(entity.get().getRole().getRole())) {
            UserEntity e = entity.get();
            e.setRole(roleRepository.findByRole(role.name()));
            entity = Optional.of(userRepository.save(e));
        }
        return entity.map(mapper::toDto);
    }

}
