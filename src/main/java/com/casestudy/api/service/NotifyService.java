package com.casestudy.api.service;

import com.casestudy.api.model.Ordered;
import org.springframework.http.ResponseEntity;

public interface NotifyService {
    ResponseEntity<String> notify(Ordered ordered);
}
