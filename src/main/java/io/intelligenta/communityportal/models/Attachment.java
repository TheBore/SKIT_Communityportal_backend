package io.intelligenta.communityportal.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Lob;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */

@Entity
@Data
public class Attachment extends BaseEntity {

    private String name;
    private String mimeType;
    private Long size;

    @JsonIgnore
    @Lob
    private byte[] content;

}
