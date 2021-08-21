package io.intelligenta.communityportal.service;


import io.intelligenta.communityportal.models.dto.AnnPubUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
public interface AnnouncementPublicationUserService  {

    Page<AnnPubUserDto> findAllWithUsers(Pageable pageable);
    Page<AnnPubUserDto> findAllWithUsersByAnnouncement(String title,Pageable pageable);
    Page<AnnPubUserDto> findAllWithUsersByUser(String firstName,String lastName,Pageable pageable);

}
