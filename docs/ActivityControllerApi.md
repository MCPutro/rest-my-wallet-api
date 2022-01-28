# ActivityControllerApi

All URIs are relative to *http://localhost:8080*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addNewActivity**](ActivityControllerApi.md#addNewActivity) | **POST** /api/activity/ | Create new activity
[**deleteActivity**](ActivityControllerApi.md#deleteActivity) | **DELETE** /api/activity/ | Delete Activity by UID, Activity Id and Period
[**getActivities**](ActivityControllerApi.md#getActivities) | **GET** /api/activity/ | Get list activity by UID and Period
[**updateActivity**](ActivityControllerApi.md#updateActivity) | **PUT** /api/activity/ | Update Activity by UID and Activity Id


<a name="addNewActivity"></a>
# **addNewActivity**
>POST http://localhost:8080/api/activity/

Create new activity

### Body Example
```json
{
  "id": "test-new-activ-1",
  "walletId": "1235ee50-6e9e-4435-bbae-fbdb90112844",
  "walletName": "Bank test",
  "title": "Mortgage",
  "category": "House",
  "desc": "test desc",
  "nominal": 5,
  "date": "2022-02-20T19:18:17+0700",
  "income": false
}
```

### Parameters

| Name            | Type                        | Description | In     |
|-----------------|-----------------------------|-------------|--------|
| **UID**         | **String**                  | UID         | Header |
| **newActivity** | [**Activity**](Activity.md) | newActivity | Body   |

### Return type

[**ResponseFormat**](ResponseFormat.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |
**201** | Created |  -  |
**401** | Unauthorized |  -  |
**403** | Forbidden |  -  |
**404** | Not Found |  -  |

<a name="deleteActivity"></a>
# **deleteActivity**
> Delete http://localhost:8080/api/activity/

Delete Activity

### Parameters

| Name           | Type       | Description | In     | Example           |
|----------------|------------|-------------|--------|-------------------|
| **UID**        | **String** | UID         | Header | m1k3qw15as4d81d8a |
| **activityId** | **String** | activityId  | header | test-new-activ-1  |
| **period**     | **String** | period      | Header | 2022-02           |

### Return type

[**ResponseFormat**](ResponseFormat.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |
**204** | No Content |  -  |
**401** | Unauthorized |  -  |
**403** | Forbidden |  -  |

<a name="getActivities"></a>
# **getActivities**
> GET http://localhost:8080/api/activity/

Get list activity by UID and Period

### Parameters

| Name       | Type       | Description | In     | Example           |
|------------|------------|-------------|--------|-------------------|
| **UID**    | **String** | UID         | Header | m1k3qw15as4d81d8a |
| **period** | **String** | period      | Header | 2021-01           |

### Return type

[**ResponseFormat**](ResponseFormat.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |
**401** | Unauthorized |  -  |
**403** | Forbidden |  -  |
**404** | Not Found |  -  |

<a name="updateActivity"></a>
# **updateActivity**
> PUT http://localhost:8080

Update Activity by UID and Activity Id

### Body Example
```json
{
    "category": "string",
    "date": "2022-01-28T14:58:25.741Z",
    "desc": "string",
    "id": "string",
    "income": true,
    "nominal": 0,
    "title": "string",
    "walletId": "string",
    "walletName": "string"
}
```

### Parameters

| Name            | Type                        | Description | In     | Example           |
|-----------------|-----------------------------|-------------|--------|-------------------|
| **UID**         | **String**                  | UID         | Header | m1k3qw15as4d81d8a |
| **period**      | **String**                  | period      | Header | 2022-01           |
| **newActivity** | [**Activity**](Activity.md) | newActivity | Body   |                   |

### Return type

[**ResponseFormat**](ResponseFormat.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |
**201** | Created |  -  |
**401** | Unauthorized |  -  |
**403** | Forbidden |  -  |
**404** | Not Found |  -  |

