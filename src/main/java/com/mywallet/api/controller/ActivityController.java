package com.mywallet.api.controller;

import com.mywallet.api.documentation.ActivityApi;
import com.mywallet.api.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mywallet.api.model.Activity;
import com.mywallet.api.response.format.ResponseFormat;


@RestController
@RequestMapping("/api/activity")
public class ActivityController implements ActivityApi {
	
	private final ActivityService activityService;

	@Autowired
	public ActivityController(ActivityService activityService) {
		this.activityService = activityService;
	}

	@PostMapping("/")
	public ResponseFormat addNewActivity(@RequestBody Activity newActivity, @RequestHeader String UID) {
		return this.activityService.createNewActivity(newActivity, UID);
	}
	
	@GetMapping("/")
	public ResponseFormat getActivities(@RequestHeader String period, @RequestHeader String UID) {
		return this.activityService.getActivities(UID, period);
	}
	
	@DeleteMapping("/")
	public ResponseFormat deleteActivity(@RequestHeader String UID, @RequestHeader String activityId, @RequestHeader String period) {
		return this.activityService.removeActivity(UID, activityId, period);
		
	}

	@PutMapping("/")
	public ResponseFormat updateActivity(@RequestHeader String period, @RequestHeader String UID, @RequestBody Activity newActivity) {
		return this.activityService.updateActivity(UID, period, newActivity);
		//return null;
	}

}
