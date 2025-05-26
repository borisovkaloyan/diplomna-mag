# ApiApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**apiMenuItemsGetAllCategoriesRetrieve**](ApiApi.md#apiMenuItemsGetAllCategoriesRetrieve) | **GET** /api/menu-items/get-all-categories/ |  |
| [**apiMenuItemsItemsByCategoryRetrieve**](ApiApi.md#apiMenuItemsItemsByCategoryRetrieve) | **GET** /api/menu-items/items-by-category/ |  |
| [**apiMenuItemsList**](ApiApi.md#apiMenuItemsList) | **GET** /api/menu-items/ |  |
| [**apiOrdersCreateOrderCreate**](ApiApi.md#apiOrdersCreateOrderCreate) | **POST** /api/orders/create-order/ |  |
| [**apiOrdersLatestOrderRetrieve**](ApiApi.md#apiOrdersLatestOrderRetrieve) | **GET** /api/orders/latest-order/ |  |
| [**apiOrdersOrdersByUserRetrieve**](ApiApi.md#apiOrdersOrdersByUserRetrieve) | **GET** /api/orders/orders-by-user/ |  |
| [**apiUsersLoginCreate**](ApiApi.md#apiUsersLoginCreate) | **POST** /api/users/login/ |  |
| [**apiUsersRegisterCreate**](ApiApi.md#apiUsersRegisterCreate) | **POST** /api/users/register/ |  |


<a id="apiMenuItemsGetAllCategoriesRetrieve"></a>
# **apiMenuItemsGetAllCategoriesRetrieve**
> MenuItem apiMenuItemsGetAllCategoriesRetrieve()



### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ApiApi()
try {
    val result : MenuItem = apiInstance.apiMenuItemsGetAllCategoriesRetrieve()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ApiApi#apiMenuItemsGetAllCategoriesRetrieve")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ApiApi#apiMenuItemsGetAllCategoriesRetrieve")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**MenuItem**](MenuItem.md)

### Authorization


Configure basicAuth:
    ApiClient.username = ""
    ApiClient.password = ""
Configure cookieAuth:
    ApiClient.apiKey["sessionid"] = ""
    ApiClient.apiKeyPrefix["sessionid"] = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a id="apiMenuItemsItemsByCategoryRetrieve"></a>
# **apiMenuItemsItemsByCategoryRetrieve**
> MenuItem apiMenuItemsItemsByCategoryRetrieve()



### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ApiApi()
try {
    val result : MenuItem = apiInstance.apiMenuItemsItemsByCategoryRetrieve()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ApiApi#apiMenuItemsItemsByCategoryRetrieve")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ApiApi#apiMenuItemsItemsByCategoryRetrieve")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**MenuItem**](MenuItem.md)

### Authorization


Configure basicAuth:
    ApiClient.username = ""
    ApiClient.password = ""
Configure cookieAuth:
    ApiClient.apiKey["sessionid"] = ""
    ApiClient.apiKeyPrefix["sessionid"] = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a id="apiMenuItemsList"></a>
# **apiMenuItemsList**
> kotlin.collections.List&lt;MenuItem&gt; apiMenuItemsList()



### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ApiApi()
try {
    val result : kotlin.collections.List<MenuItem> = apiInstance.apiMenuItemsList()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ApiApi#apiMenuItemsList")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ApiApi#apiMenuItemsList")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.collections.List&lt;MenuItem&gt;**](MenuItem.md)

### Authorization


Configure basicAuth:
    ApiClient.username = ""
    ApiClient.password = ""
Configure cookieAuth:
    ApiClient.apiKey["sessionid"] = ""
    ApiClient.apiKeyPrefix["sessionid"] = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a id="apiOrdersCreateOrderCreate"></a>
# **apiOrdersCreateOrderCreate**
> Order apiOrdersCreateOrderCreate(order)



### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ApiApi()
val order : Order =  // Order | 
try {
    val result : Order = apiInstance.apiOrdersCreateOrderCreate(order)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ApiApi#apiOrdersCreateOrderCreate")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ApiApi#apiOrdersCreateOrderCreate")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **order** | [**Order**](Order.md)|  | |

### Return type

[**Order**](Order.md)

### Authorization


Configure basicAuth:
    ApiClient.username = ""
    ApiClient.password = ""
Configure cookieAuth:
    ApiClient.apiKey["sessionid"] = ""
    ApiClient.apiKeyPrefix["sessionid"] = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="apiOrdersLatestOrderRetrieve"></a>
# **apiOrdersLatestOrderRetrieve**
> Order apiOrdersLatestOrderRetrieve()



### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ApiApi()
try {
    val result : Order = apiInstance.apiOrdersLatestOrderRetrieve()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ApiApi#apiOrdersLatestOrderRetrieve")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ApiApi#apiOrdersLatestOrderRetrieve")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**Order**](Order.md)

### Authorization


Configure basicAuth:
    ApiClient.username = ""
    ApiClient.password = ""
Configure cookieAuth:
    ApiClient.apiKey["sessionid"] = ""
    ApiClient.apiKeyPrefix["sessionid"] = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a id="apiOrdersOrdersByUserRetrieve"></a>
# **apiOrdersOrdersByUserRetrieve**
> Order apiOrdersOrdersByUserRetrieve()



### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ApiApi()
try {
    val result : Order = apiInstance.apiOrdersOrdersByUserRetrieve()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ApiApi#apiOrdersOrdersByUserRetrieve")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ApiApi#apiOrdersOrdersByUserRetrieve")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**Order**](Order.md)

### Authorization


Configure basicAuth:
    ApiClient.username = ""
    ApiClient.password = ""
Configure cookieAuth:
    ApiClient.apiKey["sessionid"] = ""
    ApiClient.apiKeyPrefix["sessionid"] = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a id="apiUsersLoginCreate"></a>
# **apiUsersLoginCreate**
> UserLoginResponse apiUsersLoginCreate(userLogin)



### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ApiApi()
val userLogin : UserLogin =  // UserLogin | 
try {
    val result : UserLoginResponse = apiInstance.apiUsersLoginCreate(userLogin)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ApiApi#apiUsersLoginCreate")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ApiApi#apiUsersLoginCreate")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userLogin** | [**UserLogin**](UserLogin.md)|  | |

### Return type

[**UserLoginResponse**](UserLoginResponse.md)

### Authorization


Configure basicAuth:
    ApiClient.username = ""
    ApiClient.password = ""
Configure cookieAuth:
    ApiClient.apiKey["sessionid"] = ""
    ApiClient.apiKeyPrefix["sessionid"] = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="apiUsersRegisterCreate"></a>
# **apiUsersRegisterCreate**
> UserRegistrationResponse apiUsersRegisterCreate(userRegistration)



### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ApiApi()
val userRegistration : UserRegistration =  // UserRegistration | 
try {
    val result : UserRegistrationResponse = apiInstance.apiUsersRegisterCreate(userRegistration)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ApiApi#apiUsersRegisterCreate")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ApiApi#apiUsersRegisterCreate")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userRegistration** | [**UserRegistration**](UserRegistration.md)|  | |

### Return type

[**UserRegistrationResponse**](UserRegistrationResponse.md)

### Authorization


Configure basicAuth:
    ApiClient.username = ""
    ApiClient.password = ""
Configure cookieAuth:
    ApiClient.apiKey["sessionid"] = ""
    ApiClient.apiKeyPrefix["sessionid"] = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

