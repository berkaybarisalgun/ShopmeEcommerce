package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController { //controller->service->repo

    @Autowired
    private UserService service;

    @GetMapping("/users")
    public String listAll(Model model){
        List<User> listUsers=service.listAll();
        model.addAttribute("listUsers",listUsers);//name of listUsers in thymleaf is listUsers!!!
        return "users";
    }

    @GetMapping("/users/new")
    public String newUser(Model model){
        List<Role> listRoles = service.listRoles();

        User  user=new User();
        user.setEnabled(true);//by default we set it true

        model.addAttribute("user",user);
        model.addAttribute("listRoles",listRoles);
        model.addAttribute("pageTitle","Create new User");
        return "user_form";
    }
    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes){
        System.out.println(user);
        service.save(user);
        redirectAttributes.addFlashAttribute("message","The user has been saved succesfully.");//in order to control the successful message
        return "redirect:/users";
    }
    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable(name="id") Integer id,
                           Model model,
                           RedirectAttributes redirectAttributes){
        try{
            User user=service.get(id);
            List<Role> listRoles=service.listRoles();
            model.addAttribute("user",user);
            model.addAttribute("pageTitle","Edit user(ID: "+id+")");
            model.addAttribute("listRoles",listRoles);


            return "user_form";
        } catch (UserNotFoundException ex){
            redirectAttributes.addFlashAttribute("message",ex.getMessage());
            return "redirect:/users";
        }
    }



}
