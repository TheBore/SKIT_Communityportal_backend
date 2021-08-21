package io.intelligenta.communityportal.service;

import io.intelligenta.communityportal.models.Announcement;
import io.intelligenta.communityportal.models.AreaOfInterest;
import io.intelligenta.communityportal.models.dto.AnnouncementDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface AnnouncementService extends BaseEntityCrudService<Announcement> {

    Announcement findAnnouncementById(Long Id);

    Announcement addAnnouncement(String title, String body, Long areaOfInterestId, String name, String mimeType, Long size, byte[] content);

    void deleteAnnouncement(Long annId);

    Announcement updateAnnouncement (AnnouncementDto announcement);

    Page<Announcement> findAllAnnouncements(Pageable pageable);

    Page<Announcement> getAllAnnouncementsByUserAreasOfInterest(List<String> areas, Pageable pageable);

    Integer getNumberOfAnnouncementPublishes(Long id);
}
