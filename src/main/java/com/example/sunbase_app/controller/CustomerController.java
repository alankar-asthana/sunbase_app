package com.example.sunbase_app.controller;

import com.example.sunbase_app.models.CustomerRequest;
import com.example.sunbase_app.service.AuthService;
import com.example.sunbase_app.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
public class CustomerController {

    private final CustomerService customerService;
    private final AuthService authService;

    @Autowired
    public CustomerController(CustomerService customerService, AuthService authService) {
        this.customerService = customerService;
        this.authService = authService;
    }

    @GetMapping("/customer/create")
    public String showCreateCustomerForm(Model model) {
        return "create-customer"; // Return the name of the HTML page for creating a customer
    }


    @PostMapping("/customer/create")
    public String createCustomer(@ModelAttribute CustomerRequest customerRequest, RedirectAttributes redirectAttributes) {
        // Use the stored bearer token from AuthService
        String bearerToken = authService.getBearerToken();

        if (bearerToken != null) {
            // The bearer token exists; proceed to create the customer
            boolean success = customerService.createCustomer(bearerToken, customerRequest);

            if (success) {
                // Redirect to a customer-list page
                return "redirect:/customer/list"; // Replace with your success page URL
            } else {
                // Handle customer creation failure (e.g., display an error message)
                redirectAttributes.addFlashAttribute("error", "Failed to create a new customer.");
                return "redirect:/customer/create";
            }
        } else {
            // Handle the case where the bearer token is not available (authentication issue)
            redirectAttributes.addFlashAttribute("error", "Authentication token is missing.");
            return "redirect:/login"; // Redirect to the login page or handle the authentication issue appropriately
        }
    }
    @GetMapping("/customer/list")
    public String getCustomerList(Model model, HttpSession session) {
        String bearerToken = (String) session.getAttribute("bearerToken"); // Retrieve the bearer token from the session
        List<CustomerRequest> customerList = customerService.getCustomerList(bearerToken);
        model.addAttribute("customerList", customerList);
        return "customer-list"; // Return the name of the HTML page for the customer list
    }
    @GetMapping("/customer/delete/{uuid}")
    public String deleteCustomer(@PathVariable String uuid, HttpSession session) {
        String bearerToken = (String) session.getAttribute("bearerToken"); // Retrieve the bearer token from the session
        boolean success = customerService.deleteCustomer(bearerToken, uuid);

        if (success) {
            // Redirect to the customer list page after successful deletion
            return "redirect:/customer/list";
        } else {
            // Handle deletion failure (e.g., display an error message)
            // You can also redirect to an error page
            return "redirect:/customer/list";
        }
    }
    @PostMapping("/customer/edit/{uuid}")
    public String updateCustomer(
            @PathVariable String uuid,
            @ModelAttribute CustomerRequest customerRequest,
            HttpSession session
    ) {
        String bearerToken = (String) session.getAttribute("bearerToken"); // Retrieve the bearer token from the session
        boolean success = customerService.updateCustomer(bearerToken, uuid, customerRequest);

        if (success) {
            // Redirect to the customer list page after successful update
            return "redirect:/customer/list";
        } else {
            // Handle update failure (e.g., display an error message)
            // You can also redirect to an error page
            return "redirect:/customer/list";
        }
    }

}

//@Controller
//public class CustomerController {
//
//    private final CustomerService customerService;
//    private final AuthService authService;
//
//    @Autowired
//    public CustomerController(CustomerService customerService, AuthService authService) {
//        this.customerService = customerService;
//        this.authService = authService;
//    }
//
//    @GetMapping("/customer/create")
//    public String showCreateCustomerForm(Model model) {
//        // Add any necessary data to the model if needed (e.g., dropdown options)
//        return "create-customer"; // Return the name of the HTML page for creating a customer
//    }

//    @PostMapping("/customer/create")
//    public String createCustomer(@ModelAttribute CustomerRequest customerRequest, RedirectAttributes redirectAttributes) {
//        String bearerToken = authService.getBearerToken();
//
//        if (bearerToken != null) {
//            Mono<Boolean> result = customerService.createCustomer(bearerToken, customerRequest);
//
//            result.subscribe(
//                    success -> {
//                        if (success) {
//                            // Redirect to a success page or display a success message
//                            // Replace with your success page URL
//                            return "redirect:/customer/list";
//                        } else {
//                            // Handle customer creation failure (e.g., display an error message)
//                            redirectAttributes.addFlashAttribute("error", "Failed to create a new customer.");
//                            return "redirect:/customer/create";
//                        }
//                    },
//                    error -> {
//                        // Handle the case where an error occurred during the request
//                        redirectAttributes.addFlashAttribute("error", "An error occurred during the request.");
//                        return "redirect:/customer/create";
//                    }
//            );
//
//            // Return immediately without waiting for the Mono to complete
//            return "redirect:/customer/list";
//        } else {
//            // Handle the case where the bearer token is not available (authentication issue)
//            redirectAttributes.addFlashAttribute("error", "Authentication token is missing.");
//            return "redirect:/login"; // Redirect to the login page or handle the authentication issue appropriately
//        }
//    }
//@PostMapping("/customer/create")
//public String createCustomer(@ModelAttribute CustomerRequest customerRequest, RedirectAttributes redirectAttributes) {
//    String bearerToken = authService.getBearerToken();
//
//    if (bearerToken != null) {
//        Mono<Boolean> result = customerService.createCustomer(bearerToken, customerRequest);
//
//        // Use the Mono's operators to handle success and error cases
//        return result
//                .flatMap(success -> {
//                    if (success) {
//                        // Redirect to a success page or display a success message
//                        // Replace with your success page URL
//                        return Mono.just("redirect:/customer/list");
//                    } else {
//                        // Handle customer creation failure (e.g., display an error message)
//                        redirectAttributes.addFlashAttribute("error", "Failed to create a new customer.");
//                        return Mono.just("redirect:/customer/create");
//                    }
//                })
//                .onErrorResume(error -> {
//                    // Handle the case where an error occurred during the request
//                    redirectAttributes.addFlashAttribute("error", "An error occurred during the request.");
//                    return Mono.just("redirect:/customer/create");
//                })
//                .block(); // block until the Mono completes, getting the final result
//    } else {
//        // Handle the case where the bearer token is not available (authentication issue)
//        redirectAttributes.addFlashAttribute("error", "Authentication token is missing.");
//        return "redirect:/login"; // Redirect to the login page or handle the authentication issue appropriately
//    }
//}
//
//    // Implement other methods using WebClient in a similar fashion
//    @GetMapping("/customer/list")
//    public String getCustomerList(Model model, HttpSession session) {
//        String bearerToken = (String) session.getAttribute("bearerToken"); // Retrieve the bearer token from the session
//        // Use WebClient to fetch the customer list asynchronously and add it to the model
//        return "customer-list"; // Return the name of the HTML page for the customer list
//    }
//
//    @GetMapping("/customer/delete/{uuid}")
//    public String deleteCustomer(@PathVariable String uuid, HttpSession session) {
//        String bearerToken = (String) session.getAttribute("bearerToken"); // Retrieve the bearer token from the session
//        // Use WebClient to delete the customer asynchronously
//        return "redirect:/customer/list"; // Redirect to the customer list page after successful deletion
//    }
//
//    @PostMapping("/customer/update/{uuid}")
//    public String updateCustomer(
//            @PathVariable String uuid,
//            @ModelAttribute CustomerRequest customerRequest,
//            HttpSession session
//    ) {
//        String bearerToken = (String) session.getAttribute("bearerToken"); // Retrieve the bearer token from the session
//        // Use WebClient to update the customer asynchronously
//        return "redirect:/customer/list"; // Redirect to the customer list page after successful update
//    }
//}
//

