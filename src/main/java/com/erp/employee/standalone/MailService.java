package com.erp.employee.standalone;

import com.erp.employee.exceptions.AppException;
import com.erp.employee.models.PaySlip;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${app.frontend.reset-password}")
    private String resetPasswordUrl;

    @Value("${app.frontend.support-email}")
    private String supportEmail;

    private String getCommonSignature() {
        return "<br><br>If you need help, contact us at: <a href='mailto:" + supportEmail + "'>" + supportEmail + "</a><br>© " + LocalDate.now().getYear();
    }

    public void sendResetPasswordMail(String to, String fullName, String resetCode) {
        String subject = "Password Reset Request";
        String html = "<p>Dear " + fullName + ",</p>"
                + "<p>You requested to reset your password. Use the following code:</p>"
                + "<h2>" + resetCode + "</h2>"
                + "<p>Or click <a href='" + resetPasswordUrl + "'>here</a> to reset your password.</p>"
                + getCommonSignature();
        sendEmail(to, subject, html);
    }

    public void sendActivateAccountEmail(String to, String fullName, String verificationCode) {
        String subject = "Account Activation Request";
        String html = "<p>Hello " + fullName + ",</p>"
                + "<p>Please use the following code to activate your account:</p>"
                + "<h2>" + verificationCode + "</h2>"
                + getCommonSignature();
        sendEmail(to, subject, html);
    }

    public void sendAccountVerifiedSuccessfullyEmail(String to, String fullName) {
        String subject = "Account Verification Successful";
        String html = "<p>Hi " + fullName + ",</p>"
                + "<p>Your account has been verified successfully. Welcome aboard!</p>"
                + getCommonSignature();
        sendEmail(to, subject, html);
    }

    public void sendPasswordResetSuccessfully(String to, String fullName) {
        String subject = "Password Reset Successful";
        String html = "<p>Hello " + fullName + ",</p>"
                + "<p>Your password has been reset successfully.</p>"
                + getCommonSignature();
        sendEmail(to, subject, html);
    }

    public void sendTokenExpiryNotification(String to, String fullName, String meterNumber, String generatedMessage) {
        String subject = "Token Expiry Reminder";

        String html = "<p>Dear " + fullName + ",</p>"
                + "<p>" + generatedMessage + "</p>"
                + "<p>Please purchase a new token to ensure continued electricity supply.</p>"
                + getCommonSignature();

        sendEmail(to, subject, html);
    }


    private void sendEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = this.mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            this.mailSender.send(message);
        } catch (MessagingException e) {
            throw new AppException("Error sending email", e);
        }
    }

    public void sendSalaryCreditNotification(PaySlip paySlip, String message) {
        if (paySlip == null || paySlip.getEmployee() == null) {
            throw new IllegalArgumentException("PaySlip or Employee information is missing");
        }

        String subject = "Salary Credit Notification";

        String html = "<p>" + message + "</p>"
                + "<p>This is an automated message. Please do not reply.</p>"
                + getCommonSignature();

        sendEmail(
                paySlip.getEmployee().getEmail(),
                subject,
                html
        );
    }
}
