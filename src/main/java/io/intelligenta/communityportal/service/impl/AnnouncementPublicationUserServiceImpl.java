package io.intelligenta.communityportal.service.impl;

import io.intelligenta.communityportal.models.dto.AnnPubUserDto;
import io.intelligenta.communityportal.repository.AnnouncementPublicationUserRepository;
import io.intelligenta.communityportal.service.AnnouncementPublicationUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
@Service
public class AnnouncementPublicationUserServiceImpl implements AnnouncementPublicationUserService {

    AnnouncementPublicationUserRepository announcementPublicationUserRepository;

    public AnnouncementPublicationUserServiceImpl(AnnouncementPublicationUserRepository announcementPublicationUserRepository) {
        this.announcementPublicationUserRepository = announcementPublicationUserRepository;
    }


    @Override
    public Page<AnnPubUserDto> findAllWithUsers(Pageable pageable) {

        List<AnnPubUserDto> dtos = announcementPublicationUserRepository.findAllWithUsers();
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > dtos.size() ? dtos.size() : (start + pageable.getPageSize());
        return new PageImpl<>(dtos.subList(start, end), pageable, dtos.size());
    }

    @Override
    public Page<AnnPubUserDto> findAllWithUsersByAnnouncement(String title, Pageable pageable) {

        List<AnnPubUserDto> dtos = announcementPublicationUserRepository.findAllWithUsersByAnnouncement(title);
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > dtos.size() ? dtos.size() : (start + pageable.getPageSize());
        return new PageImpl<>(dtos.subList(start, end), pageable, dtos.size());
    }

    @Override
    public Page<AnnPubUserDto> findAllWithUsersByUser(String firstName, String lastName, Pageable pageable) {
        firstName = firstName.toLowerCase();
        lastName = lastName.toLowerCase();

        List<AnnPubUserDto> dtos = announcementPublicationUserRepository.findAllWithUsersByUser(firstName, lastName);
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > dtos.size() ? dtos.size() : (start + pageable.getPageSize());
        return new PageImpl<>(dtos.subList(start, end), pageable, dtos.size());
    }

}
