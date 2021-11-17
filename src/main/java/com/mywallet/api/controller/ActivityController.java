package com.mywallet.api.controller;

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
import com.mywallet.api.response.Resp;
import com.mywallet.api.service.ActivityService;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {
	
	@Autowired private ActivityService activityService;
	
	@PutMapping("/")
	public Resp addNewActivity(@RequestBody Activity newActivity, @RequestHeader String UID) {
		return this.activityService.createNewActivity(newActivity, UID);
		
	}
	
	@GetMapping("/")
	public Resp getActivities(@RequestHeader String period, @RequestHeader String UID) {
		return this.activityService.getActivities(UID, period);
		
	}
	
	@DeleteMapping("/")
	public Resp deleteActivity(@RequestHeader String UID, @RequestHeader String activityId, @RequestHeader String period) {
		return this.activityService.removeActivity(UID, activityId, period);
		
	}
	
	@PostMapping("/")
	public Resp updateActivity(@RequestHeader String period, @RequestHeader String UID, @RequestBody Activity newActivity) {
		return this.activityService.updateActivity(UID, period, newActivity);
		//return null;
	}

}
