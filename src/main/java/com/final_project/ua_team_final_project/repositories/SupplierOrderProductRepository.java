package com.final_project.ua_team_final_project.repositories;

import com.final_project.ua_team_final_project.models.SupplierOrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SupplierOrderProductRepository extends JpaRepository<SupplierOrderProduct, Long> {

}
