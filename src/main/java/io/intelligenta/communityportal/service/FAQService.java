package io.intelligenta.communityportal.service;

import io.intelligenta.communityportal.models.FAQ;

import java.util.List;

public interface FAQService {

    FAQ createFAQ (String questionMK, String questionAL, String answerMK, String answerAL ,String docName, String mimeType, byte[] content);
    FAQ updateFAQ (String questionMK, String questionAL, String answerMK, String answerAL ,String docName, String mimeType, byte[] content, Long id);
    FAQ findFAQById (Long id);
    void deleteFAQ (Long id);

}
