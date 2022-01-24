package com.mywallet.api.request;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String refreshToken;
}
