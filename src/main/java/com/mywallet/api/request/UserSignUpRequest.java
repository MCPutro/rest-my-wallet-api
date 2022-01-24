package com.mywallet.api.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserSignUpRequest {
    private String email;
    private String password;
    private String username;
    private String deviceId;
}
