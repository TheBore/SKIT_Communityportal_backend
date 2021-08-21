package io.intelligenta.communityportal.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
@Data
public class PublicDocumentForYear extends BaseEntity {

    @OneToOne
    private PublicDocumentType type;

    private Integer year;

    private String url;

    @ManyToOne
    private Institution institution;

    @ManyToOne
    private Attachment attachment;
}
