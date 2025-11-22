package ru.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;
import ru.entity.Role;
import ru.entity.Role.RoleType;
import ru.entity.User;
import ru.repository.RoleRepository;
import ru.repository.UserRepository;


@Configuration
@RequiredArgsConstructor
public class RoleDB {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.name}")
    private String adminUsernameProp;

    @Value("${admin_mail}")
    private String adminEmail;

    @Value("${password_main}")
    private String adminPassword;

    @Bean
    public ApplicationRunner initializeRoles() {
        return args -> {
            for (RoleType roleType : RoleType.values()) {
                if (roleRepository.findByName(roleType).isEmpty()) {
                    Role role = new Role();
                    role.setName(roleType);
                    role.setDescription(roleType.getDescription());
                    roleRepository.save(role);
                }
            }


            String adminUsername = adminUsernameProp;
            if (userRepository.findByUsername(adminUsername).isEmpty()) {
                Role adminRole = roleRepository.findByName(RoleType.role_admin)
                    .orElseThrow(() -> new RuntimeException("Role admin not found"));

                User admin = new User();
                admin.setUsername(adminUsername);
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode(adminPassword));
                Set<Role> roles = new HashSet<>();
                roles.add(adminRole);
                admin.setRoles(roles);
                userRepository.save(admin);
            }
        };
    }
}
