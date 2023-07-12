package com.workfusion.academy.module;

import com.j256.ormlite.support.ConnectionSource;
import com.workfusion.academy.repository.EntityRepository;
import com.workfusion.odf2.core.cdi.OdfModule;
import org.codejargon.feather.Provides;

import javax.inject.Singleton;
import java.sql.SQLException;

public class RepositoryModule implements OdfModule {

    @Provides
    @Singleton
    public EntityRepository entityRepository(ConnectionSource connectionSource) throws SQLException {
        return new EntityRepository(connectionSource);
    }
}
