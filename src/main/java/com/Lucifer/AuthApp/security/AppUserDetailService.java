package com.Lucifer.AuthApp.security;

import com.Lucifer.AuthApp.model.Student;
import com.Lucifer.AuthApp.repo.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailService implements UserDetailsService {

    @Autowired
    private StudentRepo studentRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

       Student student= studentRepo.findByEmail(email);
       if(student == null){
           throw new UsernameNotFoundException("user not found");
       }
       return new StudentPrinciple(student);
    }
}
