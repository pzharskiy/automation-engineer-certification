package com.workfusion.academy.task;


import com.workfusion.academy.model.Entity;
import com.workfusion.academy.module.RepositoryModule;
import com.workfusion.academy.repository.EntityRepository;
import com.workfusion.academy.rpa.client.OrangeHrmClient;
import com.workfusion.academy.rpa.pages.EmployeeListPage;
import com.workfusion.academy.service.HrmFileGenerator;
import com.workfusion.automation.rpa.driver.DriverType;
import com.workfusion.automation.rpa.utils.ScreenshotUtils;
import com.workfusion.bot.service.SecureEntryDTO;
import com.workfusion.odf2.compiler.BotTask;
import com.workfusion.odf2.core.cdi.Injector;
import com.workfusion.odf2.core.cdi.Requires;
import com.workfusion.odf2.core.settings.Configuration;
import com.workfusion.odf2.core.task.TaskInput;
import com.workfusion.odf2.core.task.generic.GenericTask;
import com.workfusion.odf2.core.task.output.TaskRunnerOutput;
import com.workfusion.odf2.core.webharvest.BindingReader;
import com.workfusion.odf2.core.webharvest.rpa.RpaDriver;
import com.workfusion.odf2.core.webharvest.rpa.RpaFactory;
import com.workfusion.odf2.core.webharvest.rpa.RpaRunner;
import com.workfusion.odf2.service.ControlTowerServicesModule;
import com.workfusion.odf2.service.s3.S3Service;
import com.workfusion.odf2.service.vault.SecretsVaultService;
import com.workfusion.rpa.helpers.RPA;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@BotTask(requireRpa = true)
@Requires({ControlTowerServicesModule.class, RepositoryModule.class})
public class ImportToOrangeHrmTask implements GenericTask {

    private final Logger logger;
    private final TaskInput taskInput;
    private final Configuration configuration;
    private final BindingReader bindingReader;
    private final EntityRepository entityRepository;
    private final RpaRunner rpaRunner;
    private final S3Service s3Service;
    private final SecretsVaultService secretsVault;
    private final String BP_RUN_UUID_COLUMN_NAME = "bp_run_id";
    private final String AUTOMATION_ACADEMY_ID = "automation.academy.id";
    private final String ODF_S3_BUCKET_NAME = "odf.ocr.s3.bucket";
    private final String HRM_CREDENTIALS_ALIAS = "pz.hrm.credentials";
    private final String PNG_POSTFIX = ".png";

    @Inject
    public ImportToOrangeHrmTask(Injector injector) {
        this.logger = injector.instance(Logger.class);
        this.taskInput = injector.instance(TaskInput.class);
        this.configuration = injector.instance(Configuration.class);
        this.bindingReader = injector.instance(BindingReader.class);
        this.entityRepository = injector.instance(EntityRepository.class);
        this.s3Service = injector.instance(S3Service.class);
        this.secretsVault = injector.instance(SecretsVaultService.class);
        RpaFactory rpaFactory = injector.instance(RpaFactory.class);
        this.rpaRunner = rpaFactory.builder(RpaDriver.UNIVERSAL)
                .closeOnCompletion(true)
                .maximizeOnStartup(true)
                .build();
    }

    @Override
    public TaskRunnerOutput run() {

        List<Entity> employees;
        String automationAcademyId = configuration.getRequiredProperty(AUTOMATION_ACADEMY_ID);
        AtomicReference<String> screenshotPath = new AtomicReference<>();
        SecureEntryDTO secrets = secretsVault.getEntry(HRM_CREDENTIALS_ALIAS);

        try {
            employees = entityRepository
                    .getAllByBpRunId(UUID.fromString(taskInput.getRequiredVariable(BP_RUN_UUID_COLUMN_NAME)));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        HrmFileGenerator hrmFileGenerator = new HrmFileGenerator();
        hrmFileGenerator.setAutomationAcademyId(automationAcademyId);
        String pathToFile = hrmFileGenerator.generateFile(employees);
        System.out.println(pathToFile);

        rpaRunner.execute(d -> {
            OrangeHrmClient orangeHrmClient = new OrangeHrmClient(logger);
            EmployeeListPage page = orangeHrmClient
                    .getLoginPage()
                    .username(secrets.getKey())
                    .password(secrets.getValue())
                    .login()
                    .topNavigation()
                    .importPage()
                    .upload(pathToFile)
                    .topNavigation()
                    .employeeListPage()
                    .id(automationAcademyId)
                    .search()
                    .last();

            try {
                ScreenshotUtils screenshotUtils = new ScreenshotUtils();
                screenshotUtils.getScreenshotAsByteArray();
                screenshotPath.set(
                        s3Service.getBucket(configuration.getRequiredProperty(ODF_S3_BUCKET_NAME))
                                .put(screenshotUtils.getScreenshotAsByteArray(),
                                        configuration.getRequiredProperty(AUTOMATION_ACADEMY_ID) + "/" + UUID.randomUUID() + PNG_POSTFIX)
                                .getDirectUrl()
                );

            } catch (AWTException | IOException e) {
                throw new RuntimeException(e);
            }

            page.topNavigation()
                    .logout();


            RPA.driver().switchDriver(DriverType.DESKTOP.getDriverType());
            RPA.deleteFileOnAgent(pathToFile);

        });

        return taskInput.asResult()
                .withColumn("screenshot_url", screenshotPath.get());
    }

}
