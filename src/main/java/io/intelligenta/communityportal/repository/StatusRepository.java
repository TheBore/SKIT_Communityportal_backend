package io.intelligenta.communityportal.repository;

import io.intelligenta.communityportal.models.Status;
import io.intelligenta.communityportal.models.StatusType;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StatusRepository extends JpaSpecificationRepository<Status> {

    Status findByStatusMkAndStatusType(String statusMk, StatusType statusType);

    Status save(Status status);

    @Query("SELECT s from Status s where (s.statusMk like %:keyword% OR LOWER(s.statusMk) LIKE %:keyword% OR s.statusEn like %:keyword% OR LOWER(s.statusEn) LIKE %:keyword% OR s.statusAl like %:keyword% OR LOWER(s.statusAl) LIKE %:keyword% OR s.statusString like %:keyword% OR LOWER(s.statusString) LIKE %:keyword%)")
    List<Status> findAllByKeyword(String keyword);

}
