package io.intelligenta.communityportal.repository;

import io.intelligenta.communityportal.models.ActivityInstitution;
import io.intelligenta.communityportal.models.BBBUserMeeting;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BBBUserMeetingRepository extends JpaSpecificationRepository<BBBUserMeeting>,
        PagingAndSortingRepository<BBBUserMeeting, Long> {

    BBBUserMeeting findByMeetingId (String meetingId);

}
