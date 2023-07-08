package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {


    public static final int USERS_PER_PAGE=4;


    @Autowired
    private UserRepository repo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> listAll(){
        return (List<User>) repo.findAll();
    }

    public Page<User> listByPage(int pageNum,String sortField, String sortDir){
        Sort sort=Sort.by(sortField);
        sort=sortDir.equals("asc")?sort.ascending():sort.descending();
        Pageable pageable= PageRequest.of(pageNum-1,USERS_PER_PAGE,sort);
        return repo.findAll(pageable);
    }

   public List<Role> listRoles(){
        return (List<Role>) roleRepo.findAll();
   }

   /*public void deneme(){
        User user=repo.findById();
        user.getEnabled().booleanValue()
    }
    */

    public User save(User user) {
        boolean isUpdatingUser=(user.getId()!=null);
        if(isUpdatingUser){
            User existingUser=repo.findById(user.getId()).get();
            if(user.getPassword().isEmpty()){
                user.setPassword(existingUser.getPassword());
            }
            else{
                encodePassword(user);
            }
        }else{
            encodePassword(user);
        }

       return repo.save(user);
    }



    private void encodePassword(User user){
        String encoded=passwordEncoder.encode(user.getPassword());
        user.setPassword(encoded);
    }

    public boolean isEmailunique(Integer id,String email){
        User userByEmail = repo.getUserByEmail(email);
        if(userByEmail==null) return true;
        boolean isCreatingNew=(id==null);
        if(isCreatingNew){
            if(userByEmail!=null) return false;
        }
        else{
            if(userByEmail.getId()!=id){
                return false;
            }
        }
        return true;

    }


    public User get(Integer id) throws UserNotFoundException {
        try{
            return repo.findById(id).get();
        }
        catch (NoSuchElementException ex){
            throw new UserNotFoundException("Couldn't find any user with Id "+id);
        }

    }

    public void delete(Integer id) throws UserNotFoundException {
        Long countById = repo.countById(id);
        if(countById==null ||countById==0){
            throw new UserNotFoundException("Couldn't find any user with Id:"+id);
        }
        repo.deleteById(id);
    }

    @Transactional
    public void updateUserEnabledStatus(Integer id,boolean enabled){
        repo.updateEnabledStatus(id,enabled);
    }

    public void updateUserEnabledStatu(Integer id,boolean enabled){
        User user=repo.findById(id).orElseThrow();
        user.setEmail("ilkerim@gmail.com");
        user.setEnabled(enabled);
    }
}
