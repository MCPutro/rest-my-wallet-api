package com.mywallet.api.service;

import com.google.cloud.firestore.CollectionReference;
import com.mywallet.api.entity.User;
import com.mywallet.api.model.Activity;
import com.mywallet.api.request.UserUpdateRequest;
import com.mywallet.api.response.format.ResponseFormat;

public interface FireBaseService {
    ResponseFormat createUser(User baru);
    String insertActivity(String uid, String period, Activity newActivity);
    CollectionReference getActivityCollectionReference(String UID, String period);
    String updateUserInfo(UserUpdateRequest newUser);
}
