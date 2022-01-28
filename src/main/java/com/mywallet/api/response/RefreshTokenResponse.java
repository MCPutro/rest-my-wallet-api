package com.mywallet.api.response;

import com.mywallet.api.response.format.Data;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class RefreshTokenResponse implements Data {

    private final String type = "type";

    private String token;

    private String refreshToken;
}
