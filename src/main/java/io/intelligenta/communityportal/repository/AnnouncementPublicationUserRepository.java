package io.intelligenta.communityportal.repository;

import io.intelligenta.communityportal.models.AnnouncementPublicationUser;
import io.intelligenta.communityportal.models.dto.AnnPubUserDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
public interface AnnouncementPublicationUserRepository extends JpaSpecificationRepository<AnnouncementPublicationUser> {

    @Query("SELECT new  io.intelligenta.communityportal.models.dto.AnnPubUserDto (u.firstName, u.lastName, ap.announcement.title, apu.readAt) FROM AnnouncementPublicationUser apu join User u on apu.userId=u.id join AnnouncementPublication ap on apu.annPubId=ap.id")
    List<AnnPubUserDto> findAllWithUsers();


    @Query("SELECT new  io.intelligenta.communityportal.models.dto.AnnPubUserDto (u.firstName, u.lastName, ap.announcement.title, apu.readAt) FROM AnnouncementPublicationUser apu join User u on apu.userId=u.id join AnnouncementPublication ap on apu.annPubId=ap.id where ((u.firstName=:firstName and u.lastName=:lastName) OR (LOWER(u.firstName)=:firstName and LOWER(u.lastName)=:lastName) or (u.firstName=:firstName) or (LOWER(u.firstName)=:firstName) or (LOWER(u.lastName)=:lastName) or (u.lastName=:lastName))")
    List<AnnPubUserDto> findAllWithUsersByUser(@Param("firstName") String firstName,@Param("lastName") String lastName);


    @Query("SELECT new  io.intelligenta.communityportal.models.dto.AnnPubUserDto (u.firstName, u.lastName, ap.announcement.title, apu.readAt) FROM AnnouncementPublicationUser apu join User u on apu.userId=u.id join AnnouncementPublication ap on apu.annPubId=ap.id where ap.announcement.title=:title ")
    List<AnnPubUserDto> findAllWithUsersByAnnouncement(@Param("title") String title);

}
