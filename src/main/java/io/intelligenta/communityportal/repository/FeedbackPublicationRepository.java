package io.intelligenta.communityportal.repository;

import io.intelligenta.communityportal.models.feedback.FeedbackPublication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
public interface FeedbackPublicationRepository extends PagingAndSortingRepository<FeedbackPublication, Long> {
    List<FeedbackPublication> findFeedbackPublicationByFeedbackId(Long id);

    Page<FeedbackPublication> findByInstitutionIdOrderByDateCreatedDesc(Long institutionId, Pageable pageable);

    Page<FeedbackPublication> findByFeedbackId(Long feedbackId, Pageable pageable);

    FeedbackPublication findByInstitutionIdAndFeedbackId(Long institutionId, Long feedbackId);

    List<FeedbackPublication> findAllByInstitutionId(Long institutionId);

    @Query("select fb from FeedbackPublication fb where fb.institution.id=:institutionId and fb.feedback.dueDate>:date order by fb.dateCreated desc")
    List<FeedbackPublication> findByInstitutionIdOrderByDateCreatedDesc(@Param("institutionId") Long insId, @Param("date")LocalDate date);
}
