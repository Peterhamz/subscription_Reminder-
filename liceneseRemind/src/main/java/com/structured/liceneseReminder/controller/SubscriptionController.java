package com.structured.liceneseReminder.controller;

import com.structured.liceneseReminder.dto.SubDto;
import com.structured.liceneseReminder.enums.Status;
import com.structured.liceneseReminder.model.Sub_reminder;
import com.structured.liceneseReminder.reposotory.SubRepository;
import com.structured.liceneseReminder.service.SubReminderService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubscriptionController {

    @Autowired
    private SubReminderService subReminderService;
    @Autowired
    private SubRepository subRepository;

    int pageSize;

    //    Handler method to handle the list of sub request and return model and view
    @GetMapping("/subscriptions")
    private String listSub(Model model) {

        model.addAttribute("activeCount", subRepository.countByStatus(Status.ACTIVE));

    return allPaginated(1, model);
    }

    //get list of active subscription
    @GetMapping("/activeSub")
    private String activeSub(Model model) {
        return activePaginated(1, model);
    }

    //get list of pending subscription
    @GetMapping("/pendingSub")
    private String pendingSub(Model model) {
        return pendingPaginated(1, model);
    }
    //get overdue/expired subscription
    @GetMapping("/overdueSub")
    private String overdueSub(Model model) {
        return overduePaginated(1, model);
    }
    @GetMapping("/sub/new")
    public String createSubForm(Model model){
    //      create sub object to hold sub form data
        SubDto subDto = new SubDto();
        model.addAttribute("subReminder", subDto);
        return "createSub";
    }
    @PostMapping("/subscriptions")
    public String saveSubscription(@ModelAttribute("subReminder") SubDto subDto) throws SchedulerException, InterruptedException {
        subReminderService.createReminder(subDto);
        return "redirect:/subscriptions";
    }
    @GetMapping("/page/{pageNumber}")
    public String allPaginated(@PathVariable(value = "pageNumber") int pageNumber, Model model){
        int pageSize = 5;

        Pageable pageable = PageRequest.of(pageNumber -1, pageSize);
        Page<Sub_reminder> page = subRepository.getInfo(pageable);
        List<Sub_reminder> listSubscription = page.getContent();

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listSubscription", listSubscription);

        return "allPaginated_index";
    }
    @GetMapping("/pageActive/{pageNumber}")
    public String activePaginated(@PathVariable(value = "pageNumber") int pageNumber, Model model){
        pageSize = 5;

        Pageable pageable = PageRequest.of(pageNumber -1, pageSize);
        Page<Sub_reminder> page = subRepository.getActiveInfo(pageable);
        List<Sub_reminder> listSubscription = page.getContent();

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listSubscription", listSubscription);

        return "activePaginated_index";
    }
    @GetMapping("/pagePending/{pageNumber}")
    public String pendingPaginated(@PathVariable(value = "pageNumber") int pageNumber, Model model){
        pageSize = 5;

        Pageable pageable = PageRequest.of(pageNumber -1, pageSize);
        Page<Sub_reminder> page = subRepository.getPendingInfo(pageable);
        List<Sub_reminder> listSubscription = page.getContent();

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listSubscription", listSubscription);

        return "pendingPaginated_index";
    }
    @GetMapping("/pageOverdue/{pageNumber}")
    public String overduePaginated(@PathVariable(value = "pageNumber") int pageNumber, Model model){
        pageSize = 5;

        Pageable pageable = PageRequest.of(pageNumber -1, pageSize);
        Page<Sub_reminder> page = subRepository.getExpiredInfo(pageable);
        List<Sub_reminder> listSubscription = page.getContent();

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listSubscription", listSubscription);

        return "overduePaginated_index";
    }
}
