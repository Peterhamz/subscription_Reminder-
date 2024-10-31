package com.structured.liceneseReminder.service;

import com.structured.liceneseReminder.dto.MailSenderDto;
import com.structured.liceneseReminder.dto.SubDto;
import com.structured.liceneseReminder.dto.UserDto;
import com.structured.liceneseReminder.enums.Status;
import com.structured.liceneseReminder.exception.ResourceAlreadyExistException;
import com.structured.liceneseReminder.exception.ResourceNotFoundException;
import com.structured.liceneseReminder.model.Department;
import com.structured.liceneseReminder.model.SubReminder;
import com.structured.liceneseReminder.repository.SubRepository;
import com.structured.liceneseReminder.repository.DepartmentRepository;
import com.structured.liceneseReminder.response.SubResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
@EnableScheduling
public class SubReminderServiceImpl implements SubReminderService {

    @Autowired
    private SubRepository subRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private MailSenderService mailSenderService;


    @Override
    public SubDto createReminder(SubDto subDto) throws SchedulerException, InterruptedException {

        SubReminder subReminder = new SubReminder();

        Optional<Department> getDepartment= departmentRepository.getDepartmentById(subDto.getDepartmentID());

        if (getDepartment.isPresent()){

            subReminder.setName(subDto.getName());
            subReminder.setLicensePermitName(subDto.getLicensePermitName());
            subReminder.setDescription(subDto.getDescription());
            subReminder.setExpiryDate(subDto.getExpiryDate());
            subReminder.setDepartment(getDepartment.get());
            subReminder.setStatus(Status.ACTIVE);

            System.out.println(getDepartment.get().toString());
            subRepository.save(subReminder);

            MailSenderDto emailDetails = MailSenderDto.builder()
                    .recipient(getDepartment.get().getEmail())
                    .subject("Subscription Licence Reminder")
                    .messageBody("Congratulations your Subscription Licence for " + subDto.getLicensePermitName() + "\n" +
                            " Has been added \n" + " The expiry date is " + subDto.getExpiryDate())
                    .build();
            mailSenderService.sendEmail(emailDetails);

            SubResponse response = new SubResponse();
            response.setDepartment(getDepartment.get().getDepartment());
            response.setInformation(subDto.getLicensePermitName());
            return subDto;

        }
        return subDto;
    }

    @Override
    public List<SubReminder> getAllSubscription() {
        return subRepository.findAll();
    }

    @Override
    public SubReminder getSubById(Long id) {
      return subRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The subscription with " , " id "  , id));
    }
    @Override
    public SubReminder updateSub(Long subId, SubDto updateSubReminder) {

        SubReminder subscription = subRepository.findById(subId)
                .orElseThrow(() -> new ResourceNotFoundException("The subscription with",  "id",  subId ));

        Optional<Department> getDepartment= departmentRepository.getDepartmentById(subscription.getDepartment().getId());

        if (getDepartment.isPresent()) {

            subscription.setName(updateSubReminder.getName());
            subscription.setDepartment(getDepartment.get());
            subscription.setStatus(Status.ACTIVE);
            subscription.setDescription(updateSubReminder.getDescription());
            subscription.setExpiryDate(updateSubReminder.getExpiryDate());
            subscription.setLicensePermitName(updateSubReminder.getLicensePermitName());

            subRepository.save(subscription);

            System.out.println("this is the updated subscription " + subscription);
            return subscription;
        }
        System.out.println("This Update can not be done at the moment");
        return subscription;
    }

    @Override
    public UserDto createUser(UserDto userDto) {

        Department department = new Department();

        Optional<Department> email = departmentRepository
                .getDepartmentByEmail(userDto.getEmail());

        Optional<Department> departments = departmentRepository
                .getDepartmentByEmail(userDto.getEmail());

        if(!(email.isPresent() && departments.isPresent())) {
            department.setEmail(userDto.getEmail());
            department.setDepartment(userDto.getDepartment());
            departmentRepository.save(department);
        }else {
            throw new ResourceAlreadyExistException("The Details Entered: " + userDto.getEmail() + " Already Exist", " ");
        }

       // System.out.println(department.getEmail() + department.getDepartment());
        return userDto;
    }

    @Override
    public List<Department> getAllDepartment() {
        return departmentRepository.findAll();
    }

    @Override
    public void sendEmail() {
 // Get all the Subscription from the Database and check for expiry date
        subRepository.findAll()
                .forEach(sub -> {
                    LocalDate expiryDate = sub.getExpiryDate();
                    LocalDate today = LocalDate.now();
                   // LocalDate notYetExpired = today.plusDays(31);
                    if (expiryDate.isBefore(today)){
                        sub.setStatus(Status.EXPIRED);
                        subRepository.save(sub);
                    }
//                    if(expiryDate.isAfter(notYetExpired)){
//                        sub.setStatus(Status.ACTIVE);
//                        subRepository.save(sub);
//                    }
                    if(isExpiringIn30Days(expiryDate)){
//                        System.out.println(sub.getLicensePermitName() +
//                                " --> Date reached for 30days");
//
// // Send Email to the Sub that met the requirement
//                        MailSenderDto emailDetails = MailSenderDto.builder()
//                .recipient(sub.getEmail())
//                .subject("Subscription Licence Reminder")
//                .messageBody("Your Subscription Licence for " + sub.getLicensePermitName() + "\n" +
//                        " Will expire on this day \n" + sub.getExpiryDate())
//                .build();
//        mailSenderService.sendEmail(emailDetails);
//
//   // Edit the Status and save it to the Database.
//                        sub.setStatus(Status.PENDING);
//                        subRepository.save(sub);

                    } else if (isExpiringIn10Days(expiryDate)) {
//                        System.out.println(sub.getLicensePermitName() +
//                                " --> Date reached for 10Days");
//
//   // Send Email to the Sub that met the requirement
//                        MailSenderDto emailDetails = MailSenderDto.builder()
//                                .recipient(sub.getEmail())
//                                .subject("Subscription Licence Reminder")
//                                .messageBody("Your Subscription Licence for " + sub.getLicensePermitName() + "\n" +
//                                        " Will expire on this day \n" + sub.getExpiryDate())
//                                .build();
//                        mailSenderService.sendEmail(emailDetails);
//
//   // Edit the Status and save it to the Database.
//                        sub.setStatus(Status.PENDING);
//                        subRepository.save(sub);
//

                    } else if (isExpiringToday(expiryDate)) {
                        System.out.println(sub.getLicensePermitName() +
                                " --> Date reached for Expiry");
   // Send Email to the Sub that met the requirement
                        MailSenderDto emailDetails = MailSenderDto.builder()
                                .recipient(sub.getDepartment().getEmail())
                                .subject("Subscription Licence Reminder")
                                .messageBody("Your Subscription Licence for " + sub.getLicensePermitName() + "\n" +
                                        " Will expire Today \n" + sub.getExpiryDate() + "PLease Renew and " +
                                        "Update to avoid service interruption")
                                .build();
                        mailSenderService.sendEmail(emailDetails);

   // Edit the Status and save it to the Database.
                        sub.setStatus(Status.EXPIRED);
                        subRepository.save(sub);

                    } else if (isExpiringIn1Day(expiryDate)) {
                        System.out.println(sub.getLicensePermitName() +
                                " --> Date reached for 1Day remaining");

   // Send Email to the Sub that met the requirement
                        MailSenderDto emailDetails = MailSenderDto.builder()
                                .recipient(sub.getDepartment().getEmail())
                                .subject("Subscription Licence Reminder")
                                .messageBody("Your Subscription Licence for " + sub.getLicensePermitName() + "\n" +
                                        " Will expire on this day \n" + sub.getExpiryDate())
                                .build();
                        mailSenderService.sendEmail(emailDetails);

   // Edit the Status and save it to the Database.
                        sub.setStatus(Status.PENDING);
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
    private boolean isExpiringIn1Day(LocalDate expiryDate){
        LocalDate today = LocalDate.now();
        LocalDate expiryDateMinus10Days = expiryDate.minusDays(1);
        return today.equals(expiryDateMinus10Days);
    }

    // setup Scheduler
    @Scheduled(fixedRate = 2 * 60 * 1000, initialDelay = 1000)
   // @Scheduled(cron = "0 9 0 * * ?")
   // @Scheduled(cron = "0 59 11 * * ?")
    private void scheduler(){
     System.out.println("Scheduler running");
     sendEmail();
    }
    // Implementation for pagination
    @Override
    public Page<SubReminder> allPaginated(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber -1, pageSize);
        return this.subRepository.findAll(pageable);
    }



    @Override
    public Page<SubReminder> pendingPaginated(int pageNumber, int pageSize) {
        return null;
    }
    @Override
    public Page<SubReminder> activePaginated(int pageNumber, int pageSize) {
        return null;
    }

    @Override
    public Page<SubReminder> overduePaginated(int pageNumber, int pageSize) {
        return null;
    }
    @Override
    public Page<SubReminder> getSubscriptionsByDepartmentName(String departmentName, Pageable pageable) {
        return subRepository.findByDepartment_Department(departmentName, pageable);
    }


}
