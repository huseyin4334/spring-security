package com.example.springSecurity.bean.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Student {
    private Integer studentId;
    private String name;

    public void update(Integer studentId, String name) {
        this.studentId = studentId;
        this.name = name;
    }
}
