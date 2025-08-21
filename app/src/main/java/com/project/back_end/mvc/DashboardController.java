package com.project.back_end.mvc;

import com.project.back_end.models.*;
import com.project.back_end.repo.*;
import com.project.back_end.services.*;
import com.project.back_end.DTO.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

// 1. Set Up the MVC Controller Class:
//    - Annotate the class with `@Controller` to indicate that it serves as an MVC controller returning view names (not JSON).
//    - This class handles routing to admin and doctor dashboard pages based on token validation.


// 2. Autowire the Shared Service:
//    - Inject the common `Service` class, which provides the token validation logic used to authorize access to dashboards.


// 3. Define the `adminDashboard` Method:
//    - Handles HTTP GET requests to `/adminDashboard/{token}`.
//    - Accepts an admin's token as a path variable.
//    - Validates the token using the shared service for the `"admin"` role.
//    - If the token is valid (i.e., no errors returned), forwards the user to the `"admin/adminDashboard"` view.
//    - If invalid, redirects to the root URL, likely the login or home page.


// 4. Define the `doctorDashboard` Method:
//    - Handles HTTP GET requests to `/doctorDashboard/{token}`.
//    - Accepts a doctor's token as a path variable.
//    - Validates the token using the shared service for the `"doctor"` role.
//    - If the token is valid, forwards the user to the `"doctor/doctorDashboard"` view.
//    - If the token is invalid, redirects to the root URL.

private final MainService service;

    @Autowired
    public DashboardController(MainService service) {
        this.service = service;
    }

    @GetMapping("/adminDashboard/{token}")
    public ModelAndView adminDashboard(@PathVariable("token") String token) {
        Map<String, Object> validationResult = service.validateToken(token, "admin");

        if (validationResult.isEmpty()) {
            return "admin/adminDashboard";
        } else {
            return "redirect:/login";
        }
    }
    @GetMapping("/doctorDashboard/{token}")
    public String doctorDashboard(@PathVariable("token") String token) {
        Map<String, Object> validationResult = service.validateToken(token, "doctor");

        if (validationResult.isEmpty()) {
            return "doctor/doctorDashboard";
        } else {
            // Token invalid → redirect to login page
            return "redirect:/login";
        }
    }
}
