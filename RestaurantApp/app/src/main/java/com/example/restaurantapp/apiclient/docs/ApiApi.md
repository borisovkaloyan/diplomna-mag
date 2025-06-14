# ApiApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**apiMenuItemsGetAllCategoriesRetrieve**](ApiApi.md#apiMenuItemsGetAllCategoriesRetrieve) | **GET** /api/menu-items/get-all-categories/ |  |
| [**apiMenuItemsGetItemByIdRetrieve**](ApiApi.md#apiMenuItemsGetItemByIdRetrieve) | **GET** /api/menu-items/{id}/get-item-by-id/ |  |
| [**apiMenuItemsItemsByCategoryCreate**](ApiApi.md#apiMenuItemsItemsByCategoryCreate) | **POST** /api/menu-items/items-by-category/ |  |
| [**apiMenuItemsList**](ApiApi.md#apiMenuItemsList) | **GET** /api/menu-items/ |  |
| [**apiOrdersCreateOrderCreate**](ApiApi.md#apiOrdersCreateOrderCreate) | **POST** /api/orders/create-order/ |  |
| [**apiOrdersLatestOrderRetrieve**](ApiApi.md#apiOrdersLatestOrderRetrieve) | **GET** /api/orders/latest-order/ |  |
| [**apiOrdersOrdersByUserCreate**](ApiApi.md#apiOrdersOrdersByUserCreate) | **POST** /api/orders/orders-by-user/ |  |
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

<a id="apiMenuItemsGetItemByIdRetrieve"></a>
# **apiMenuItemsGetItemByIdRetrieve**
> MenuItem apiMenuItemsGetItemByIdRetrieve(id)



### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ApiApi()
val id : kotlin.Int = 56 // kotlin.Int | A unique integer value identifying this menu item.
try {
    val result : MenuItem = apiInstance.apiMenuItemsGetItemByIdRetrieve(id)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ApiApi#apiMenuItemsGetItemByIdRetrieve")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ApiApi#apiMenuItemsGetItemByIdRetrieve")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Int**| A unique integer value identifying this menu item. | |

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

<a id="apiMenuItemsItemsByCategoryCreate"></a>
# **apiMenuItemsItemsByCategoryCreate**
> kotlin.collections.List&lt;MenuItem&gt; apiMenuItemsItemsByCategoryCreate(orderCategory)



### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ApiApi()
val orderCategory : OrderCategory =  // OrderCategory | 
try {
    val result : kotlin.collections.List<MenuItem> = apiInstance.apiMenuItemsItemsByCategoryCreate(orderCategory)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ApiApi#apiMenuItemsItemsByCategoryCreate")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ApiApi#apiMenuItemsItemsByCategoryCreate")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **orderCategory** | [**OrderCategory**](OrderCategory.md)|  | |

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

 - **Content-Type**: application/json
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
> Order apiOrdersCreateOrderCreate(orderRequest)



### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ApiApi()
val orderRequest : OrderRequest =  // OrderRequest | 
try {
    val result : Order = apiInstance.apiOrdersCreateOrderCreate(orderRequest)
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
| **orderRequest** | [**OrderRequest**](OrderRequest.md)|  | |

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

<a id="apiOrdersOrdersByUserCreate"></a>
# **apiOrdersOrdersByUserCreate**
> kotlin.collections.List&lt;Order&gt; apiOrdersOrdersByUserCreate(ordersByUser)



### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ApiApi()
val ordersByUser : OrdersByUser =  // OrdersByUser | 
try {
    val result : kotlin.collections.List<Order> = apiInstance.apiOrdersOrdersByUserCreate(ordersByUser)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ApiApi#apiOrdersOrdersByUserCreate")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ApiApi#apiOrdersOrdersByUserCreate")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **ordersByUser** | [**OrdersByUser**](OrdersByUser.md)|  | |

### Return type

[**kotlin.collections.List&lt;Order&gt;**](Order.md)

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

