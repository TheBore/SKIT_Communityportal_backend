package io.intelligenta.communityportal.models;

import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class MeasureAttachment extends BaseEntity{

    private Long measureId;

    private Long attachmentId;

}
