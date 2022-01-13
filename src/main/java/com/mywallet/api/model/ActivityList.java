package com.mywallet.api.model;

import java.util.ArrayList;

import com.mywallet.api.response.Data;

import lombok.*;

@Getter
@Setter
@Builder
public class ActivityList implements Data{
	private String period;
	private ArrayList<Activity> activities;
		
}
