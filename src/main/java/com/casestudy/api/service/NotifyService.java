package com.casestudy.api.service;

import com.casestudy.api.model.Ordered;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface NotifyService {
    ResponseEntity<String> notify(Ordered ordered);
}
