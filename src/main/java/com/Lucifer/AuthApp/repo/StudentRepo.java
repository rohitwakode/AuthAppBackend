package com.Lucifer.AuthApp.repo;

import com.Lucifer.AuthApp.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepo extends JpaRepository<Student, Integer> {

    Student findByEmail(String email);
    Student findByResetToken(String resetToken);
}