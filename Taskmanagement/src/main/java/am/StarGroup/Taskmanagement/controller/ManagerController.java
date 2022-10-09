package am.StarGroup.Taskmanagement.controller;

import am.StarGroup.Taskmanagement.entity.Role;
import am.StarGroup.Taskmanagement.entity.User;
import am.StarGroup.Taskmanagement.security.CurrentUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ManagerController {

    @GetMapping("/manager")
    public String managerPage(){
        return "manager";
    }
}
