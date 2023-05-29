package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {
    @Autowired
    private UserRepository repo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> listAll(){
        return (List<User>) repo.findAll();
    }
   public List<Role> listRoles(){
        return (List<Role>) roleRepo.findAll();
   }


    public void save(User user) {
        encodePassword(user);
        repo.save(user);
    }

    private void encodePassword(User user){
        String encoded=passwordEncoder.encode(user.getPassword());
        user.setPassword(encoded);
    }

    public boolean isEmailunique(String email){
        User userByEmail = repo.getUserByEmail(email);
        return userByEmail==null;

    }


    public User get(Integer id) throws UserNotFoundException {
        try{
            return repo.findById(id).get();
        }
        catch (NoSuchElementException ex){
            throw new UserNotFoundException("Couldn't find any user with Id "+id);
        }

    }
}
