package com.final_project.ua_team_final_project.controllers;

import com.final_project.ua_team_final_project.dto.OrderRequest;
import com.final_project.ua_team_final_project.models.*;
import com.final_project.ua_team_final_project.repositories.*;
import com.final_project.ua_team_final_project.models.User;
import com.final_project.ua_team_final_project.repositories.DepartmentRepository;
import com.final_project.ua_team_final_project.repositories.RoleRepository;
import com.final_project.ua_team_final_project.repositories.UserRepository;
import com.final_project.ua_team_final_project.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.final_project.ua_team_final_project.services.PageDataManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AppController {

    @Autowired
    private PageDataManager pageDataManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DepartmentRepository deptRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AvailableProductsRepository availableProductsRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderStatusRepository orderStatusRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SupplierRepository supplierRepository;


    @GetMapping("/")
    public String index(Principal principal,
                        @RequestParam(name = "page", required = false, defaultValue = "1") Integer urlPageNumber,
                        @RequestParam(name = "page_size", required = false, defaultValue = "10") Integer pageSize,
                        @RequestParam(name = "order", required = false, defaultValue = "userId") String order,
                        @RequestParam(required = false) Long category,
                        @RequestParam(required = false) Long supplier,
                        Model model) {
        if (principal == null) {

            return "redirect:/login";
        }

        User user = userRepository.findByLogin(principal.getName()).orElseThrow(() ->
                new UsernameNotFoundException("User not found: " + principal.getName()));

        if (user == null) {
            return "redirect:/login";
        }
        Category categoryObj = (category != null) ? categoryRepository.findById(category).orElse(null) : null;
        Supplier supplierObj = (supplier != null) ? supplierRepository.findById(supplier).orElse(null) : null;


        switch (user.getRole().getName()) {
            case "ADMIN" -> {
                pageDataManager.setAdminModel(model, urlPageNumber, pageSize, order, user);
                return "organization/adminpage";
            }
            case "USER" -> {
                if (order.equals("userId")) {
                    order = "productCode";
                }

                pageDataManager.getAvailableProductsModel(model, urlPageNumber, pageSize, order, user, categoryObj, supplierObj);

                return "organization/userpage";
            }
            case "HEAD" -> {
                List<Order> orderForDept = orderRepository.findByDept_DeptIdAndStatus_StatusIdIn(
                        user.getDepartment().getDeptId(), List.of(1L));
                model.addAttribute("user", user);
                if (order.equals("userId")) {
                    order = "orderId";
                }

                model.addAttribute("orderForDept", orderForDept);
                model.addAttribute("department", user.getDepartment().getName());
                orderService.setHeadOfDepModel(model, urlPageNumber, pageSize, order, user);

                return "organization/pageOfHead";
            }
            case "FINCO" -> {
                if (order.equals("userId")) {
                    order = "orderId";
                }
                pageDataManager.setFincoModel(model, urlPageNumber, pageSize, order, user);
                return "organization/pageOfFinco";
            }
            case "SUPPLIER" -> {
                pageDataManager.setSupplierModel(model, user);
                return "organization/pageOfSupplier";
            }
            case null, default -> {
                return "accessDenied";
            }

        }
    }

    @PostMapping("/selectedProducts")
    public String selectedProducts(@RequestBody OrderRequest orderRequest,
                                   Model model,
                                   Principal principal) {

        List<Long> productIds = orderRequest.getProductIds();
        List<Integer> quantities = orderRequest.getQuantities();

        System.out.println(productIds);
        Map<Long, Integer> selectedProducts = new HashMap<>();
        for (int i = 0; i < productIds.size(); i++) {
            selectedProducts.put(productIds.get(i), quantities.get(i));
        }

        orderService.setSelectedProductsModel(selectedProducts, model);
        model.addAttribute("user", userRepository.findByLogin(principal.getName()).orElseThrow(() ->
                new UsernameNotFoundException("User not found: " + principal.getName())));

        return "organization/editProducts";
    }



    @PostMapping("/confirmOrder")
    public String confirmOrder(@RequestParam List<Long> selectedProducts,
                               @RequestParam List<Long> quantities) {
        Map<Long, Long> productQuantities = new HashMap<>();
        for (int i = 0; i < selectedProducts.size(); i++) {
            productQuantities.put(selectedProducts.get(i), quantities.get(i));

        }
        orderService.saveNewOrder(productQuantities);

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/useredit/{id}")
    public String editUser(@PathVariable Long id, Model model, Principal principal) {
        User user = userRepository.findByLogin(principal.getName()).orElseThrow(() ->
                new UsernameNotFoundException("User not found: " + principal.getName()));
        pageDataManager.setEditUserModel(id, model, user);
        return "editUser";
    }

    @PostMapping("/edituser")
    public String editUser(@RequestParam("id") Long id,
                           @RequestParam("department") String department,
                           @RequestParam("name") String name,
                           @RequestParam("role") String role,
                           @RequestParam("login") String login,
                           @RequestParam("phone") String phone,
                           @RequestParam("email") String email,
                           @RequestParam("hired_at") LocalDate created_at) {

        User user = new User();
        user.setUserId(id);
        user.setDepartment(deptRepository.findByName(department).orElse(null));
        user.setName(name);
        user.setLogin(login);
        user.setPhone(phone);
        user.setEmail(email);
        user.setCreatedAt(created_at.atStartOfDay());
        user.setRole(roleRepository.findByName(role).orElse(null));
        user.setPasswordEnc(userRepository.findById(id).orElseThrow(IllegalArgumentException::new).getPasswordEnc());

        userRepository.save(user);
        return "redirect:/";
    }

    @GetMapping("/deleteuser/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/";
    }

    @PostMapping("/approveOrders")
    public String approveOrders(@RequestParam("selectedOrders") List<Long> orders) {
        for (Long id : orders) {
            Order order = orderRepository.findById(id).orElseThrow(IllegalArgumentException::new);
            order.setApprovedByFinDept(true);
            order.setStatus(orderStatusRepository.findById(2L).orElseThrow(IllegalArgumentException::new));
            orderRepository.save(order);
        }
        return "redirect:/";
    }

    @PostMapping("/approveOrdersByHead")
    public String approveOrdersByHead(@RequestParam("selectedOrders") List<Long> orders, Principal principal) {
        User user = userRepository.findByLogin(principal.getName()).get();
        for (Long id : orders) {
            Order order = orderRepository.findById(id).orElseThrow(IllegalArgumentException::new);
            order.setApprovedByHead(true);
            order.setUser(user);
            order.setStatus(orderStatusRepository.findById(2L).orElseThrow(IllegalArgumentException::new));
            orderRepository.save(order);
        }
        return "redirect:/";
    }

    @PostMapping("/rejectOrders")
    public String rejectOrders(@RequestParam("selectedOrders") List<Long> orders) {
        for (Long id : orders) {
            Order order = orderRepository.findById(id).orElseThrow(IllegalArgumentException::new);
            order.setStatus(orderStatusRepository.findById(4L).orElseThrow(IllegalArgumentException::new));
            orderRepository.save(order);
        }
        return "redirect:/";
    }

    @PostMapping("/rejectOrdersByHead")
    public String rejectOrdersByHead(@RequestParam("selectedOrders") List<Long> orders) {
        for (Long id : orders) {
            Order order = orderRepository.findById(id).orElseThrow(IllegalArgumentException::new);
            order.setStatus(orderStatusRepository.findById(4L).orElseThrow(IllegalArgumentException::new));
            orderRepository.save(order);
        }
        return "redirect:/";
    }

    @GetMapping("/addUser")
    public String addUser(Model model, Principal principal) {
        User user = userRepository.findByLogin(principal.getName()).orElseThrow(() ->
            new UsernameNotFoundException("User not found: " + principal.getName()));
        pageDataManager.setNewUserModel(model, user);
        return "/organization/addUser";
    }

    @PostMapping("/createUser")
    public String createUser(@RequestParam("department") String department,
                             @RequestParam("name") String name,
                             @RequestParam("role") String role,
                             @RequestParam("login") String login,
                             @RequestParam("phone") String phone,
                             @RequestParam("email") String email) {
        User user = new User();
        user.setDepartment(deptRepository.findByName(department).orElse(null));
        user.setName(name);
        user.setLogin(login);
        user.setPhone(phone);
        user.setEmail(email);
        user.setRole(roleRepository.findByName(role).orElse(null));
        user.setPasswordEnc("$2a$12$7xYK.QaW9kmjU8Lbqu.cauuL7Dl4SidrO2O/2P2na8ujC0cKdHbtK");
        userRepository.save(user);
        return "redirect:/";
    }

    @GetMapping("/search")
    public String searchProducts(
            @RequestParam(name = "query", required = false, defaultValue = "") String query,
            @RequestParam(name = "page_size", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(name = "order", required = false, defaultValue = "productCode") String order,
            Model model) {

        PageRequest pageRequest = PageRequest.of(0, pageSize, Sort.by(order));

        Page<AvailableProducts> filteredProducts;
        if (query.isEmpty()) {
            filteredProducts = availableProductsRepository.findAll(pageRequest);
        } else {
            filteredProducts = availableProductsRepository.findByNameContainingIgnoreCase(query, pageRequest);
        }

        model.addAttribute("availableProducts", filteredProducts.getContent());
        return "fragments/productList :: productList";
    }

    @GetMapping("/filter")
    public String filterProducts(
            @RequestParam(required = false) Long category_id,
            @RequestParam(required = false) Long supplier_id,
            @RequestParam(defaultValue = "10") int page_size,
            @RequestParam(defaultValue = "productCode: ASC") String order,
            Model model) {

        List<AvailableProducts> filteredProducts = pageDataManager.findByCategoryAndSupplier(category_id, supplier_id, page_size, order);

        model.addAttribute("availableProducts", filteredProducts);
        return "fragments/productList :: productList";
    }

    @GetMapping("/history")
    public String history(Model model, Principal principal,
                          @RequestParam(name = "page", required = false, defaultValue = "1") Integer urlPageNumber,
                          @RequestParam(name = "page_size", required = false, defaultValue = "10") Integer pageSize,
                          @RequestParam(name = "order", required = false, defaultValue = "orderId") String order) {
        pageDataManager.setHistoryModel(model,
                userRepository.findByLogin(principal.getName()).get(),
                urlPageNumber, pageSize, order);
        return "/history";
    }

}
