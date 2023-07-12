package com.workfusion.academy.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.workfusion.odf2.core.orm.Datastore;
import com.workfusion.odf2.core.orm.DatastoreType;
import com.workfusion.odf2.transaction.model.OdfTransactionalEntity;
import com.workfusion.odf2.transaction.repository.TransactionalEntityRepository;
import lombok.*;

import java.util.UUID;

@DatabaseTable(tableName = "entity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Datastore(type = DatastoreType.NON_VERSIONED)
public class Entity{

    public static final String ENTITY_ID_COLUMN = "entity_id";
    public static final String NAME_COLUMN = "name";
    public static final String BP_RUN_UUID_COLUMN = "bp_run_id";

    @DatabaseField(
            columnName = ENTITY_ID_COLUMN,
            dataType = DataType.UUID,
            generatedId = true)
    private UUID entityId;
    @DatabaseField(columnName = BP_RUN_UUID_COLUMN, dataType = DataType.UUID)
    private UUID bpRunId;
    @DatabaseField(columnName = NAME_COLUMN, dataType = DataType.STRING)
    private String name;

}
