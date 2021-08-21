package io.intelligenta.communityportal.models;

import lombok.Data;

import javax.persistence.Entity;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
@Entity
@Data
public class AnnouncementAttachment extends BaseEntity  {

    private Long announcementId;

    private Long attachmentId;
}
