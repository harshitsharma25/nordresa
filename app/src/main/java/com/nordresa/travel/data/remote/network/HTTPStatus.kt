package com.nordresa.travel.data.remote.network

enum class HTTPStatus constructor(val code: Int, val message: String) {
    OK(200, "Ok"),
    CREATED(201, "Created"),
    NO_CONTENT(204, "No Content"),
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    CLIENT_ERROR(402, "server could not understand the request"),
    FORBIDDEN(403, "You are not authorised to do this action"),
    NOT_FOUND(404, "Not Found Android"),
    SERVER_ERROR(500, "Internal Server Error"), // Internal Server Error
    BAD_GATEWAY(502, "Bad Gateway"),
    SOCKET_TIMEOUT(504, "No Internet"), // Socket Timeout Exception
    ENCOUNTERED_A_SITUATION(505, "The server has encountered a situation"),
    UNEXPECTED_RESPONSE(506, "Unexpected response"),
    CONNECTION_OFF(1001, "Oops. Your connection seems off.."),
    JSON_SYNTAX_ERROR(505, "Oops. Json syntax error");

    companion object {
        fun getHTTPStatus(code: Int): HTTPStatus {
            for (httpstatus in HTTPStatus.values()) {
                if (code == httpstatus.code) {
                    return httpstatus
                }
            }
            return OK
        }
    }
}
