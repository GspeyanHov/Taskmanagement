package am.StarGroup.Taskmanagement.controller;

import am.StarGroup.Taskmanagement.entity.Role;
import am.StarGroup.Taskmanagement.entity.Task;
import am.StarGroup.Taskmanagement.entity.User;
import am.StarGroup.Taskmanagement.repository.TaskRepository;
import am.StarGroup.Taskmanagement.repository.UserRepository;
import am.StarGroup.Taskmanagement.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/tasks")
    public String tasks(ModelMap modelMap, @AuthenticationPrincipal CurrentUser currentUser) {
        Role role = currentUser.getUser().getRole();
        List<Task> all = role == Role.USER
                ? taskRepository.findAllByUser_Id(currentUser.getUser().getId())
                : taskRepository.findAll();
        List<User> users = userRepository.findAll();
        modelMap.addAttribute("tasks", all);
        modelMap.addAttribute("users",users);
        return "tasks";
    }
    @GetMapping("/tasks/add")
    public String addTaskPage(ModelMap modelMap){
        List<User> all = userRepository.findAll();
        modelMap.addAttribute("users",all);
        return "addTask";
    }

    @PostMapping("/tasks/add")
    public String addTask(@ModelAttribute Task task){
        if(task.getUser() != null && task.getUser().getId() == 0){
            task.setUser(null);
        }
        taskRepository.save(task);
        return "redirect:/tasks";
    }
    @PostMapping("/tasks/changeUser")
    public String changeUser(@RequestParam("userId") int userId,@RequestParam("taskId") int taskId){
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if(userOptional.isPresent() && taskOptional.isPresent()){
            User user = userOptional.get();
            Task task = taskOptional.get();
            if(task.getUser() != user){
                task.setUser(user);
                taskRepository.save(task);
            }
        }
        return "redirect:/tasks";
    }
}
