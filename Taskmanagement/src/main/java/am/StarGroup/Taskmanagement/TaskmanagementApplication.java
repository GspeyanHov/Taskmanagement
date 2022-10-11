package am.StarGroup.Taskmanagement;

import am.StarGroup.Taskmanagement.entity.Role;
import am.StarGroup.Taskmanagement.entity.User;
import am.StarGroup.Taskmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
@EnableAsync
@SpringBootApplication
@RequiredArgsConstructor
public class TaskmanagementApplication implements CommandLineRunner {

    private final UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(TaskmanagementApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void run(String... args) throws Exception {
        Optional<User> byEmail = userRepository.findByEmail("admin@mail.com");
        if (byEmail.isEmpty()) {
            userRepository.save(User.builder()
                    .name("admin")
                    .surname("admin")
                    .email("admin@mail.com")
                    .role(Role.MANAGER)
                    .password(passwordEncoder().encode("admin"))
                    .build());
        }
    }
}

