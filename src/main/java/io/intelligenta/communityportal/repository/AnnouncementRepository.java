package io.intelligenta.communityportal.repository;

import io.intelligenta.communityportal.models.Announcement;
import io.intelligenta.communityportal.models.AreaOfInterest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
@Repository
public interface AnnouncementRepository extends JpaSpecificationRepository<Announcement> {

    List<Announcement> findAllByCreatorId(Long userId);


    @Query("SELECT a FROM Announcement a ORDER BY a.dateCreated DESC")
    Page<Announcement> findAllAnnouncements(Pageable pageable);

    @Query("select a from Announcement a where a.areaOfInterest in :areasOfInterest")
    Page<Announcement> getAllByUserAreasOfInterest (@Param("areasOfInterest") List<AreaOfInterest> areasOfInterest, Pageable pageable);

}
