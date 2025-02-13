package com.scheduler.memberservice.member.student.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ClassListDTO {

    private String studentName;
    private List<Integer> mondayClassList = new ArrayList<>();
    private List<Integer> tuesdayClassList = new ArrayList<>();
    private List<Integer> wednesdayClassList = new ArrayList<>();
    private List<Integer> thursdayClassList = new ArrayList<>();
    private List<Integer> fridayClassList = new ArrayList<>();

    public static ClassListDTO getInstance(){
        return new ClassListDTO();
    }
}
