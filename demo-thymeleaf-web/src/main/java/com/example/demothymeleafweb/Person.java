package com.example.demothymeleafweb;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

public class Person {

    @NotEmpty
    private String name;

    @Min(0)
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
