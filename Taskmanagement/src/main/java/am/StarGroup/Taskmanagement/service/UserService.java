package am.StarGroup.Taskmanagement.service;

import am.StarGroup.Taskmanagement.entity.User;
import am.StarGroup.Taskmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Value("${task.management.images.folder}")
    private String folderPath;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void saveUser(User user, MultipartFile file) throws IOException {

        if (!file.isEmpty() && file.getSize() > 0) {
            String fileName = System.nanoTime() + "_" + file.getOriginalFilename();
            File newFile = new File(folderPath + File.separator + fileName);
            file.transferTo(newFile);
            user.setPicUrl(fileName);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        mailService.sendEmail(user.getEmail(), "Welcome","Dear" + user.getName() + "\n"
                + "you have successfully registered");
    }
    public byte[] getUserImage(String fileName) throws IOException {
        InputStream fileInputStream = new FileInputStream(folderPath + File.separator + fileName);
        return IOUtils.toByteArray(fileInputStream);
    }

    public void deleteById(int id) {
        userRepository.deleteById(id);
    }
}
