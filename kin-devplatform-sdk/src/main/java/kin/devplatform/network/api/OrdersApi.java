package kin.devplatform.network.api;/*
 * Kin Ecosystem
 * Apis for client to server interaction
 *
 * OpenAPI spec version: 1.0.0
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


import static kin.devplatform.core.network.ApiClient.DELETE;
import static kin.devplatform.core.network.ApiClient.GET;
import static kin.devplatform.core.network.ApiClient.POST;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kin.devplatform.Configuration;
import kin.devplatform.core.network.ApiCallback;
import kin.devplatform.core.network.ApiClient;
import kin.devplatform.core.network.ApiException;
import kin.devplatform.core.network.ApiResponse;
import kin.devplatform.core.network.Pair;
import kin.devplatform.network.model.EarnSubmission;
import kin.devplatform.network.model.ExternalOrderRequest;
import kin.devplatform.network.model.OpenOrder;
import kin.devplatform.network.model.Order;
import kin.devplatform.network.model.OrderList;
import okhttp3.Call;


public class OrdersApi {

	private ApiClient apiClient;

	public OrdersApi() {
		this(Configuration.getDefaultApiClient());
	}

	public OrdersApi(ApiClient apiClient) {
		this.apiClient = apiClient;
	}

	public ApiClient getApiClient() {
		return apiClient;
	}

	public void setApiClient(ApiClient apiClient) {
		this.apiClient = apiClient;
	}


	/**
	 * Build call for cancelOrder
	 *
	 * @param orderId The order id (required)
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @return Call to execute
	 * @throws ApiException If fail to serialize the request body object
	 */
	public Call cancelOrderCall(String orderId, String X_REQUEST_ID) throws ApiException {
		Object localVarPostBody = null;

		// create path and map variables
		String localVarPath = "/orders/{order_id}"
			.replaceAll("\\{" + "order_id" + "\\}", apiClient.escapeString(orderId.toString()));

		List<Pair> localVarQueryParams = new ArrayList<Pair>();
		List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

		Map<String, String> localVarHeaderParams = new HashMap<String, String>();
		if (X_REQUEST_ID != null) {
			localVarHeaderParams.put("X-REQUEST-ID", apiClient.parameterToString(X_REQUEST_ID));
		}

		Map<String, Object> localVarFormParams = new HashMap<String, Object>();

		final String[] localVarAccepts = {
			"application/json"
		};
		final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
		if (localVarAccept != null) {
			localVarHeaderParams.put("Accept", localVarAccept);
		}

		final String[] localVarContentTypes = {

		};
		final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
		localVarHeaderParams.put("Content-Type", localVarContentType);

		String[] localVarAuthNames = new String[]{};
		return apiClient
			.buildCall(localVarPath, DELETE, localVarQueryParams, localVarCollectionQueryParams, localVarPostBody,
				localVarHeaderParams, localVarFormParams, localVarAuthNames);
	}

	@SuppressWarnings("rawtypes")
	private Call cancelOrderValidateBeforeCall(String orderId, String X_REQUEST_ID)
		throws ApiException {

		// verify the required parameter 'orderId' is set
		if (orderId == null) {
			throw new ApiException("Missing the required parameter 'orderId' when calling cancelOrder(Async)");
		}

		// verify the required parameter 'X_REQUEST_ID' is set
		if (X_REQUEST_ID == null) {
			throw new ApiException("Missing the required parameter 'X_REQUEST_ID' when calling cancelOrder(Async)");
		}

		Call call = cancelOrderCall(orderId, X_REQUEST_ID);
		return call;


	}

	/**
	 * cancel an order cancel an order
	 *
	 * @param orderId The order id (required)
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
	 */
	public void cancelOrder(String orderId, String X_REQUEST_ID) throws ApiException {
		cancelOrderWithHttpInfo(orderId, X_REQUEST_ID);
	}

	/**
	 * cancel an order cancel an order
	 *
	 * @param orderId The order id (required)
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @return ApiResponse&lt;Void&gt;
	 * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
	 */
	public ApiResponse<Void> cancelOrderWithHttpInfo(String orderId, String X_REQUEST_ID) throws ApiException {
		Call call = cancelOrderValidateBeforeCall(orderId, X_REQUEST_ID);
		return apiClient.execute(call);
	}

	/**
	 * cancel an order (asynchronously) cancel an order
	 *
	 * @param orderId The order id (required)
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @param callback The callback to be executed when the API call finishes
	 * @return The request call
	 * @throws ApiException If fail to process the API call, e.g. serializing the request body object
	 */
	public Call cancelOrderAsync(String orderId, String X_REQUEST_ID, final ApiCallback<Void> callback)
		throws ApiException {

		Call call = cancelOrderValidateBeforeCall(orderId, X_REQUEST_ID);
		apiClient.executeAsync(call, callback);
		return call;
	}

	/**
	 * Build call for createExternalOrder
	 *
	 * @param externalorderrequest (required)
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @return Call to execute
	 * @throws ApiException If fail to serialize the request body object
	 */
	public Call createExternalOrderCall(ExternalOrderRequest externalorderrequest, String X_REQUEST_ID)
		throws ApiException {
		Object localVarPostBody = externalorderrequest;

		// create path and map variables
		String localVarPath = "/offers/external/orders";

		List<Pair> localVarQueryParams = new ArrayList<Pair>();
		List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

		Map<String, String> localVarHeaderParams = new HashMap<String, String>();
		if (X_REQUEST_ID != null) {
			localVarHeaderParams.put("X-REQUEST-ID", apiClient.parameterToString(X_REQUEST_ID));
		}

		Map<String, Object> localVarFormParams = new HashMap<String, Object>();

		final String[] localVarAccepts = {
			"application/json", "application/json"
		};
		final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
		if (localVarAccept != null) {
			localVarHeaderParams.put("Accept", localVarAccept);
		}

		final String[] localVarContentTypes = {
			"application/json"
		};
		final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
		localVarHeaderParams.put("Content-Type", localVarContentType);

		String[] localVarAuthNames = new String[]{};
		return apiClient
			.buildCall(localVarPath, POST, localVarQueryParams, localVarCollectionQueryParams, localVarPostBody,
				localVarHeaderParams, localVarFormParams, localVarAuthNames);
	}

	@SuppressWarnings("rawtypes")
	private Call createExternalOrderValidateBeforeCall(ExternalOrderRequest externalorderrequest, String X_REQUEST_ID)
		throws ApiException {

		// verify the required parameter 'externalorderrequest' is set
		if (externalorderrequest == null) {
			throw new ApiException(
				"Missing the required parameter 'externalorderrequest' when calling createExternalOrder(Async)");
		}

		// verify the required parameter 'X_REQUEST_ID' is set
		if (X_REQUEST_ID == null) {
			throw new ApiException(
				"Missing the required parameter 'X_REQUEST_ID' when calling createExternalOrder(Async)");
		}

		Call call = createExternalOrderCall(externalorderrequest, X_REQUEST_ID);
		return call;
	}

	/**
	 * create an external order for a native offer create an external order for a native offer
	 *
	 * @param externalorderrequest (required)
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @return OpenOrder
	 * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
	 */
	public OpenOrder createExternalOrder(ExternalOrderRequest externalorderrequest, String X_REQUEST_ID)
		throws ApiException {
		ApiResponse<OpenOrder> resp = createExternalOrderWithHttpInfo(externalorderrequest, X_REQUEST_ID);
		return resp.getData();
	}

	/**
	 * create an external order for a native offer create an external order for a native offer
	 *
	 * @param externalorderrequest (required)
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @return ApiResponse&lt;OpenOrder&gt;
	 * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
	 */
	public ApiResponse<OpenOrder> createExternalOrderWithHttpInfo(ExternalOrderRequest externalorderrequest,
		String X_REQUEST_ID) throws ApiException {
		Call call = createExternalOrderValidateBeforeCall(externalorderrequest, X_REQUEST_ID);
		Type localVarReturnType = new TypeToken<OpenOrder>() {
		}.getType();
		return apiClient.execute(call, localVarReturnType);
	}

	/**
	 * create an external order for a native offer (asynchronously) create an external order for a native offer
	 *
	 * @param externalorderrequest (required)
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @param callback The callback to be executed when the API call finishes
	 * @return The request call
	 * @throws ApiException If fail to process the API call, e.g. serializing the request body object
	 */
	public Call createExternalOrderAsync(ExternalOrderRequest externalorderrequest, String X_REQUEST_ID,
		final ApiCallback<OpenOrder> callback) throws ApiException {
		Call call = createExternalOrderValidateBeforeCall(externalorderrequest, X_REQUEST_ID);
		Type localVarReturnType = new TypeToken<OpenOrder>() {
		}.getType();
		apiClient.executeAsync(call, localVarReturnType, callback);
		return call;
	}

	/**
	 * Build call for createOrder
	 *
	 * @param offerId The offer id (required)
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @return Call to execute
	 * @throws ApiException If fail to serialize the request body object
	 */
	public Call createOrderCall(String offerId, String X_REQUEST_ID) throws ApiException {
		Object localVarPostBody = null;

		// create path and map variables
		String localVarPath = "/offers/{offer_id}/orders"
			.replaceAll("\\{" + "offer_id" + "\\}", apiClient.escapeString(offerId.toString()));

		List<Pair> localVarQueryParams = new ArrayList<Pair>();
		List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

		Map<String, String> localVarHeaderParams = new HashMap<String, String>();
		if (X_REQUEST_ID != null) {
			localVarHeaderParams.put("X-REQUEST-ID", apiClient.parameterToString(X_REQUEST_ID));
		}

		Map<String, Object> localVarFormParams = new HashMap<String, Object>();

		final String[] localVarAccepts = {
			"application/json", "application/json", "application/json"
		};
		final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
		if (localVarAccept != null) {
			localVarHeaderParams.put("Accept", localVarAccept);
		}

		final String[] localVarContentTypes = {

		};
		final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
		localVarHeaderParams.put("Content-Type", localVarContentType);

		String[] localVarAuthNames = new String[]{};
		return apiClient
			.buildCall(localVarPath, POST, localVarQueryParams, localVarCollectionQueryParams, localVarPostBody,
				localVarHeaderParams, localVarFormParams, localVarAuthNames);
	}

	@SuppressWarnings("rawtypes")
	private Call createOrderValidateBeforeCall(String offerId, String X_REQUEST_ID) throws ApiException {

		// verify the required parameter 'offerId' is set
		if (offerId == null) {
			throw new ApiException("Missing the required parameter 'offerId' when calling createOrder(Async)");
		}

		// verify the required parameter 'X_REQUEST_ID' is set
		if (X_REQUEST_ID == null) {
			throw new ApiException("Missing the required parameter 'X_REQUEST_ID' when calling createOrder(Async)");
		}

		Call call = createOrderCall(offerId, X_REQUEST_ID);
		return call;
	}

	/**
	 * create an order for an offer create an order for an offer
	 *
	 * @param offerId The offer id (required)
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @return OpenOrder
	 * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
	 */
	public OpenOrder createOrder(String offerId, String X_REQUEST_ID) throws ApiException {
		ApiResponse<OpenOrder> resp = createOrderWithHttpInfo(offerId, X_REQUEST_ID);
		return resp.getData();
	}

	/**
	 * create an order for an offer create an order for an offer
	 *
	 * @param offerId The offer id (required)
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @return ApiResponse&lt;OpenOrder&gt;
	 * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
	 */
	public ApiResponse<OpenOrder> createOrderWithHttpInfo(String offerId, String X_REQUEST_ID) throws ApiException {
		Call call = createOrderValidateBeforeCall(offerId, X_REQUEST_ID);
		Type localVarReturnType = new TypeToken<OpenOrder>() {
		}.getType();
		return apiClient.execute(call, localVarReturnType);
	}

	/**
	 * create an order for an offer (asynchronously) create an order for an offer
	 *
	 * @param offerId The offer id (required)
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @param callback The callback to be executed when the API call finishes
	 * @return The request call
	 * @throws ApiException If fail to process the API call, e.g. serializing the request body object
	 */
	public Call createOrderAsync(String offerId, String X_REQUEST_ID, final ApiCallback<OpenOrder> callback)
		throws ApiException {

		Call call = createOrderValidateBeforeCall(offerId, X_REQUEST_ID);
		Type localVarReturnType = new TypeToken<OpenOrder>() {
		}.getType();
		apiClient.executeAsync(call, localVarReturnType, callback);
		return call;
	}

	/**
	 * Build call for getHistory
	 *
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @param origin filter by origin (optional)
	 * @param offerId filter by offer_id (optional)
	 * @param limit maximum number of items in a list (optional)
	 * @param before cursor that points to the start of the page of data that has been returned (optional)
	 * @param after cursor that points to the end of the page of data that has been returned (optional)
	 * @return Call to execute
	 * @throws ApiException If fail to serialize the request body object
	 */
	public Call getHistoryCall(String X_REQUEST_ID, String origin, String offerId, Integer limit, String before,
		String after) throws ApiException {
		Object localVarPostBody = null;

		// create path and map variables
		String localVarPath = "/orders";

		List<Pair> localVarQueryParams = new ArrayList<Pair>();
		List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
		if (origin != null) {
			localVarQueryParams.addAll(apiClient.parameterToPair("origin", origin));
		}
		if (offerId != null) {
			localVarQueryParams.addAll(apiClient.parameterToPair("offerId", offerId));
		}
		if (limit != null) {
			localVarQueryParams.addAll(apiClient.parameterToPair("limit", limit));
		}
		if (before != null) {
			localVarQueryParams.addAll(apiClient.parameterToPair("before", before));
		}
		if (after != null) {
			localVarQueryParams.addAll(apiClient.parameterToPair("after", after));
		}

		Map<String, String> localVarHeaderParams = new HashMap<String, String>();
		if (X_REQUEST_ID != null) {
			localVarHeaderParams.put("X-REQUEST-ID", apiClient.parameterToString(X_REQUEST_ID));
		}

		Map<String, Object> localVarFormParams = new HashMap<String, Object>();

		final String[] localVarAccepts = {
			"application/json", "application/json"
		};
		final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
		if (localVarAccept != null) {
			localVarHeaderParams.put("Accept", localVarAccept);
		}

		final String[] localVarContentTypes = {

		};
		final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
		localVarHeaderParams.put("Content-Type", localVarContentType);

		String[] localVarAuthNames = new String[]{};
		return apiClient
			.buildCall(localVarPath, GET, localVarQueryParams, localVarCollectionQueryParams, localVarPostBody,
				localVarHeaderParams, localVarFormParams, localVarAuthNames);
	}

	@SuppressWarnings("rawtypes")
	private Call getHistoryValidateBeforeCall(String X_REQUEST_ID, String origin, String offerId, Integer limit,
		String before, String after) throws ApiException {

		// verify the required parameter 'X_REQUEST_ID' is set
		if (X_REQUEST_ID == null) {
			throw new ApiException("Missing the required parameter 'X_REQUEST_ID' when calling getHistory(Async)");
		}

		Call call = getHistoryCall(X_REQUEST_ID, origin, offerId, limit, before, after);
		return call;


	}

	/**
	 * get user order history get user order history
	 *
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @param origin filter by origin (optional)
	 * @param offerId filter by offer_id (optional)
	 * @param limit maximum number of items in a list (optional)
	 * @param before cursor that points to the start of the page of data that has been returned (optional)
	 * @param after cursor that points to the end of the page of data that has been returned (optional)
	 * @return OrderList
	 * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
	 */
	public OrderList getHistory(String X_REQUEST_ID, String origin, String offerId, Integer limit, String before,
		String after) throws ApiException {
		ApiResponse<OrderList> resp = getHistoryWithHttpInfo(X_REQUEST_ID, origin, offerId, limit, before, after);
		return resp.getData();
	}

	/**
	 * get user order history get user order history
	 *
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @param origin filter by origin (optional)
	 * @param offerId filter by offer_id (optional)
	 * @param limit maximum number of items in a list (optional)
	 * @param before cursor that points to the start of the page of data that has been returned (optional)
	 * @param after cursor that points to the end of the page of data that has been returned (optional)
	 * @return ApiResponse&lt;OrderList&gt;
	 * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
	 */
	public ApiResponse<OrderList> getHistoryWithHttpInfo(String X_REQUEST_ID, String origin, String offerId,
		Integer limit, String before,
		String after) throws ApiException {
		Call call = getHistoryValidateBeforeCall(X_REQUEST_ID, origin, offerId, limit, before, after);
		Type localVarReturnType = new TypeToken<OrderList>() {
		}.getType();
		return apiClient.execute(call, localVarReturnType);
	}

	/**
	 * get user order history (asynchronously) get user order history
	 *
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @param origin filter by origin (optional)
	 * @param offerId filter by offer_id (optional)
	 * @param limit maximum number of items in a list (optional)
	 * @param before cursor that points to the start of the page of data that has been returned (optional)
	 * @param after cursor that points to the end of the page of data that has been returned (optional)
	 * @param callback The callback to be executed when the API call finishes
	 * @return The request call
	 * @throws ApiException If fail to process the API call, e.g. serializing the request body object
	 */
	public Call getHistoryAsync(String X_REQUEST_ID, String origin, String offerId, Integer limit, String before,
		String after,
		final ApiCallback<OrderList> callback) throws ApiException {

		Call call = getHistoryValidateBeforeCall(X_REQUEST_ID, origin, offerId, limit, before, after);
		Type localVarReturnType = new TypeToken<OrderList>() {
		}.getType();
		apiClient.executeAsync(call, localVarReturnType, callback);
		return call;
	}

	/**
	 * Build call for getOrder
	 *
	 * @param orderId The order id (required)
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @return Call to execute
	 * @throws ApiException If fail to serialize the request body object
	 */
	public Call getOrderCall(String orderId, String X_REQUEST_ID) throws ApiException {
		Object localVarPostBody = null;

		// create path and map variables
		String localVarPath = "/orders/{order_id}"
			.replaceAll("\\{" + "order_id" + "\\}", apiClient.escapeString(orderId.toString()));

		List<Pair> localVarQueryParams = new ArrayList<Pair>();
		List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

		Map<String, String> localVarHeaderParams = new HashMap<String, String>();
		if (X_REQUEST_ID != null) {
			localVarHeaderParams.put("X-REQUEST-ID", apiClient.parameterToString(X_REQUEST_ID));
		}

		Map<String, Object> localVarFormParams = new HashMap<String, Object>();

		final String[] localVarAccepts = {
			"application/json", "application/json"
		};
		final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
		if (localVarAccept != null) {
			localVarHeaderParams.put("Accept", localVarAccept);
		}

		final String[] localVarContentTypes = {

		};
		final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
		localVarHeaderParams.put("Content-Type", localVarContentType);

		String[] localVarAuthNames = new String[]{};
		return apiClient
			.buildCall(localVarPath, GET, localVarQueryParams, localVarCollectionQueryParams, localVarPostBody,
				localVarHeaderParams, localVarFormParams, localVarAuthNames);
	}

	@SuppressWarnings("rawtypes")
	private Call getOrderValidateBeforeCall(String orderId, String X_REQUEST_ID) throws ApiException {

		// verify the required parameter 'orderId' is set
		if (orderId == null) {
			throw new ApiException("Missing the required parameter 'orderId' when calling getOrder(Async)");
		}

		// verify the required parameter 'X_REQUEST_ID' is set
		if (X_REQUEST_ID == null) {
			throw new ApiException("Missing the required parameter 'X_REQUEST_ID' when calling getOrder(Async)");
		}

		Call call = getOrderCall(orderId, X_REQUEST_ID);
		return call;
	}

	/**
	 * get an order get an order
	 *
	 * @param orderId The order id (required)
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @return Order
	 * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
	 */
	public Order getOrder(String orderId, String X_REQUEST_ID) throws ApiException {
		ApiResponse<Order> resp = getOrderWithHttpInfo(orderId, X_REQUEST_ID);
		return resp.getData();
	}

	/**
	 * get an order get an order
	 *
	 * @param orderId The order id (required)
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @return ApiResponse&lt;Order&gt;
	 * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
	 */
	public ApiResponse<Order> getOrderWithHttpInfo(String orderId, String X_REQUEST_ID) throws ApiException {
		Call call = getOrderValidateBeforeCall(orderId, X_REQUEST_ID);
		Type localVarReturnType = new TypeToken<Order>() {
		}.getType();
		return apiClient.execute(call, localVarReturnType);
	}

	/**
	 * get an order (asynchronously) get an order
	 *
	 * @param orderId The order id (required)
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @param callback The callback to be executed when the API call finishes
	 * @return The request call
	 * @throws ApiException If fail to process the API call, e.g. serializing the request body object
	 */
	public Call getOrderAsync(String orderId, String X_REQUEST_ID, final ApiCallback<Order> callback)
		throws ApiException {

		Call call = getOrderValidateBeforeCall(orderId, X_REQUEST_ID);
		Type localVarReturnType = new TypeToken<Order>() {
		}.getType();
		apiClient.executeAsync(call, localVarReturnType, callback);
		return call;
	}

	/**
	 * Build call for submitOrder
	 *
	 * @param earnsubmission (required)
	 * @param orderId The order id (required)
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @return Call to execute
	 * @throws ApiException If fail to serialize the request body object
	 */
	public Call submitOrderCall(EarnSubmission earnsubmission, String orderId, String X_REQUEST_ID)
		throws ApiException {
		Object localVarPostBody = earnsubmission;

		// create path and map variables
		String localVarPath = "/orders/{order_id}"
			.replaceAll("\\{" + "order_id" + "\\}", apiClient.escapeString(orderId.toString()));

		List<Pair> localVarQueryParams = new ArrayList<Pair>();
		List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

		Map<String, String> localVarHeaderParams = new HashMap<String, String>();
		if (X_REQUEST_ID != null) {
			localVarHeaderParams.put("X-REQUEST-ID", apiClient.parameterToString(X_REQUEST_ID));
		}

		Map<String, Object> localVarFormParams = new HashMap<String, Object>();

		final String[] localVarAccepts = {
			"application/json", "application/json", "application/json"
		};
		final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
		if (localVarAccept != null) {
			localVarHeaderParams.put("Accept", localVarAccept);
		}

		final String[] localVarContentTypes = {
			"application/json"
		};
		final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
		localVarHeaderParams.put("Content-Type", localVarContentType);

		String[] localVarAuthNames = new String[]{};
		return apiClient
			.buildCall(localVarPath, POST, localVarQueryParams, localVarCollectionQueryParams, localVarPostBody,
				localVarHeaderParams, localVarFormParams, localVarAuthNames);
	}

	@SuppressWarnings("rawtypes")
	private Call submitOrderValidateBeforeCall(EarnSubmission earnsubmission, String orderId, String X_REQUEST_ID)
		throws ApiException {

		// verify the required parameter 'earnsubmission' is set
		if (earnsubmission == null) {
			throw new ApiException("Missing the required parameter 'earnsubmission' when calling submitOrder(Async)");
		}

		// verify the required parameter 'orderId' is set
		if (orderId == null) {
			throw new ApiException("Missing the required parameter 'orderId' when calling submitOrder(Async)");
		}

		// verify the required parameter 'X_REQUEST_ID' is set
		if (X_REQUEST_ID == null) {
			throw new ApiException("Missing the required parameter 'X_REQUEST_ID' when calling submitOrder(Async)");
		}

		Call call = submitOrderCall(earnsubmission, orderId, X_REQUEST_ID);
		return call;


	}

	/**
	 * submit an order submit an order
	 *
	 * @param earnsubmission (required)
	 * @param orderId The order id (required)
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @return Order
	 * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
	 */
	public Order submitOrder(EarnSubmission earnsubmission, String orderId, String X_REQUEST_ID) throws ApiException {
		ApiResponse<Order> resp = submitOrderWithHttpInfo(earnsubmission, orderId, X_REQUEST_ID);
		return resp.getData();
	}

	/**
	 * submit an order submit an order
	 *
	 * @param earnsubmission (required)
	 * @param orderId The order id (required)
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @return ApiResponse&lt;Order&gt;
	 * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
	 */
	public ApiResponse<Order> submitOrderWithHttpInfo(EarnSubmission earnsubmission, String orderId,
		String X_REQUEST_ID) throws ApiException {
		Call call = submitOrderValidateBeforeCall(earnsubmission, orderId, X_REQUEST_ID);
		Type localVarReturnType = new TypeToken<Order>() {
		}.getType();
		return apiClient.execute(call, localVarReturnType);
	}

	/**
	 * submit an order (asynchronously) submit an order
	 *
	 * @param earnsubmission (required)
	 * @param orderId The order id (required)
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @param callback The callback to be executed when the API call finishes
	 * @return The request call
	 * @throws ApiException If fail to process the API call, e.g. serializing the request body object
	 */
	public Call submitOrderAsync(EarnSubmission earnsubmission, String orderId, String X_REQUEST_ID,
		final ApiCallback<Order> callback) throws ApiException {
		Call call = submitOrderValidateBeforeCall(earnsubmission, orderId, X_REQUEST_ID);
		Type localVarReturnType = new TypeToken<Order>() {
		}.getType();
		apiClient.executeAsync(call, localVarReturnType, callback);
		return call;
	}

}
