package com.drop_box_clone.dto.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Getter
public class BaseApiResponse {
    protected Object data;
    protected String message;

    public BaseApiResponse(Object data, String message) {
        this.data = data;
        this.message = message;
    }

    public static BaseApiResponse okResponse(Object data) {
        return new BaseApiResponse(data, "Success");
    }

    public static BaseApiResponse errorResponse(String message) {
        return new BaseApiResponse(null, message);
    }
}
