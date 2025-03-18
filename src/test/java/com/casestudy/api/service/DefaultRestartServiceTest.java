package com.casestudy.api.service;

import com.casestudy.api.OrderApplication;
import com.casestudy.api.service.impl.DefaultRestartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;

import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext
class DefaultRestartServiceTest {

    @Autowired
    private DefaultRestartService restartService;

    @Test
    void restart_shouldRestartApplication() {
        ConfigurableApplicationContext context = mock(ConfigurableApplicationContext.class);
        ApplicationArguments args = mock(ApplicationArguments.class);
        OrderApplication.setContext(context);

        when(context.getBean(ApplicationArguments.class)).thenReturn(args);
        when(args.getSourceArgs()).thenReturn(new String[]{});

        restartService.restart();

        verify(context).close();
        verify(context, times(1)).getBean(ApplicationArguments.class);
    }
}