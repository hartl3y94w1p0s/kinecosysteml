/*
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


package kin.devplatform.core.network;

import java.util.List;
import java.util.Map;
import kin.devplatform.core.network.model.Error;

public class ApiException extends Exception {

	private static int CODE_INTERNAL = 1;
	private static String MSG_INTERNAL = "internal error";

	private int code = 0;
	private Map<String, List<String>> responseHeaders = null;
	private Error responseBody = null;

	public ApiException() {
		this.code = CODE_INTERNAL;
		this.responseBody = new Error(MSG_INTERNAL, MSG_INTERNAL, CODE_INTERNAL);

	}

	public ApiException(Throwable throwable) {
		super(throwable);
	}

	public ApiException(String message) {
		super(message);
	}

	public ApiException(String message, Throwable throwable, int code, Map<String, List<String>> responseHeaders,
		Error responseBody) {
		super(message, throwable);
		this.code = code;
		this.responseHeaders = responseHeaders;
		this.responseBody = responseBody;
	}

	public ApiException(String message, int code, Map<String, List<String>> responseHeaders, Error responseBody) {
		this(message, (Throwable) null, code, responseHeaders, responseBody);
	}

	public ApiException(String message, Throwable throwable, int code, Map<String, List<String>> responseHeaders) {
		this(message, throwable, code, responseHeaders, null);
	}

	public ApiException(int code, Map<String, List<String>> responseHeaders, Error responseBody) {
		this((String) null, (Throwable) null, code, responseHeaders, responseBody);
	}

	public ApiException(int code, String message) {
		super(message);
		this.code = code;
	}

	public ApiException(int code, Throwable throwable) {
		super(throwable);
		this.code = code;
	}

	public ApiException(int code, String message, Map<String, List<String>> responseHeaders, Error responseBody) {
		this(code, message);
		this.responseHeaders = responseHeaders;
		this.responseBody = responseBody;
	}

	/**
	 * Get the HTTP status code.
	 *
	 * @return HTTP status code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Get the HTTP response headers.
	 *
	 * @return A map of list of string
	 */
	public Map<String, List<String>> getResponseHeaders() {
		return responseHeaders;
	}

	/**
	 * Get the HTTP response body.
	 *
	 * @return KinCallback body in the form of string
	 */
	public Error getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(Error responseBody) {
		this.responseBody = responseBody;
	}
}
