package com.mywallet.api.documentation;

import com.mywallet.api.model.Activity;
import com.mywallet.api.response.format.ResponseFormat;
import io.swagger.annotations.*;


@Api(value = "haho")
public interface ActivityApi {

    @ApiOperation(value = "Create new activity", response = ResponseFormat.class, nickname = "addNewActivity")
    public ResponseFormat addNewActivity(Activity newActivity, String UID);

    @ApiOperation(value = "Get list activity by UID and Period", response = ResponseFormat.class, nickname = "getActivities")
    public ResponseFormat getActivities(String period, String UID);

    @ApiOperation(value = "Delete Activity by UID, Activity Id and Period", response = ResponseFormat.class, nickname = "deleteActivity")
	public ResponseFormat deleteActivity(String UID, String activityId, String period);

    @ApiOperation(value = "Update Activity by UID and Activity Id", response = ResponseFormat.class, nickname = "updateActivity")
	public ResponseFormat updateActivity(String period, String UID, Activity newActivity);
}
