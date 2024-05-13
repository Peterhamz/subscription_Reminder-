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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

        String department = subDto.getDepartment();

        Sub_reminder subReminder = new Sub_reminder();

        switch (department) {
            case "IT": subDto.setDepartment("IT");
                subReminder.setEmail("itsupport@structuredresource.com");
                break;
            case "STRATEGY & OPERATIONS": subDto.setDepartment("STRATEGY & OPERATIONS");
                subReminder.setEmail("strategyandoperationsdesk@structuredresource.com");
                break;
            case "HR & ADMIN": subDto.setDepartment("HR & ADMIN");
                subReminder.setEmail("hr@structuredresource.com");
                break;
            case "SUPPLY CHAIN": subDto.setDepartment("SUPPLY CHAIN");
                subReminder.setEmail("procurement@structuredresource.com");
                break;
            case "TECHNICAL": subDto.setDepartment("TECHNICAL");
                subReminder.setEmail("support@structuredresource.com");
                break;
            case "FINANCE": subDto.setDepartment("FINANCE");
                subReminder.setEmail("finadmin@structuredresource.com");
                break;
            case "BUSINESS DEVELOPMENT": subDto.setDepartment("BUSINESS DEVELOPMENT");
                subReminder.setEmail("salesadmin@structuredresource.com");
                break;
            default: subDto.setDepartment("IT");
                subReminder.setEmail("itsupport@structuredresource.com");
                break;
        }
        subDto.getDepartment();
        subReminder.setName(subDto.getName());
        subReminder.setLicensePermitName(subDto.getLicensePermitName());
        subReminder.setDescription(subDto.getDescription());
        subReminder.setExpiryDate(subDto.getExpiryDate());
        subReminder.setDepartment(subDto.getDepartment());
        subReminder.setStatus(Status.ACTIVE);

        subRepository.save(subReminder);

        MailSenderDto emailDetails = MailSenderDto.builder()
                .recipient(subDto.getEmail())
                .subject("Subscription Licence Reminder")
                .messageBody("Congrats your Subscription Licence for " + subDto.getLicensePermitName() + "\n" +
                        " Has been added \n" + " The expiry date is " + subDto.getExpiryDate())
                .build();
        mailSenderService.sendEmail(emailDetails);


        // System.out.println("Scheduler finished");

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
                    LocalDate today = LocalDate.now();
                    if (expiryDate.isBefore(today)){
                        sub.setStatus(Status.EXPIRED);
                        subRepository.save(sub);
                    }
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
                        sub.setStatus(Status.EXPIRED);
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

    // setup Scheduler
    @Scheduled(fixedRate = 2 * 60 * 1000, initialDelay = 1000)
   // @Scheduled(cron = "0 9 0 * * ?")
    private void scheduler(){
     System.out.println("Scheduler running");
     sendEmail();
    }

    // Implementation for pagination
    @Override
    public Page<Sub_reminder> allPaginated(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber -1, pageSize);
        return this.subRepository.findAll(pageable);
    }

    @Override
    public Page<Sub_reminder> pendingPaginated(int pageNumber, int pageSize) {
        return null;
    }

    @Override
    public Page<Sub_reminder> activePaginated(int pageNumber, int pageSize) {
        return null;
    }

    @Override
    public Page<Sub_reminder> overduePaginated(int pageNumber, int pageSize) {
        return null;
    }

}
