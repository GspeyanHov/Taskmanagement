package am.StarGroup.Taskmanagement.controller;
import am.StarGroup.Taskmanagement.entity.User;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import am.StarGroup.Taskmanagement.repository.UserRepository;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${task.management.images.folder}")
    private String folderPath;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user")
    public String userPage(){
        return "user";
    }
    @GetMapping("/users")
    public String users(ModelMap modelMap) {
        List<User> all = userRepository.findAll();
        modelMap.addAttribute("users", all);
        return "users";
    }
    @GetMapping("/users/add")
    public String addUserPage(){
        return "addUser";
    }
    @PostMapping("/users/add")
    public String addUser(ModelMap modelMap, @ModelAttribute User user,
                          @RequestParam("userImage")MultipartFile file) throws IOException {
        Optional<User> byEmail = userRepository.findByEmail(user.getEmail());
        if(byEmail.isPresent()){
            modelMap.addAttribute("errorMessageEmail","User with that email already exists");
            return "addUser";
        }
        if(!file.isEmpty() && file.getSize() > 0){
            if(file.getContentType() != null && !file.getContentType().contains("image")){
                modelMap.addAttribute("errorMessageFile","please choose file's right format");
                return "addUser";
            }
            String fileName =  System.nanoTime() + "_" + file.getOriginalFilename();
            File newFile = new File(folderPath + File.separator + fileName);
            file.transferTo(newFile);
            user.setPicUrl(fileName);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return "redirect:/users";
    }

    @GetMapping(value = "/users/getImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@RequestParam("fileName") String fileName) throws IOException {
        InputStream fileInputStream = new FileInputStream(folderPath + File.separator + fileName);
        return IOUtils.toByteArray(fileInputStream);
    }
    @GetMapping("/users/delete")
    public String deleteUser(@RequestParam("id") int id){
        userRepository.deleteById(id);
        return "redirect:/users";
    }
}
