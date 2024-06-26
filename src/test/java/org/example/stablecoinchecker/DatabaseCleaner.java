package org.example.stablecoinchecker;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
public class DatabaseCleaner {

    @Autowired
    private EntityManager em;

    @Transactional
    public void truncate() {
        List<String> tableNames = em.createNativeQuery("SHOW TABLES;", String.class).getResultList();
        em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE;").executeUpdate();
        for (String tableName : tableNames) {
            em.createNativeQuery("TRUNCATE TABLE " + tableName+" RESTART IDENTITY;").executeUpdate();
        }
        em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE;").executeUpdate();
    }
}
