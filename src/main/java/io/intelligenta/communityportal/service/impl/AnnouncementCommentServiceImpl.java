package io.intelligenta.communityportal.service.impl;

import io.intelligenta.communityportal.models.Announcement;
import io.intelligenta.communityportal.models.AnnouncementComment;
import io.intelligenta.communityportal.models.auth.User;
import io.intelligenta.communityportal.models.exceptions.AnnouncementNotFoundException;
import io.intelligenta.communityportal.models.exceptions.UserNotFoundException;
import io.intelligenta.communityportal.repository.AnnouncementCommentRepository;
import io.intelligenta.communityportal.repository.AnnouncementRepository;
import io.intelligenta.communityportal.repository.auth.UserRepository;
import io.intelligenta.communityportal.service.AnnouncementCommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
@Service
public class AnnouncementCommentServiceImpl implements AnnouncementCommentService {

    UserRepository userRepository;
    AnnouncementRepository announcementRepository;
    AnnouncementCommentRepository announcementCommentRepository;

    public AnnouncementCommentServiceImpl(UserRepository userRepository,AnnouncementRepository announcementRepository,
                                          AnnouncementCommentRepository announcementCommentRepository){
        this.userRepository=userRepository;
        this.announcementRepository=announcementRepository;
        this.announcementCommentRepository = announcementCommentRepository;
    }

    @Override
    public void addComment(String userEmail, Long announcementId, String comment) {
        User user = userRepository.findByEmailAndActive(userEmail,true).orElseThrow(UserNotFoundException::new);
        Announcement announcement = announcementRepository.findById(announcementId).orElseThrow(AnnouncementNotFoundException::new);
        AnnouncementComment announcementComment = new AnnouncementComment();
        announcementComment.setAnnouncement(announcement);
        announcementComment.setSubmittedByUser(user);
        announcementComment.setComment(comment);
        announcementComment.setSubmittedAt(LocalDateTime.now());
        announcementCommentRepository.save(announcementComment);

    }

    @Override
    public Page<AnnouncementComment> findAllByAnnouncementIdOrderBySubmittedAtAsc(Long annId, Pageable pageable) {
        return announcementCommentRepository.findAllByAnnouncementIdOrderBySubmittedAtAsc(annId, pageable);
    }
}
