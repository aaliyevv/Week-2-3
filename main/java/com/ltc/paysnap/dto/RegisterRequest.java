package com.ltc.paysnap.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
}
