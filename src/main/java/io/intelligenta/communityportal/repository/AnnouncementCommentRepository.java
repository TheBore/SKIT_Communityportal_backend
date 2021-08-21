package io.intelligenta.communityportal.repository;

import io.intelligenta.communityportal.models.AnnouncementComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
public interface AnnouncementCommentRepository extends PagingAndSortingRepository<AnnouncementComment,Long> {
    Page<AnnouncementComment> findAllByAnnouncementIdOrderBySubmittedAtAsc(Long annId, Pageable pageable);

    List<AnnouncementComment> findAllByAnnouncementId(Long id);
}
