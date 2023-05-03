package com.example.dataproducer.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author irfan.nagoo
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BaseResponse {

    private String code;
    private String message;
}
