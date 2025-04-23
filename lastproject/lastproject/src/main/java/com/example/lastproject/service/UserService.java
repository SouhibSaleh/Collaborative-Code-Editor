package com.example.lastproject.service;

import com.example.lastproject.model.User;
import com.example.lastproject.repo.UserRepo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepo userRepo;
    public UserService(UserRepo userRepo){
        this.userRepo = userRepo;
    }
    public User saveUser(User user) {
        try {
            return userRepo.save(user);
        }catch (Exception ex){

            return null;
        }

    }
    public User getUserByEmail(String email){
        Optional<User> user =  userRepo.findByEmail(email);
        if(user.isPresent())
        {
            return user.get();
        }else{
            return null;
        }
    }
    public static User getCurrentUser()
    {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

}
