package com.example.authservice.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.authservice.dao.entity.UserInfo;
import com.example.authservice.dao.repository.UserInfoRepository;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserInfoRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Check if admin user already exists
            if (userRepository.findByEmail("admin@example.com").isEmpty()) {
                UserInfo admin = new UserInfo();
                admin.setName("System Administrator");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRoles("ROLE_ADMIN,ROLE_USER");

                userRepository.save(admin);
                log.info("✅ Default admin user created successfully!");
                log.info("📧 Email: admin@example.com");
                log.info("🔑 Password: admin123");
                log.info("⚠️  IMPORTANT: Please change the default password after first login!");
            } else {
                log.info("ℹ️  Admin user already exists, skipping initialization.");
            }
        };
    }
}
