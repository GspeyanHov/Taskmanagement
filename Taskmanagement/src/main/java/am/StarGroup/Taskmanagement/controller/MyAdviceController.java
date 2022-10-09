package am.StarGroup.Taskmanagement.controller;

import am.StarGroup.Taskmanagement.entity.User;
import am.StarGroup.Taskmanagement.security.CurrentUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class MyAdviceController {

    @ModelAttribute(name = "currentUser")
    public User currentUser(@AuthenticationPrincipal CurrentUser currentUser){
        if(currentUser != null){
            return currentUser.getUser();
        }
        return null;
    }

    @ModelAttribute(name = "currentUri")
    public String currentUri(HttpServletRequest request){
        return request.getRequestURI();
    }
}
