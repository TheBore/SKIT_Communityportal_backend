package io.intelligenta.communityportal.repository;

import io.intelligenta.communityportal.models.FAQ;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.w3c.dom.stylesheets.LinkStyle;

public interface FAQRepository extends PagingAndSortingRepository<FAQ, Long> {
    Page<FAQ> findAll (Pageable pageable);
}
