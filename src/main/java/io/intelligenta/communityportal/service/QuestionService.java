package io.intelligenta.communityportal.service;

import io.intelligenta.communityportal.models.Question;
import io.intelligenta.communityportal.models.dto.QuestionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionService extends BaseEntityCrudService<Question> {

    Question createQuestion(QuestionDto questionDto);

    Page<Question> findAll(Pageable pageable);

    Page<Question> findAllByActive(Boolean active, Pageable pageable);

//    Page<Question> findAllByActive(Boolean active, Long areaOfInterestId, Pageable pageable);

    Question findQuestionById(Long id);

    Question updateQuestion(QuestionDto questionDto);

    Question setInactive(Long id);

    Question setActive(Long id);

    Page<Question> findAllByActiveAndByAuthor(Boolean active, Pageable pageable);

    Page<Question> getAllQuestionsByUserAreasOfInterest(List<String> areas, Pageable pageable);

    Integer getNumberOfCommentsForQuestion(Long questionId);


}
