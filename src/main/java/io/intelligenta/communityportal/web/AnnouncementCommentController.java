package io.intelligenta.communityportal.web;

import io.intelligenta.communityportal.models.AnnouncementComment;
import io.intelligenta.communityportal.repository.AnnouncementCommentRepository;
import io.intelligenta.communityportal.service.AnnouncementCommentService;
import io.intelligenta.communityportal.service.AnnouncementService;
import io.intelligenta.communityportal.service.auth.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
@RestController
@RequestMapping("/rest/annComm")
@CrossOrigin("*")
public class AnnouncementCommentController {

    UserService userService;
    AnnouncementService announcementService;
    AnnouncementCommentService announcementCommentService;
    AnnouncementCommentRepository announcementCommentRepository;

    public AnnouncementCommentController(UserService userService, AnnouncementService announcementService,
                                         AnnouncementCommentService announcementCommentService,
                                         AnnouncementCommentRepository announcementCommentRepository) {
        this.userService = userService;
        this.announcementService = announcementService;
        this.announcementCommentService = announcementCommentService;
        this.announcementCommentRepository = announcementCommentRepository;
    }

    @GetMapping("/comments")
    public Page<AnnouncementComment> getCommentsForAnnouncement(@RequestParam("annId") Long annId, Pageable pageable) {
        return announcementCommentService.findAllByAnnouncementIdOrderBySubmittedAtAsc(annId, pageable);
    }

    @PostMapping("/addComment")
    public void addComment(@RequestParam("email") String userEmail, @RequestParam("annId") Long announcementId,
                           @RequestParam("comment") String comment) {
        announcementCommentService.addComment(userEmail, announcementId, comment);
    }

}
