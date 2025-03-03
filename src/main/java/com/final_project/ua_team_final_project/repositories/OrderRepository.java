package com.final_project.ua_team_final_project.repositories;

import com.final_project.ua_team_final_project.models.Department;
import com.final_project.ua_team_final_project.models.Order;
import com.final_project.ua_team_final_project.models.OrderStatus;
import com.final_project.ua_team_final_project.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByDept_DeptIdAndStatus_StatusIdIn(Long deptId, List<Long> statusIds);
    List<Order> findByApprovedByHeadTrueAndApprovedByFinDeptTrueAndStatusStatusId(Long id);

    Page<Order> findByApprovedByHeadAndApprovedByFinDeptAndStatus(
            boolean approvedByHead, boolean approvedByFinDept, OrderStatus status, Pageable pageable);

    Page<Order> findByApprovedByHeadAndApprovedByFinDeptAndStatusNot(
            boolean approvedByHead, boolean approvedByFinDept, OrderStatus status, Pageable pageable);

    Page<Order> findByUserAndStatusNot(User user, OrderStatus status, Pageable pageable);
    Page<Order> findByDept(Department dept, Pageable pageable);
    Page<Order> findByStatusIn(List<OrderStatus> statusIds, Pageable pageable);
}
