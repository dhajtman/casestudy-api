package com.casestudy.api.http;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class VaultRequest {
    private String path;
    private Map<String, String> data;
}
