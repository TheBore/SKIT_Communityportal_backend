package io.intelligenta.communityportal.models.dto;

import io.intelligenta.communityportal.models.AnnouncementPublication;
import lombok.Data;

@Data
public class AnnPubWithComments {

    private AnnouncementPublication announcementPublication;

    private Integer numberOfComments;

}
