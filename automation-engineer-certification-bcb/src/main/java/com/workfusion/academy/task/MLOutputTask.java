package com.workfusion.academy.task;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.workfusion.academy.model.Entity;
import com.workfusion.academy.module.RepositoryModule;
import com.workfusion.academy.repository.EntityRepository;
import com.workfusion.odf2.compiler.BotTask;
import com.workfusion.odf2.core.cdi.Injector;
import com.workfusion.odf2.core.cdi.Requires;
import com.workfusion.odf2.core.settings.Configuration;
import com.workfusion.odf2.core.task.TaskInput;
import com.workfusion.odf2.core.task.generic.GenericTask;
import com.workfusion.odf2.core.task.output.SingleResult;
import com.workfusion.odf2.core.task.output.TaskRunnerOutput;
import com.workfusion.odf2.core.webharvest.BindingReader;
import com.workfusion.odf2.service.ControlTowerServicesModule;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.UUID;

@BotTask
@Requires({ControlTowerServicesModule.class, RepositoryModule.class})
public class MLOutputTask implements GenericTask {

    private final Logger logger;
    private final TaskInput taskInput;
    private final Configuration configuration;
    private final BindingReader bindingReader;
    private final EntityRepository entityRepository;
    private final String FIELD_TO_EXTRACT = "field.to.extract";
    private final String BP_RUN_UUID_COLUMN_NAME = "bp_run_id";
    private final String HIT_SUBMISSION_COLUMN_NAME = "hit_id";

    @Inject
    public MLOutputTask(Injector injector) {
        this.logger = injector.instance(Logger.class);
        this.taskInput = injector.instance(TaskInput.class);
        this.configuration = injector.instance(Configuration.class);
        this.bindingReader = injector.instance(BindingReader.class);
        this.entityRepository = injector.instance(EntityRepository.class);
    }

    @Override
    public TaskRunnerOutput run() {

        String modelResultJson = taskInput.getRequiredVariable("model_result");
        UUID bpRunUuid = UUID.fromString(taskInput.getRequiredVariable(BP_RUN_UUID_COLUMN_NAME));
        String extractedValue = StringUtils.EMPTY;
        JsonObject jsonObject = JsonParser.parseString(modelResultJson).getAsJsonObject();
        Iterator<JsonElement> iterator = jsonObject.get("tags").getAsJsonArray().iterator();
        String field = configuration.getRequiredProperty(FIELD_TO_EXTRACT);

        while (iterator.hasNext()) {
            JsonObject jsonObj = iterator.next().getAsJsonObject();
            if (jsonObj.get("tag").getAsString().equalsIgnoreCase(field)) {
                extractedValue = jsonObj.get("text").getAsString();
                break;
            }
        }

        if (!extractedValue.isEmpty()) {
            try {
                entityRepository.create(Entity.builder().name(extractedValue).bpRunId(bpRunUuid).build());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        SingleResult output = new SingleResult();
        output.clear();
        output.setColumn(BP_RUN_UUID_COLUMN_NAME, bpRunUuid.toString());
        output.setColumn(HIT_SUBMISSION_COLUMN_NAME, bindingReader.getTaskItem().getSubmission().getAwsHitId().toString());
        return output;
    }
}