# My Wallet REST API

- API version: 2.0.7
    - Build date: 2022-01-27T15:10:21.372627500+07:00[Asia/Bangkok]
    - Documentation: https://rest-api-my-wallet.herokuapp.com/doc

## Preparation
### Tools
  - Spring Boot Editor (Spring Tools Suite or IntelliJ IDEA Ultimate)
  - JWT 
  - PostgreSQL 
  - Firebase Firestore & User create
  - Swagger (documentation tool)
  - Postman or Insomnia or Soap UI (Client Test) 
### Config 
  - Rename file **firebase-key.json.example** to **firebase-key.json** in folder resources 
  - Put the firebase key in the file **firebase-key.json** then save 
  - Update url, username and password database in file **application.yml**
  - Open project 

## Documentation for API Endpoints

All URIs are relative to *http://localhost:8080*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*UserControllerApi* | [**createAndLoginUsingPOST**](docs/UserControllerApi.md#createAndLoginUsingPOST) | **POST** /api/user/createAndLogin | Create than login with new account
*UserControllerApi* | [**getTokenByRefreshTokenUsingPOST**](docs/UserControllerApi.md#getTokenByRefreshTokenUsingPOST) | **POST** /api/user/refresh-token | Refresh token
*UserControllerApi* | [**signinUsingPOST**](docs/UserControllerApi.md#signinUsingPOST) | **POST** /api/user/signin | Sign in
*UserControllerApi* | [**signupUsingPOST**](docs/UserControllerApi.md#signupUsingPOST) | **POST** /api/user/signup | Create new account
*UserControllerApi* | [**updateUserDataUsingPOST**](docs/UserControllerApi.md#updateUserDataUsingPOST) | **POST** /api/user/update | Update info user
*ActivityControllerApi* | [**addNewActivityUsingPOST**](docs/ActivityControllerApi.md#addNewActivityUsingPOST) | **POST** /api/activity/ | Create new activity
*ActivityControllerApi* | [**deleteActivityUsingDELETE**](docs/ActivityControllerApi.md#deleteActivityUsingDELETE) | **DELETE** /api/activity/ | Delete Activity by UID, Activity Id and Period
*ActivityControllerApi* | [**getActivitiesUsingGET**](docs/ActivityControllerApi.md#getActivitiesUsingGET) | **GET** /api/activity/ | Get list activity by UID and Period
*ActivityControllerApi* | [**updateActivityUsingPUT**](docs/ActivityControllerApi.md#updateActivityUsingPUT) | **PUT** /api/activity/ | Update Activity by UID and Activity Id
*WalletControllerApi* | [**addWalletUsingPOST**](docs/WalletControllerApi.md#addWalletUsingPOST) | **POST** /api/wallet/ | Create new wallet
*WalletControllerApi* | [**cancelTransferUsingPOST**](docs/WalletControllerApi.md#cancelTransferUsingPOST) | **POST** /api/wallet/cancelTransfer | Cancel transfer between own wallet
*WalletControllerApi* | [**getAllWalletUsingGET**](docs/WalletControllerApi.md#getAllWalletUsingGET) | **GET** /api/wallet/ | Get list wallet by UID
*WalletControllerApi* | [**removeWalletUsingDELETE**](docs/WalletControllerApi.md#removeWalletUsingDELETE) | **DELETE** /api/wallet/ | Delete wallet by wallet Id
*WalletControllerApi* | [**transferUsingPOST**](docs/WalletControllerApi.md#transferUsingPOST) | **POST** /api/wallet/transfer | Transfer between own wallet


## Documentation for Models

- [Activity](docs/Activity.md)
- [RefreshTokenRequest](docs/RefreshTokenRequest.md)
- [ResponseFormat](docs/ResponseFormat.md)
- [TransferRequest](docs/TransferRequest.md)
- [UserSignInRequest](docs/UserSignInRequest.md)
- [UserSignUpRequest](docs/UserSignUpRequest.md)
- [UserUpdateRequest](docs/UserUpdateRequest.md)
- [Wallet](docs/Wallet.md)


## Documentation for Authorization

Authentication schemes defined for the API:
### Bearer

- **Type**: API key
- **API key parameter name**: Authorization
- **Location**: HTTP header


## Author
em.chepe@gmail.com


## Example in mobile app
https://bit.ly/3ABO6DA
