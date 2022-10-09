package am.StarGroup.Taskmanagement.service;

import am.StarGroup.Taskmanagement.entity.Role;
import am.StarGroup.Taskmanagement.entity.Task;
import am.StarGroup.Taskmanagement.entity.User;
import am.StarGroup.Taskmanagement.repository.TaskRepository;
import am.StarGroup.Taskmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;


    public void saveTask(Task task) {
        if (task.getUser() != null && task.getUser().getId() == 0) {
            task.setUser(null);
        }
        taskRepository.save(task);
    }

    public Page<Task> findTasksByUserRole(User user, Pageable pageable) {
        return user.getRole() == Role.USER ?
                taskRepository.findAllByUser_Id(user.getId(), pageable)
                : taskRepository.findAll(pageable);
    }

    public void changeTaskUser(int userId, int taskId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (userOptional.isPresent() && taskOptional.isPresent()) {
            User user = userOptional.get();
            Task task = taskOptional.get();
            if (task.getUser() != user) {
                task.setUser(user);
                taskRepository.save(task);
            }
        } else if (taskOptional.isPresent() && userId == 0) {
            taskOptional.get().setUser(null);
            taskRepository.save(taskOptional.get());
        }
    }
}
