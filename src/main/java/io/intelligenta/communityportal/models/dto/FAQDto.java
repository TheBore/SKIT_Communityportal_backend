package io.intelligenta.communityportal.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.multipart.MultipartFile;

public class FAQDto {
    private String questionMK;
    private String questionAL;
    private String answerMK;
    private String answerAL;
    @JsonProperty
    private MultipartFile attachment;

    private Long id;

    public String getQuestionMK() {
        return questionMK;
    }

    public void setQuestionMK(String questionMK) {
        this.questionMK = questionMK;
    }

    public String getQuestionAL() {
        return questionAL;
    }

    public void setQuestionAL(String questionAL) {
        this.questionAL = questionAL;
    }

    public String getAnswerMK() {
        return answerMK;
    }

    public void setAnswerMK(String answerMK) {
        this.answerMK = answerMK;
    }

    public String getAnswerAL() {
        return answerAL;
    }

    public void setAnswerAL(String answerAL) {
        this.answerAL = answerAL;
    }

    public MultipartFile getAttachment() {
        return attachment;
    }

    public void setAttachment(MultipartFile attachment) {
        this.attachment = attachment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
