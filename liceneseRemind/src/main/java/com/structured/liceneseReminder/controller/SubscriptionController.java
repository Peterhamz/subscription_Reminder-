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
import org.springframework.web.bind.annotation.*;

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
        model.addAttribute("pendingCount", subRepository.countByStatus(Status.PENDING));
        model.addAttribute("overdueCount", subRepository.countByStatus(Status.EXPIRED));
    return allPaginated(1, model);
    }

    //get list of active subscription
    @GetMapping("/activeSub")
    private String activeSub(Model model) {
        model.addAttribute("activeCount", subRepository.countByStatus(Status.ACTIVE));
        model.addAttribute("pendingCount", subRepository.countByStatus(Status.PENDING));
        model.addAttribute("overdueCount", subRepository.countByStatus(Status.EXPIRED));
        return activePaginated(1, model);
    }

    //get list of pending subscription
    @GetMapping("/pendingSub")
    private String pendingSub(Model model) {
        model.addAttribute("activeCount", subRepository.countByStatus(Status.ACTIVE));
        model.addAttribute("pendingCount", subRepository.countByStatus(Status.PENDING));
        model.addAttribute("overdueCount", subRepository.countByStatus(Status.EXPIRED));
        return pendingPaginated(1, model);
    }
    //get overdue/expired subscription
    @GetMapping("/overdueSub")
    private String overdueSub(Model model) {
        model.addAttribute("activeCount", subRepository.countByStatus(Status.ACTIVE));
        model.addAttribute("pendingCount", subRepository.countByStatus(Status.PENDING));
        model.addAttribute("overdueCount", subRepository.countByStatus(Status.EXPIRED));
        return overduePaginated(1, model);
    }

    @GetMapping("/sub/update/{id}")
    public String updateSubscriptionForm(@PathVariable Long id, Model model){
        //      create sub object to hold sub form data
        model.addAttribute("update", subReminderService.getSubById(id));
        return "updateSub";
    }
    @PostMapping("/subscriptions/{id}")
    public String updateSub(@PathVariable Long id,
                            @ModelAttribute("update") Sub_reminder sub_reminder) throws SchedulerException, InterruptedException {
        subReminderService.updateSub(id,sub_reminder);
        return "redirect:/subscriptions";
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
    private void extractedModels(@PathVariable("pageNumber") int pageNumber, Model model, Page<Sub_reminder> page) {
        List<Sub_reminder> listSubscription = page.getContent();

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listSubscription", listSubscription);

        model.addAttribute("activeCount", subRepository.countByStatus(Status.ACTIVE));
        model.addAttribute("pendingCount", subRepository.countByStatus(Status.PENDING));
        model.addAttribute("overdueCount", subRepository.countByStatus(Status.EXPIRED));
    }

    @GetMapping("/page/{pageNumber}")
    public String allPaginated(@PathVariable(value = "pageNumber") int pageNumber, Model model){
        int pageSize = 5;

        Pageable pageable = PageRequest.of(pageNumber -1, pageSize);
        Page<Sub_reminder> page = subRepository.getInfo(pageable);
        extractedModels(pageNumber, model, page);


        return "allPaginated_index";
    }
    @GetMapping("/pageActive/{pageNumber}")
    public String activePaginated(@PathVariable(value = "pageNumber") int pageNumber, Model model){
        pageSize = 5;

        Pageable pageable = PageRequest.of(pageNumber -1, pageSize);
        Page<Sub_reminder> page = subRepository.getActiveInfo(pageable);
        extractedModels(pageNumber, model, page);

        return "activePaginated_index";
    }
    @GetMapping("/pagePending/{pageNumber}")
    public String pendingPaginated(@PathVariable(value = "pageNumber") int pageNumber, Model model){
        pageSize = 5;

        Pageable pageable = PageRequest.of(pageNumber -1, pageSize);
        Page<Sub_reminder> page = subRepository.getPendingInfo(pageable);
        extractedModels(pageNumber, model, page);

        return "pendingPaginated_index";
    }
    @GetMapping("/pageOverdue/{pageNumber}")
    public String overduePaginated(@PathVariable(value = "pageNumber") int pageNumber, Model model){
        pageSize = 5;

        Pageable pageable = PageRequest.of(pageNumber -1, pageSize);
        Page<Sub_reminder> page = subRepository.getExpiredInfo(pageable);
        extractedModels(pageNumber, model, page);

        return "overduePaginated_index";
    }
}
