package com.structured.liceneseReminder.service;

import com.structured.liceneseReminder.dto.MailSenderDto;
import com.structured.liceneseReminder.dto.SubDto;
import com.structured.liceneseReminder.enums.Status;
import com.structured.liceneseReminder.model.Sub_reminder;
import com.structured.liceneseReminder.reposotory.SubRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
@EnableScheduling
public class SubReminderServiceImpl implements SubReminderService {

    @Autowired
    private SubRepository subRepository;

    @Autowired
    private MailSenderService mailSenderService;

    @Override
    public SubDto createReminder(SubDto subDto) throws SchedulerException, InterruptedException {

        Sub_reminder subReminder = new Sub_reminder();

        subReminder.setName(subDto.getName());
        subReminder.setDepartment(subDto.getDepartment());
        subReminder.setLicensePermitName(subDto.getLicensePermitName());
        subReminder.setDescription(subDto.getDescription());
        subReminder.setExpiryDate(subDto.getExpiryDate());
        subReminder.setEmail(subDto.getEmail());
        subReminder.setStatus(Status.ACTIVE);

        subRepository.save(subReminder);

        MailSenderDto emailDetails = MailSenderDto.builder()
                .recipient(subDto.getEmail())
                .subject("Subscription Licence Reminder")
                .messageBody("Congrats your Subscription Licence for " + subDto.getLicensePermitName() + "\n" +
                        " Has been added \n" + " The expiry date is " + subDto.getExpiryDate())
                .build();
        mailSenderService.sendEmail(emailDetails);


        System.out.println("Scheduler finished");

        return subDto;


    }

    @Override
    public List<Sub_reminder> getAllSubscription() {
        return subRepository.findAll();
    }

    @Override
    public void sendEmail() {
 // Get all the Subscription from the Database and check for expiry date
        subRepository.findAll()
                .forEach(sub -> {
                    LocalDate expiryDate = sub.getExpiryDate();
                    if(isExpiringIn30Days(expiryDate)){
                        System.out.println(sub.getLicensePermitName() +
                                " --> Date reached for 30days");

 // Send Email to the Sub that met the requirement
                        MailSenderDto emailDetails = MailSenderDto.builder()
                .recipient(sub.getEmail())
                .subject("Subscription Licence Reminder")
                .messageBody("Your Subscription Licence for " + sub.getLicensePermitName() + "\n" +
                        " Will expire on this day \n" + sub.getExpiryDate())
                .build();
        mailSenderService.sendEmail(emailDetails);

   // Edit the Status and save it to the Database.
                        sub.setStatus(Status.PENDING);
                        subRepository.save(sub);

                    } else if (isExpiringIn10Days(expiryDate)) {
                        System.out.println(sub.getLicensePermitName() +
                                " --> Date reached for 10Days");

   // Send Email to the Sub that met the requirement
                        MailSenderDto emailDetails = MailSenderDto.builder()
                                .recipient(sub.getEmail())
                                .subject("Subscription Licence Reminder")
                                .messageBody("Your Subscription Licence for " + sub.getLicensePermitName() + "\n" +
                                        " Will expire on this day \n" + sub.getExpiryDate())
                                .build();
                        mailSenderService.sendEmail(emailDetails);

   // Edit the Status and save it to the Database.
                        sub.setStatus(Status.PENDING);
                        subRepository.save(sub);


                    } else if (isExpiringToday(expiryDate)) {
                        System.out.println(sub.getLicensePermitName() +
                                " --> Date reached for Expiry");
   // Send Email to the Sub that met the requirement
                        MailSenderDto emailDetails = MailSenderDto.builder()
                                .recipient(sub.getEmail())
                                .subject("Subscription Licence Reminder")
                                .messageBody("Your Subscription Licence for " + sub.getLicensePermitName() + "\n" +
                                        " Will expire Today \n" + sub.getExpiryDate() + "PLease Renew and " +
                                        "Update to avoid service interruption")
                                .build();
                        mailSenderService.sendEmail(emailDetails);

   // Edit the Status and save it to the Database.
                        sub.setStatus(Status.INACTIVE);
                        subRepository.save(sub);

                    }
                });
    }


    // Checking to see if the expiry date is within 30 days
    private boolean isExpiringIn30Days(LocalDate expiryDate){
        LocalDate today = LocalDate.now();
        LocalDate expiryDateMinus30Days = expiryDate.minusDays(30);
        return today.equals(expiryDateMinus30Days);
    }

    // Checking to see if the expiry date is within 10 days
    private boolean isExpiringIn10Days(LocalDate expiryDate){
        LocalDate today = LocalDate.now();
        LocalDate expiryDateMinus10Days = expiryDate.minusDays(10);
        return today.equals(expiryDateMinus10Days);
    }

    // Checking to see if it has expired
    private boolean isExpiringToday(LocalDate expiryDate) {
        LocalDate today = LocalDate.now();
        return today.equals(expiryDate);
    }
    @Scheduled(fixedRate = 2 * 60 * 1000, initialDelay = 1000)
   // @Scheduled(cron = "0 9 0 * * ?")
    private void scheduler(){
     System.out.println("Scheduler running");
     sendEmail();
    }

}
