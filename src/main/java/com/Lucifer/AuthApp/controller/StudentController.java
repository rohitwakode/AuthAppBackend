package com.Lucifer.AuthApp.controller;

import com.Lucifer.AuthApp.dtos.StdReqDto;
import com.Lucifer.AuthApp.dtos.StdResDto;
import com.Lucifer.AuthApp.service.EmailService;
import com.Lucifer.AuthApp.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/std")
public class StudentController {

    @Autowired
    private StudentService studentService;


    @GetMapping
    private List<StdResDto> getAllStudents(){
        return studentService.getAllStudents();
    }

    @GetMapping("/profile")
    private StdResDto getProfile(Authentication auth){
        String email = auth.getName(); ;
        return studentService.getProfile(email);
    }


}
