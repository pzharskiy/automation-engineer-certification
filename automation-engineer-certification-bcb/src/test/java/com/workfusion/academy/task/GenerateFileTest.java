package com.workfusion.academy.task;

import com.workfusion.odf.test.junit.IacDeveloperJUnitConfig;
import com.workfusion.odf2.core.settings.ConfigEntity;
import com.workfusion.odf2.junit.BotTaskFactory;
import com.workfusion.odf2.junit.OrmSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;

@IacDeveloperJUnitConfig
public class GenerateFileTest {

    private static final String RECEIVER_EMAIL = "paulzharskiy@gmail.com";
    private static final String EMAIL_THEME = "[Code RPA Level 2 of Difficulty] Generated Report";
    private static final String ODF_BUCKET_NAME = "doc-upload";
    private static final String S3_KEY_NAME = "36776";
    private static final String PATH_TO_DOWNLOAD = "C:\\temp\\";
    private final static String YAHOO_USERNAME = "***";
    private final static String YAHOO_PASSWORD = "***";


    @BeforeEach
    void setUp(OrmSupport ormSupport) {

        ormSupport.createTables(ConfigEntity.class);

        Collection<ConfigEntity> cfg = new ArrayList<ConfigEntity>() {{
            add(new ConfigEntity("automation.academy.id", "11111"));//36776
            add(new ConfigEntity("hrm.credentials.alias", "pz.hrm.credentials"));
            add(new ConfigEntity("odf.s3.bucket.name", "doc-upload"));
        }};

        ormSupport.getConfigRepository().createAll(cfg);
    }


    @Test
    @Timeout(1000)
    @DisplayName("Should complete all scenario")
    void shouldRunRpaBotTask(BotTaskFactory botTaskFactory) {

        botTaskFactory.fromClass(ImportToOrangeHrmTask.class)
                .withSecureEntries(cfg -> cfg
                        .withEntry("pz.hrm.credentials", "forrest.gump", "work4Workfusion!"))
                .withS3(cfg -> cfg.withAccessKey("admin").withSecretKey("admin123").withEndpointUrl("http://localhost:15110"))
                .withTimeout(Duration.ofSeconds(1000))
                .buildAndRun();
    }
}