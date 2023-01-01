package com.saber.spring_camel_mongodb_demo.dto.student;

import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.ToNumberPolicy;
import lombok.Data;

import java.util.List;
@Data
public class StudentTermCourseDto {
    private String nationalCode ;
    private String studentNumber;
    private List<Course> courses;

    @Override
    public String toString() {
        return new GsonBuilder()
                .setLenient()
                .setPrettyPrinting()
                .enableComplexMapKeySerialization()
                .setLongSerializationPolicy(LongSerializationPolicy.DEFAULT)
                .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
                .create().toJson(this, StudentTermCourseDto.class);
    }
}
