package com.casestudy.api.service;

import org.springframework.http.ResponseEntity;

public interface NotifyService {
    ResponseEntity<String> orderNotify();

    ResponseEntity<String> orderNotifyNew();
}
