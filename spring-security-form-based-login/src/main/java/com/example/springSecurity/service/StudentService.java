package com.example.springSecurity.service;

import com.example.springSecurity.bean.entity.Student;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class StudentService {

    private static final List<Student> students = new ArrayList<>();

    static {
        students.add(Student.builder().studentId(1).name("James Bond").build());
        students.add(Student.builder().studentId(2).name("George Bonds").build());
        students.add(Student.builder().studentId(3).name("Maria Jones").build());
        students.add(Student.builder().studentId(4).name("Anna Smith").build());
    }

    public List<Student> getAll() {
        return students;
    }

    public Student getStudent(Integer studentId) {
        return students.stream()
                .filter(student -> student.getStudentId().equals(studentId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("Student %s doesn't exist", studentId)));
    }

    public void create(Student student) {
        students.add(student);
    }

    public void update(Integer studentId, Student student) {
        students.stream()
                .filter(existStudent -> existStudent.getStudentId().equals(studentId))
                .findFirst()
                .ifPresent(existStudent -> existStudent.update(student.getStudentId(), student.getName()));
    }

    public void delete(Integer studentId) {
        AtomicReference<Student> delete = new AtomicReference<>();
        students.stream()
                .filter(existStudent -> existStudent.getStudentId().equals(studentId))
                .findFirst()
                .ifPresent(delete::set);
        if (delete.get() != null)
            students.remove(delete.get());
    }
}
