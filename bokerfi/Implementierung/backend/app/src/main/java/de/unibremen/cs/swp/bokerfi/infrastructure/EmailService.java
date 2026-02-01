package de.unibremen.cs.swp.bokerfi.infrastructure;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


/**
 * Service zur Versendung von E-Mails.
 *
 * <p>
 * Diese Klasse kapselt den technischen Versand von E-Mails und trennt
 * die Infrastruktur-Logik von der fachlichen Service-Schicht.
 * </p>
 */
@Service
public class EmailService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("bokerfi@offline.de");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        try {
            mailSender.send(message);
            log.info("✅ [EMAIL SENT] To: {} | Subject: {}", to, subject);
        } catch (org.springframework.mail.MailException e) {
            log.error("⚠️ [EMAIL FAIL] Could not send email to {}: {}", to, e.getMessage());
            // Log payload for dev verification
            log.info("   [MOCK EMAIL] Content: {}", text);
        }
    }
}
