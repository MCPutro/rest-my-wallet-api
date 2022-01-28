# WalletControllerApi

All URIs are relative to *http://localhost:8080*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addWallet**](WalletControllerApi.md#addWallet) | **POST** /api/wallet/ | Create new wallet
[**cancelTransfer**](WalletControllerApi.md#cancelTransfer) | **POST** /api/wallet/cancelTransfer | Cancel transfer between own wallet
[**getAllWallet**](WalletControllerApi.md#getAllWallet) | **GET** /api/wallet/ | Get list wallet by UID
[**removeWallet**](WalletControllerApi.md#removeWallet) | **DELETE** /api/wallet/ | Delete wallet by wallet Id
[**transfer**](WalletControllerApi.md#transfer) | **POST** /api/wallet/transfer | Transfer between own wallet


<a name="addWallet"></a>
# **addWallet**
> POST http://localhost:8080/api/wallet/

Create new wallet

### Body Example
```json
{
  "id" : "wallet-test-3" ,
  "backgroundColor" : "background2",
  "textColor" : "#FF000000",
  "name" : "bank B",
  "nominal" : 100,
  "type" : "Cash"
}
```

### Parameters

| Name       | Type                    | Description | In     |
|------------|-------------------------|------------|--------|
| **UID**    | **String**              | UID        | Header |
| **wallet** | [**Wallet**](Wallet.md) | wallet     | Body   |

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

<a name="cancelTransfer"></a>
# **cancelTransfer**
> POST http://localhost:8080/api/wallet/cancelTransfer

Cancel transfer between own wallet

### Body Example
```json
{
  "id" : "test-trf1" ,
  "walletIdSource" : "1235ee50-6e9e-4435-bbae-fbdb90112844",
  "walletIdDestination" : "wallet-test-3",
  "nominal" : 10,
  "fee" : 0
}
```

### Parameters

| Name       | Type                                      | Description | Notes  | Example           |
|------------|-------------------------------------------|-------------|--------|-------------------|
| **UID**    | **String**                                | UID         | Header | m1k3qw15as4d81d8a |
| **period** | **String**                                | period      | Header | 2022-01           |
| **trf**    | [**TransferRequest**](TransferRequest.md) | trf         | Body   |                   |

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

<a name="getAllWallet"></a>
# **getAllWallet**
> GET http://localhost:8080/api/wallet/

Get list wallet by UID

### Parameters

| Name    | Type       | Description | In     | Example            |
|---------|------------|-------------|--------|--------------------|
| **UID** | **String** | UID         | Header | m1k3qw15as4d81d8a  |

### Return type

[**List&lt;Wallet&gt;**](Wallet.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

### HTTP response details
| Status code | Description  | Response headers |
|-------------|--------------|------------------|
| **200**     | OK           | -                |
| **401**     | Unauthorized | -                |
| **403**     | Forbidden    | -                |
| **404**     | Not Found    | -                |

<a name="removeWallet"></a>
# **removeWallet**
> DELETE http://localhost:8080/api/wallet/

Delete wallet by wallet Id

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **walletId** | **String**| walletId |

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

<a name="transfer"></a>
# **transfer**
> ResponseFormat transfer(UID, trf)

Transfer between own wallet

### Body Example
```json
{
  "id" : "test-trf1" ,
  "walletIdSource" : "1235ee50-6e9e-4435-bbae-fbdb90112844",
  "walletIdDestination" : "fbdb90112844-bbae-wallet-1235ee50-test",
  "nominal" : 10,
  "fee" : 0
}
```

### Parameters

| Name    | Type                                      | Description | In     | Example           |
|---------|-------------------------------------------|-------------|--------|-------------------|
| **UID** | **String**                                | UID         | Header | m1k3qw15as4d81d8a |
| **trf** | [**TransferRequest**](TransferRequest.md) | trf         | Body   |                   |

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

