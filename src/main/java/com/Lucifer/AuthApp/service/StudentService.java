package com.Lucifer.AuthApp.service;

import com.Lucifer.AuthApp.dtos.StdReqDto;
import com.Lucifer.AuthApp.dtos.StdResDto;
import com.Lucifer.AuthApp.model.Student;
import com.Lucifer.AuthApp.repo.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;


@Service
public class StudentService {

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    //get all students profile
    public List<StdResDto> getAllStudents() {
       return   studentRepo.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    //get profile
    public StdResDto getProfile(String email) {
       Student student= studentRepo.findByEmail(email);
        if (student == null) {
            throw new RuntimeException("Student with email  not found");
        }
        return  mapToResponse(student);
    }

    //saved Student
    public StdResDto savedStudent(StdReqDto dto){

        Student existesStudent=studentRepo.findByEmail(dto.email());
        if(existesStudent !=null){
            throw new RuntimeException("email already exist");}

        Student student = new Student();
        student.setName(dto.name());
        student.setEmail(dto.email());
        student.setUserId(UUID.randomUUID().toString());
        student.setPassword(passwordEncoder.encode(dto.password()));
        student.setResetOtpExpiredAt(0L);
        student.setResetOtp(null);

        Student student1=studentRepo.save(student);
        return mapToResponse(student1);
    }

    //Helper method
    StdResDto mapToResponse(Student student ){
        return new StdResDto(
                student.getName(),
                student.getUserId(),
                student.getEmail()
        );
    }
}
