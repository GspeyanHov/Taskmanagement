package am.StarGroup.Taskmanagement.controller;

import am.StarGroup.Taskmanagement.entity.Role;
import am.StarGroup.Taskmanagement.entity.User;
import am.StarGroup.Taskmanagement.security.CurrentUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @GetMapping("/")
    public String mainPage(){
        return "index";
    }

    @GetMapping("/loginPage")
    public String loginPage(@RequestParam(value = "error",required = false) String error, ModelMap modelMap){
        if(error != null && error.equals("true")){
            modelMap.addAttribute("error","true");
        }
        return "loginPage";
    }
    @GetMapping("/accessDenied")
    public String accessDenied(){
        return "accessDenied";
    }
    @GetMapping("/loginSuccess")
    public String loginSuccess(@AuthenticationPrincipal CurrentUser currentUser){
        if(currentUser != null){
            User user = currentUser.getUser();
            if(user.getRole().equals(Role.MANAGER)){
                return "redirect:/manager";
            }else if(user.getRole().equals(Role.USER)){
                return "redirect:/user";
            }
        }
        return "redirect:/";
    }
}
