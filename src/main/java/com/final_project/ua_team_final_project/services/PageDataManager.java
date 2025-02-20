package com.final_project.ua_team_final_project.services;

import com.final_project.ua_team_final_project.models.*;
import com.final_project.ua_team_final_project.repositories.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Component
@Service
public class PageDataManager {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final RoleRepository roleRepository;
    private final OrderRepository orderRepository;
    private final OrderStatusRepository orderStatusRepository;
    private AvailableProductsRepository availableProductsRepository;

    public PageDataManager(UserRepository userRepository, DepartmentRepository departmentRepository,
                           RoleRepository roleRepository, OrderRepository orderRepository, OrderStatusRepository orderStatusRepository, AvailableProductsRepository availableProductsRepository) {

        this.roleRepository = roleRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.availableProductsRepository = availableProductsRepository;

    }

    public void setAdminModel(Model model, Integer urlPageNumber, Integer pageSize, String order, User user) {

        int pageNumber = urlPageNumber - 1;

        Sort.Direction direction = Sort.Direction.ASC;

        if (order.endsWith("desc")) {
            direction = Sort.Direction.DESC;
            order = order.substring(0, order.length() - 5);
        }
        Sort sort = Sort.by(direction, order);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<User> page = null;
        try {
            page = userRepository.findAll(pageable);
        } catch (PropertyReferenceException e) {
            setAdminModel(model, 1, 10, "userId", user);
        }
        if (page != null) {
            model.addAttribute("user", user);
            model.addAttribute("users", page.getContent());
            model.addAttribute("pageNumber", urlPageNumber);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("order", sort.toString());
        }
    }

    public void setEditUserModel(Long id, Model model, User user) {
        model.addAttribute("user", user);
        model.addAttribute("edituser", userRepository.findById(id).orElse(null));
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("roles", roleRepository.findAll());
    }

    public void setFincoModel(Model model, Integer urlPageNumber, Integer pageSize, String order, User user) {

        int pageNumber = urlPageNumber - 1;

        Sort.Direction direction = Sort.Direction.ASC;

        if (order.endsWith("desc")) {
            direction = Sort.Direction.DESC;
            order = order.substring(0, order.length() - 5);
        }
        Sort sort = Sort.by(direction, order);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Order> page = null;
        try {
            page = orderRepository.findByApprovedByHeadAndApprovedByFinDeptAndStatusNot(true,
                    false, orderStatusRepository.findById(4L).orElse(null), pageable);
        } catch (PropertyReferenceException e) {
            setFincoModel(model, 1, 10, "orderId", user);
        }
        model.addAttribute("user", user);
        model.addAttribute("pageNumber", urlPageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("orders", page.getContent());
        model.addAttribute("order", sort.toString());
    }

    public void setSupplierModel(Model model, User user) {
        model.addAttribute("user", user);
        List<Order> orders = orderRepository.findByApprovedByHeadTrueAndApprovedByFinDeptTrue();
        model.addAttribute("orders", orders);
        model.addAttribute("department", user.getDepartment().getName());
    }


    public void getAvailableProductsModel(Model model, Integer urlPageNumber, Integer pageSize, String order, User user) {
        int pageNumber = urlPageNumber - 1;
        Sort.Direction direction = Sort.Direction.ASC;
        if (order.endsWith("desc")) {
            direction = Sort.Direction.DESC;
            order = order.substring(0, order.length() - 5);
        }
        Sort sort = Sort.by(direction, order);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<AvailableProducts> page = null;
        try {
            page = availableProductsRepository.findAll(pageable);
        } catch (PropertyReferenceException e) {
            getAvailableProductsModel(model, 1, 10, "productCode", user);
        }
        model.addAttribute("user", user);
        model.addAttribute("pageNumber", urlPageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("order", sort.toString());
        model.addAttribute("availableProducts", page.getContent());
    }
    public void setNewUserModel(Model model, User user) {
        model.addAttribute("user", user);
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("roles", roleRepository.findAll());

    }

    public void getAvailableProductsModel(Model model, Integer urlPageNumber, Integer pageSize, String order, User user) {

        int pageNumber = urlPageNumber - 1;

        Sort.Direction direction = Sort.Direction.ASC;

        if (order.endsWith("desc")) {
            direction = Sort.Direction.DESC;
            order = order.substring(0, order.length() - 5);
        }
        Sort sort = Sort.by(direction, order);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<AvailableProducts> page = null;
        try {
            page = availableProductsRepository.findAll(pageable);
        } catch (PropertyReferenceException e) {
            getAvailableProductsModel(model, 1, 10, "productCode", user);
        }

        model.addAttribute("user", user);
        model.addAttribute("pageNumber", urlPageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("order", sort.toString());
        model.addAttribute("availableProducts", page.getContent());
    }
}