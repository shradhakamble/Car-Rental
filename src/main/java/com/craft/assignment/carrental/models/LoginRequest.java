package com.craft.assignment.carrental.models;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Data
public class LoginRequest {
    private String email;
    private String password;
}


