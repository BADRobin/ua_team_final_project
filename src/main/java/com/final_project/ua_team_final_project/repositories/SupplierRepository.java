package com.final_project.ua_team_final_project.repositories;

import com.final_project.ua_team_final_project.models.Supplier;
import com.final_project.ua_team_final_project.models.SupplierOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Optional<Supplier> findByName(String name);
    Optional<Supplier> findBySupplierId(Long supplierId);
    Optional<Supplier> findAllByName(String name);
    Optional<Supplier> findByIban(String iban);
    Optional<Supplier> findAllByIban(String iban);
    Optional<Supplier> findByAddress(String address);
    Optional<Supplier> findAllByAddress(String address);
    Optional<Supplier> findByContactPhone(String contactPhone);
    Optional<Supplier> findAllByContactPhone(String contactPhone);
    Optional<Supplier> findByContactEmail(String contactEmail);
    Optional<Supplier> findAllByContactEmail(String contactEmail);
}
