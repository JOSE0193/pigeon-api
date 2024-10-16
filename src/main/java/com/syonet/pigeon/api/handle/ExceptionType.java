package com.syonet.pigeon.api.handle;

import lombok.Getter;

@Getter
public enum ExceptionType {

    INCOMPREHENSIBLE_MESSAGE("/incomprehensible-message", "Incomprehensible Message"),
    RESOURCE_NOT_FOUND("/resource-not-found", "Resource Not Found"),
    ENTITY_IN_USE("/entity-in-use", "Entity Is In Use"),
    BUSINESS_ERROR("/business-error", "Business Rule Error"),
    INVALID_PARAMETER("/invalid-parameter", "Invalid Parameter"),
    SYSTEM_ERROR("/system-error", "Internal System Error"),
    INVALID_DATA("/invalid-data", "Invalid Data");

    String uri;
    String title;

    ExceptionType(String path, String title) {
        this.uri = "https://jkfood.com.br" + path;
        this.title = title;
    }

}
