# UserControllerApi

All URIs are relative to *http://localhost:8080*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createAndLogin**](UserControllerApi.md#createAndLogin) | **POST** /api/user/createAndLogin | Create than login with new account
[**getTokenByRefreshToken**](UserControllerApi.md#getTokenByRefreshToken) | **POST** /api/user/refresh-token | Refresh token
[**signIn**](UserControllerApi.md#signIn) | **POST** /api/user/signin | Sign in
[**signUp**](UserControllerApi.md#signUp) | **POST** /api/user/signup | Sign up
[**updateUserData**](UserControllerApi.md#updateUserData) | **POST** /api/user/update | Update info user


<a name="createAndLogin"></a>
# **createAndLogin**
> POST http://localhost:8080/api/user/createAndLogin

Create than login with new account

### Example
```json
{
  "email" : "email02@local.com",
  "password" : "123456789",
  "username" : "user local",
  "deviceId" : "adalah"
}

```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **newUser** | [**UserSignUpRequest**](UserSignUpRequest.md)| newUser |

### Return type

[**ResponseFormat**](ResponseFormat.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |
**201** | Created |  -  |
**401** | Unauthorized |  -  |
**403** | Forbidden |  -  |
**404** | Not Found |  -  |

<a name="getTokenByRefreshToken"></a>
# **getTokenByRefreshToken**
> POST http://localhost:8080/api/user/refresh-token

Refresh token

### Example
```json
{
  "refreshToken": "f8587320-4612-bed9-45eb380c3751.MTY0Mzk3OTM3NQ=="
} 
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **refreshToken** | [**RefreshTokenRequest**](RefreshTokenRequest.md)| refreshToken |

### Return type

[**ResponseFormat**](ResponseFormat.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |
**201** | Created |  -  |
**401** | Unauthorized |  -  |
**403** | Forbidden |  -  |
**404** | Not Found |  -  |

<a name="signIn"></a>
# **signIn**
> POST http://localhost:8080/api/user/signin

Sign in

### Example
```json
{
    "email" : "email@test.com",
    "password" : "123456789"
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **user** | [**UserSignInRequest**](UserSignInRequest.md)| user |

### Return type

[**ResponseFormat**](ResponseFormat.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |
**201** | Created |  -  |
**401** | Unauthorized |  -  |
**403** | Forbidden |  -  |
**404** | Not Found |  -  |

<a name="signUp"></a>
# **signUp**
> POST http://localhost:8080/api/user/signup

Sign up

### Example
```json
{
  "email" : "email@test.com",
  "password" : "123456789",
  "username" : "user name",
  "deviceId" : "12a45s6d8"
}

```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **newUser** | [**UserSignUpRequest**](UserSignUpRequest.md)| newUser |

### Return type

[**ResponseFormat**](ResponseFormat.md)

### Authorization

No authorization required

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

<a name="updateUserData"></a>
# **updateUserData**
> POST http://localhost:8080/api/user/update

Update info user

### Example
```json
{
    "email": "email",
    "password": "password",
    "uid": "UID",
    "username": "username",
    "urlAvatar": "UrlAvatar()"
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userUpdate** | [**UserUpdateRequest**](UserUpdateRequest.md)| userUpdate |

### Return type

[**ResponseFormat**](ResponseFormat.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |
**201** | Created |  -  |
**401** | Unauthorized |  -  |
**403** | Forbidden |  -  |
**404** | Not Found |  -  |

