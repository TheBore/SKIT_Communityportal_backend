package io.intelligenta.communityportal.service;

import io.intelligenta.communityportal.models.AnnouncementComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
public interface AnnouncementCommentService {
    void addComment(String userEmail, Long announcementId,String comment);

    Page<AnnouncementComment> findAllByAnnouncementIdOrderBySubmittedAtAsc(Long annId, Pageable pageable);

}
