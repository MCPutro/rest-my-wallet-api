package com.mywallet.api.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserSignInRequest {
    private String email;
    private String password;

}
