package com.shopme.admin.user;

import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User,Integer> {
    @Query("select u FROM User u Where u.email=:email")
    public User getUserByEmail(@Param("email") String email); //This section little bit complicated I gotta make sure that I fully Understand this part.

    public Long countById(Integer id);
}
