package com.mywallet.api.model;

import java.util.ArrayList;

import com.mywallet.api.response.Data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityList implements Data{
	private String period;
	private ArrayList<Activity> activities;
		
}
