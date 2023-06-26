package com.sparta.gunwooklv3.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignupRequestDto {


    private String username;
    private String password;
    private boolean admin = false;
    private String adminToken = "";

}