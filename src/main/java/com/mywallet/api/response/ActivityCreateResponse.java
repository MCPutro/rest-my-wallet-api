package com.mywallet.api.response;

import com.mywallet.api.response.format.Data;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ActivityCreateResponse implements Data {
    private String activityId ;
    private String walletId ;
    private Double remainingBalance ;
}
