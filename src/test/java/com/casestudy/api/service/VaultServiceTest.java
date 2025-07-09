package com.casestudy.api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

    class VaultServiceTest {

        @Mock
        private VaultTemplate vaultTemplate;

        @InjectMocks
        private VaultService vaultService;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
        }

        @Test
        void testWriteSecret() {
            String path = "secret/my-app";
            Map<String, String> data = Map.of("username", "admin", "password", "password123");
            Map<String, Object> requestBody = Map.of("data", data);

            VaultResponse mockResponse = new VaultResponse();
            when(vaultTemplate.write(eq(path), eq(requestBody))).thenReturn(mockResponse);

            vaultService.writeSecret(path, data);

            verify(vaultTemplate, times(1)).write(eq(path), eq(requestBody));
        }

        @Test
        void testReadSecret() {
            String path = "secret/my-app";
            Map<String, Object> expectedData = Map.of("username", "admin", "password", "password123");

            VaultResponse mockResponse = new VaultResponse();
            mockResponse.setData(expectedData);

            when(vaultTemplate.read(eq(path))).thenReturn(mockResponse);

            Map<String, Object> actualData = vaultService.readSecret(path);

            assertEquals(expectedData, actualData);
            verify(vaultTemplate, times(1)).read(eq(path));
        }

        @Test
        void testReadSecretNotFound() {
            String path = "secret/non-existent";

            when(vaultTemplate.read(eq(path))).thenReturn(null);

            Map<String, Object> actualData = vaultService.readSecret(path);

            assertNull(actualData);
            verify(vaultTemplate, times(1)).read(eq(path));
        }

        @Test
        void testDeleteSecret() {
            String path = "secret/my-app";

            // No return value for delete, just verify interaction
            vaultService.deleteSecret(path);

            verify(vaultTemplate, times(1)).delete(eq(path));
        }
    }