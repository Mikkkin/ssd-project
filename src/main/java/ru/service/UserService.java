package ru.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import ru.DTO.UserDTO;
import ru.entity.Role;
import ru.entity.User;
import ru.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<UserDTO> getAll() {
        return userRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getById(Long id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User " + id + " not found"));
        return toDTO(u);
    }

    public UserDTO update(Long id, UserDTO dto) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User " + id + " not found"));
        
        if (dto.getUsername() != null && !dto.getUsername().equals(user.getUsername())
                && userRepository.existsByUsername(dto.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exist: " + dto.getUsername());
        }
        
        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())
                && userRepository.existsByUsername(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exist: " + dto.getEmail());
        }

        if (dto.getUsername() != null) {user.setUsername(dto.getUsername());}
        if (dto.getEmail() != null) {user.setEmail(dto.getEmail());}
        
        return toDTO(userRepository.save(user));
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    private UserDTO toDTO(User u) {
        return UserDTO.builder()
                .id(u.getId())
                .username(u.getUsername())
                .email(u.getEmail())
                .roles(u.getRoles().stream()
                        .map(Role::getName)
                        .map(Enum::name)
                        .collect(Collectors.toSet()))
                .build();
    }
}
