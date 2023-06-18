package com.shopme.admin.user;

import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
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

    @GetMapping("/users/list/{pageNum}")
    public String listByPage(@PathVariable(name="pageNum") int pageNum,Model model){
        Page<User> page = service.listByPage(pageNum);
        List<User> listUsers = page.getContent();

        System.out.println("Pagenum= "+pageNum);
        System.out.println("Total elements= "+page.getTotalElements());
        System.out.println("Total pages= "+page.getTotalPages());

        model.addAttribute("listUsers",listUsers);
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
    public String saveUser(User user, RedirectAttributes redirectAttributes,
                           @RequestParam("image") MultipartFile multipartFile) throws IOException {

        if(!multipartFile.isEmpty()){
            String fileName= StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName);
            User savedUser=service.save(user);
            String uploadDir="user-photos/"+savedUser.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
        }
        else{
            if(user.getPhotos().isEmpty()) user.setPhotos(null);
            service.save(user);
        }


        //service.save(user);
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
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name="id") Integer id,
                           Model model,
                           RedirectAttributes redirectAttributes){
            try{
                service.delete(id);
                redirectAttributes.addFlashAttribute("message",
                        "The user ID:"+id+ " deleted succesfully ");

            }
            catch (UserNotFoundException ex){
                redirectAttributes.addFlashAttribute("message",ex.getMessage());
            }
        return "redirect:/users";

    }


    @GetMapping("/users/{id}/enabled/{status}")
    public  String updateUserEnabledStatus(@PathVariable("id") Integer id,
                                           @PathVariable("status") boolean enabled,RedirectAttributes redirectAttributes){
        service.updateUserEnabledStatus(id,enabled);

        String status=enabled ? "enabled":"disabled";
        String message="The user ID "+id+"has been "+status;
        redirectAttributes.addFlashAttribute("message",message);

    return "redirect:/users";
    }




}
