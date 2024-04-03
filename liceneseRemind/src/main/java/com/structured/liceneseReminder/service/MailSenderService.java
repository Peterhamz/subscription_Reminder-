package com.structured.liceneseReminder.service;

import com.structured.liceneseReminder.dto.MailSenderDto;

public interface MailSenderService {
    void sendEmail(MailSenderDto emailDetails);
    void sendEmailWithAttachment(MailSenderDto emailDetails);
}
