package com.example.springSecurity.controller;


import com.example.springSecurity.bean.entity.Student;
import com.example.springSecurity.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/students/")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PreAuthorize("hasAnyAuthority('student:read', 'student:write')")
    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAll();
    }

    @PreAuthorize("hasAuthority('student:read')")
    @GetMapping(path = "{studentId}")
    public Student getStudent(@PathVariable("studentId") Integer studentId) {
        return studentService.getStudent(studentId);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping(path = "create")
    public void create(@RequestBody Student student) {
        studentService.create(student);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping(path = "delete/{studentId}")
    public void delete(@PathVariable("studentId") Integer studentId) {
        studentService.delete(studentId);
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'TEACHER')")
    @PostMapping(path = "update")
    public void update(@RequestBody Student student,
                       @RequestParam(name = "studentId") Integer studentId) {
        studentService.update(studentId, student);
    }
}
