package com.eshop.admin.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CategoryController {
    @GetMapping("/categories")
    public String categories(Model model){
        model.addAttribute("title","Category");
        return "categories";
    }
}
