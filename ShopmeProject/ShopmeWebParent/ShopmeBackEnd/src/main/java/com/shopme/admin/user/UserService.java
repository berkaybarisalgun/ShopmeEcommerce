package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository repo;

    @Autowired
    private RoleRepository roleRepo;

    public List<User> listAll(){
        return (List<User>) repo.findAll();
    }
   public List<Role> listRoles(){
        return (List<Role>) roleRepo.findAll();
   }


    public void save(User user) {
        repo.save(user);
    }
}
