package am.StarGroup.Taskmanagement.controller;

import am.StarGroup.Taskmanagement.entity.User;
import am.StarGroup.Taskmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/user")
    public String userPage() {
        return "user";
    }

    @GetMapping("/users")
    public String users(ModelMap modelMap) {
        List<User> all = userService.findAllUsers();
        modelMap.addAttribute("users", all);
        return "users";
    }

    @GetMapping("/users/add")
    public String addUserPage() {
        return "addUser";
    }

    @PostMapping("/users/add")
    public String addUser(ModelMap modelMap, @ModelAttribute User user,
                          @RequestParam("userImage") MultipartFile file) throws IOException {
        Optional<User> byEmail = userService.findByEmail(user.getEmail());
        if (byEmail.isPresent()) {
            modelMap.addAttribute("errorMessageEmail", "User with that email already exists");
            return "addUser";
        }
        if (file.getContentType() != null && !file.getContentType().contains("image")) {
            modelMap.addAttribute("errorMessageFile", "please choose file's right format");
            return "addUser";
        }
        userService.saveUser(user, file);
        return "redirect:/users";
    }

    @GetMapping(value = "/users/getImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@RequestParam("fileName") String fileName) throws IOException {
        return userService.getUserImage(fileName);
    }

    @GetMapping("/users/delete")
    public String deleteUser(@RequestParam("id") int id) {
        userService.deleteById(id);
        return "redirect:/users";
    }
}
