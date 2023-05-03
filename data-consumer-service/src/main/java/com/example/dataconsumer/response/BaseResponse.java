package com.example.dataconsumer.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author irfan.nagoo
 */

@RequiredArgsConstructor
@Getter
public class BaseResponse {

    private final String code;
    private final String message;
}
