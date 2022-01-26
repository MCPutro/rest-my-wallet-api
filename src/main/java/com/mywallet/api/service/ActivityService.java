package com.mywallet.api.service;

import com.mywallet.api.model.Activity;
import com.mywallet.api.response.format.ResponseFormat;

public interface ActivityService {
    public ResponseFormat createNewActivity(Activity newActivity, String UID);
    public ResponseFormat getActivities(String UID, String period);
    public ResponseFormat removeActivity(String uid, String activityId, String period);
    public ResponseFormat updateActivity(String UID, String period, Activity activity);
}
