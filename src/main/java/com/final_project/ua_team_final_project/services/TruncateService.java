package com.final_project.ua_team_final_project.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TruncateService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void truncateAvailableProductsTable() {
        entityManager.createNativeQuery("TRUNCATE TABLE available_products").executeUpdate();
    }
}
