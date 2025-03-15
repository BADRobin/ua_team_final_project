package com.final_project.ua_team_final_project.controllers;

import com.final_project.ua_team_final_project.models.OrderedProduct;
import com.final_project.ua_team_final_project.models.SupplierOrder;
import com.final_project.ua_team_final_project.models.SupplierOrderProduct;
import com.final_project.ua_team_final_project.repositories.UserRepository;
import com.final_project.ua_team_final_project.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.OutputStream;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class SupplierOrderController {

    private final OrderService orderService;
    private final SupplierOrderService supplierOrderService;
    private final SupplierOrderProductService supplierOrderProductService;
    private final DigitalOceanStorageService digitalOceanStorageService;
    private final SupplierOrderStatusService supplierOrderStatusService;
    private final OrderEmailSender orderEmailSender;

    private Map<SupplierOrder, List<SupplierOrderProduct>> supplierOrders;
    private List<Long> selectedOrderIds;

    private final PageDataManager pageDataManager;
    private final UserRepository userRepository;

    @Transactional
    @PostMapping("/createSupplierOrders")
    public String createSupplierOrders(@RequestParam List<Long> selectedOrders, Model model) {

        selectedOrderIds = selectedOrders;

        Map<Long, List<OrderedProduct>> orderedProducts =
                orderService.getOrderedProductsForOrdersBySupplier(selectedOrders);
        supplierOrders = supplierOrderService.processOrders(orderedProducts);

        supplierOrders.forEach((supplierOrder, orderProducts) -> {
            SupplierOrder savedOrder = supplierOrderService.saveSupplierOrder(supplierOrder);
            if (savedOrder != null) {
                supplierOrderProductService.saveSupplierProducts(orderProducts);
                supplierOrderStatusService.changeStatusToSent(savedOrder.getSupplierOrderId());
            }
        });

        model.addAttribute("supplierOrders", supplierOrders.keySet());
        return "organization/supplierOrdersModal";
    }

    @Transactional
    @PostMapping("/sendToSuppliers")
    public String sendToSuppliers(RedirectAttributes redirectAttributes) {
        Map<String, OutputStream> files = supplierOrderService.generateScvFiles(supplierOrders);
        List<String> csvFiles = digitalOceanStorageService.uploadFiles(files);
        supplierOrders.keySet().forEach(order -> supplierOrderStatusService.changeStatusToSent(order.getSupplierOrderId()));
        selectedOrderIds.forEach(orderService::changeStatusToDone);
        orderEmailSender.sendSupplierOrder("oleh.bryl@dci-student.org", files);
        redirectAttributes.addFlashAttribute("message", "Orders sent successfully");
        return "redirect:/";
    }

    @Transactional
    @PostMapping("/cancelSupplierOrders")
    public String cancelSupplierOrders(RedirectAttributes redirectAttributes) {
        supplierOrders.keySet().forEach(order -> supplierOrderStatusService.changeStatusToCancelled(order.getSupplierOrderId()));
        redirectAttributes.addFlashAttribute("message", "Orders cancelled successfully");
        return "redirect:/";
    }

    @GetMapping("/supplyOrdersHistory")
    public String getSupplyOrdersHistory(Model model, Principal principal,
                                         @RequestParam(name = "page", required = false, defaultValue = "1") Integer urlPageNumber,
                                         @RequestParam(name = "page_size", required = false, defaultValue = "10") Integer pageSize,
                                         @RequestParam(name = "order", required = false, defaultValue = "supplierOrderId") String order) {
        pageDataManager.setSupplierHistoryModel(model,
                userRepository.findByLogin(principal.getName()).get(),
                urlPageNumber, pageSize, order);

        return "organization/supplyOrdersHistory";
    }

}

