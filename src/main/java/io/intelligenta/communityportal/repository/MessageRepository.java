package io.intelligenta.communityportal.repository;

import io.intelligenta.communityportal.models.Message;
import io.intelligenta.communityportal.models.Question;
import org.apache.xpath.operations.Bool;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MessageRepository extends JpaSpecificationRepository<Message>, PagingAndSortingRepository<Message, Long> {


    Page<Message> findAllByQuestionIdAndActiveOrderByDateCreatedAsc(Long questionId, Boolean active, Pageable pageable);

    Page<Message> findAllByQuestionIdAndActiveAndAuthorIdOrderByDateCreatedDesc(Long questionId, Boolean active, Long authorId, Pageable pageable);

    Page<Message> findAllByQuestionIdOrderByDateCreatedDesc(Long questionId, Pageable pageable);


}
