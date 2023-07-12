package com.workfusion.academy.task;

import com.workfusion.odf2.compiler.BotTask;
import com.workfusion.odf2.core.cdi.Injector;
import com.workfusion.odf2.core.cdi.Requires;
import com.workfusion.odf2.core.settings.Configuration;
import com.workfusion.odf2.core.task.TaskInput;
import com.workfusion.odf2.core.task.generic.GenericTask;
import com.workfusion.odf2.core.task.output.TaskRunnerOutput;
import com.workfusion.odf2.service.ControlTowerServicesModule;
import com.workfusion.odf2.service.s3.S3Bucket;
import com.workfusion.odf2.service.s3.S3Service;
import org.slf4j.Logger;

import javax.inject.Inject;

@BotTask
@Requires(ControlTowerServicesModule.class)
public class MLInputTask implements GenericTask {

    private final Logger logger;
    private final TaskInput taskInput;
    private final Configuration configuration;
    private final S3Service s3Service;
    private static final String MODEL_ID = "trained.model.id";
    private static final String DOCUMENT_XML_LINK = "document_xml_link";

    @Inject
    public MLInputTask(Injector injector) {
        this.logger = injector.instance(Logger.class);
        this.taskInput = injector.instance(TaskInput.class);
        this.configuration = injector.instance(Configuration.class);
        this.s3Service = injector.instance(S3Service.class);
    }

    @Override
    public TaskRunnerOutput run() {

        String document = new String(s3Service.getObjectByUrl(taskInput.getRequiredVariable(DOCUMENT_XML_LINK)));
        String modelId = configuration.getRequiredProperty(MODEL_ID);

        return taskInput.asResult()
                .withColumn("model_id", modelId)
                .withColumn("document", document);
    }

}