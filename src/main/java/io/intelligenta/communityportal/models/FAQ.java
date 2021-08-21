package io.intelligenta.communityportal.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class FAQ extends BaseEntity{

    private String questionMK;
    private String questionAL;

    @Column(length=4000)
    private String answerMK;

    @Column(length=4000)
    private String answerAL;

    private String docName;
    private String mimeType;

    @JsonIgnore
    @Lob
    private byte[] content;
}
