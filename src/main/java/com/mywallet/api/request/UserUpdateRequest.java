package com.mywallet.api.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserUpdateRequest {
    private String email;
    private String password;
    private String username;
    private String uid;
    private String urlAvatar;
}
