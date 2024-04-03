package com.structured.liceneseReminder.service;

import com.structured.liceneseReminder.dto.MailSenderDto;
import com.structured.liceneseReminder.dto.SubDto;
import com.structured.liceneseReminder.enums.Status;
import com.structured.liceneseReminder.model.Sub_reminder;
import com.structured.liceneseReminder.reposotory.SubRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class SubReminderServiceImpl implements SubReminderService {

    @Autowired
    private SubRepository subRepository;

    @Autowired
    private MailSenderService mailSenderService;
    @Override
    public SubDto createReminder(SubDto subDto) {

        Sub_reminder subReminder = new Sub_reminder();

        subReminder.setName(subDto.getName());
        subReminder.setDepartment(subDto.getDepartment());
        subReminder.setLicensePermitName(subDto.getLicensePermitName());
        subReminder.setDescription(subDto.getDescription());
        subReminder.setExpiryDate(LocalDate.parse(subDto.getExpiryDate().toString()));
        subReminder.setEmail(subDto.getEmail());
        subReminder.setStatus(Status.valueOf(Status.ACTIVE.toString()));

        subRepository.save(subReminder);

        MailSenderDto emailDetails = MailSenderDto.builder()
                .recipient(subDto.getEmail())
                .subject("Subscription Licence Reminder")
                .messageBody("Congrats your Subscription Licence for " + subDto.getLicensePermitName() + "\n" +
                        " Has been added \n" + " The expiry date is " + subDto.getExpiryDate())
                .build();
        mailSenderService.sendEmail(emailDetails);

        return subDto;


    }

    @Override
    public List<Sub_reminder> getAllSubscription() {
        return subRepository.findAll();
    }

}
