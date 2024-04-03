package com.structured.liceneseReminder.controller;

import com.structured.liceneseReminder.dto.SubDto;
import com.structured.liceneseReminder.service.SubReminderService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
@Data
public class SubscriptionController {
    private SubReminderService subReminderService;

    //    Handler method to handle the list of sub request and return model and view
    @GetMapping("/subscriptions")
    private String listStudent(Model model) {
        model.addAttribute("subscriptions", subReminderService.getAllSubscription());
        return "subscriptions";
    }

    @GetMapping("/sub/new")
    public String creatStudentForm(Model model){
    //      create sub object to hold sub form data
        SubDto subDto = new SubDto();
        model.addAttribute("subReminder", subDto);
        return "createSubscription";
    }

    @PostMapping("/subscriptions")
    public String saveStudent(@ModelAttribute("subReminder") SubDto subDto){
        subReminderService.createReminder(subDto);
        return "redirect:/subscriptions";
    }
}