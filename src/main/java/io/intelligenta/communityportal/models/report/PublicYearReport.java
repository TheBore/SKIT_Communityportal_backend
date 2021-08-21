package io.intelligenta.communityportal.models.report;

import io.intelligenta.communityportal.models.Attachment;
import io.intelligenta.communityportal.models.BaseEntity;
import io.intelligenta.communityportal.models.Institution;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
@Entity
@Data
public class PublicYearReport extends BaseEntity {

    private Integer year; // godina na izvestajot

    @Enumerated(EnumType.STRING)
    private ReportStatus status; // status saved or submitted

    @OneToOne
    private Attachment doc; // dokument prvo bez a potoa so pecat

    @OneToOne
    private Attachment signedDoc;

    @OneToOne
    private Report report; // izvestajot

    @OneToOne
    private Institution institution; // koja institucija go podnela izvestajot

}
