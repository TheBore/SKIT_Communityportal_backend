package io.intelligenta.communityportal.service.impl;

import io.intelligenta.communityportal.models.FAQ;
import io.intelligenta.communityportal.models.exceptions.FAQNotFoundException;
import io.intelligenta.communityportal.repository.FAQRepository;
import io.intelligenta.communityportal.service.FAQService;
import org.springframework.stereotype.Service;


@Service
public class FAQServiceImpl implements FAQService {

    private FAQRepository faqRepository;

    FAQServiceImpl (FAQRepository faqRepository){
        this.faqRepository = faqRepository;
    }

    @Override
    public FAQ createFAQ(String questionMK, String questionAL, String answerMK, String answerAL, String docName, String mimeType, byte[] content) {

        FAQ newFAQ = new FAQ();

        newFAQ.setQuestionMK(questionMK);
        newFAQ.setQuestionAL(questionAL);
        newFAQ.setAnswerMK(answerMK);
        newFAQ.setAnswerAL(answerAL);


        newFAQ.setDocName(docName);
        newFAQ.setMimeType(mimeType);
        newFAQ.setContent(content);

        return faqRepository.save(newFAQ);
    }

    @Override
    public FAQ updateFAQ(String questionMK, String questionAL, String answerMK, String answerAL ,String docName, String mimeType, byte[] content, Long id) {
        FAQ newFAQ = faqRepository.findById(id).orElseThrow(FAQNotFoundException::new);
//        newFAQ.setId(faq.getId());
        newFAQ.setQuestionMK(questionMK);
        newFAQ.setAnswerMK(answerMK);
        newFAQ.setQuestionAL(questionAL);
        newFAQ.setAnswerAL(answerAL);
        newFAQ.setContent(content);
        newFAQ.setDocName(docName);
        newFAQ.setMimeType(mimeType);

        faqRepository.save(newFAQ);
        return newFAQ;
    }

    @Override
    public FAQ findFAQById(Long id) {
        return faqRepository.findById(id).orElseThrow(FAQNotFoundException::new);
    }


    @Override
    public void deleteFAQ(Long id) {
        faqRepository.deleteById(id);
    }

}
