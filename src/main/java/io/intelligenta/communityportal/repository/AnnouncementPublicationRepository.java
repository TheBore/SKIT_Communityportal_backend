
package io.intelligenta.communityportal.repository;

import io.intelligenta.communityportal.models.AnnouncementPublication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;


import java.util.List;

public interface AnnouncementPublicationRepository extends PagingAndSortingRepository<AnnouncementPublication,Long> {

    List<AnnouncementPublication> findAllByAnnouncementIdOrderByReadAsc(Long annId);
    Page<AnnouncementPublication> findAllByAnnouncementIdOrderByReadAsc(Long annId,Pageable pageable);
    List<AnnouncementPublication> findAllByReceiverId(Long id);
    Page<AnnouncementPublication> findByReceiverId(Long receiverId, Pageable pageable);
    List<AnnouncementPublication> findAllByAnnouncementId(Long id);
    AnnouncementPublication findByAnnouncementIdAndReceiverId(Long annId,Long instId);
}