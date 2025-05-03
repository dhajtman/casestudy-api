package com.casestudy.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;

import java.util.List;
import java.util.Map;

@Service
public class VaultService {

    @Autowired
    private VaultTemplate vaultTemplate;

    public void writeSecret(String path, Map<String, String> data) {
        Map<String, Object> requestBody = Map.of("data", data);

        VaultResponse response = vaultTemplate.write(path, requestBody);
        if (response == null) {
            throw new RuntimeException("Failed to write secret to Vault");
        }
    }

    public Map<String, Object> readSecret(String path) {
        List<String> list = vaultTemplate.list(path);
        VaultResponse response = vaultTemplate.read(path);
        return response != null ? response.getData() : null;
    }

    public void deleteSecret(String path) {
        vaultTemplate.delete(path);
    }
}
