package io.intelligenta.communityportal.repository;

import io.intelligenta.communityportal.models.PublicDocumentType;

import java.util.List;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
public interface PublicDocumentTypeRepository extends JpaSpecificationRepository<PublicDocumentType> {
    List<PublicDocumentType> findAllByOrderByIdDesc();
}
