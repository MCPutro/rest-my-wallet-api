package com.mywallet.api.documentation;

import com.mywallet.api.model.Activity;
import com.mywallet.api.response.format.ResponseFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Api(value = "haho")
public interface ActivityApi {

    @ApiOperation(value = "test1", response = ResponseFormat.class)
    public ResponseFormat addNewActivity(Activity newActivity, String UID);

    @ApiOperation(value = "test2", response = ResponseFormat.class)
    public ResponseFormat getActivities(String period, String UID);

    @ApiOperation(value = "test3", response = ResponseFormat.class)
	public ResponseFormat deleteActivity(String UID, String activityId, String period);

    @ApiOperation(value = "test4", response = ResponseFormat.class)
	public ResponseFormat updateActivity(String period, String UID, Activity newActivity);
}
