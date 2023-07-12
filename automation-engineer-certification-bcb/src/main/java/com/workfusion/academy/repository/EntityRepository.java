package com.workfusion.academy.repository;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.workfusion.academy.model.Entity;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class EntityRepository {

    private final Dao<Entity, String> dao;

    public EntityRepository(ConnectionSource connectionSource) throws SQLException {
        dao = DaoManager.createDao(connectionSource, Entity.class);
    }

    public void create(Entity entity) throws SQLException {
        dao.create(entity);
    }

    public List<Entity> getAllByBpRunId(UUID bpRunId) throws SQLException {
        return dao.queryForEq(Entity.BP_RUN_UUID_COLUMN,bpRunId);
    }
}
