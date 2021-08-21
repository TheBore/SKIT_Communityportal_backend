package io.intelligenta.communityportal.repository.Mail;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
public interface EmailRepository {
    void sendHtmlMail(List<String> to,
                      String subject,
                      String template,
                      Map<String, String> params,
                      String from) throws MessagingException, IOException;

}
