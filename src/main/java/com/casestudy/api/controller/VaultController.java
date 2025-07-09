package com.casestudy.api.controller;

import com.casestudy.api.http.VaultRequest;
import com.casestudy.api.service.VaultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/vault")
public class VaultController {

    @Autowired
    private VaultService vaultService;

    @PostMapping("/write")
    public ResponseEntity<String> writeSecret(@RequestBody VaultRequest request) {
        vaultService.writeSecret(request.getPath(), request.getData());
        return ResponseEntity.ok("Secret written successfully");
    }

    @PostMapping("/read")
    public ResponseEntity<Map<String, Object>> readSecret(@RequestBody VaultRequest request) {
        Map<String, Object> secret = vaultService.readSecret(request.getPath());
        if (secret == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(secret);
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteSecret(@RequestBody VaultRequest request) {
        vaultService.deleteSecret(request.getPath());
        return ResponseEntity.ok("Secret deleted successfully");
    }
}