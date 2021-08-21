package io.intelligenta.communityportal.web;

import io.intelligenta.communityportal.models.Question;
import io.intelligenta.communityportal.models.dto.QuestionDto;
import io.intelligenta.communityportal.service.QuestionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/question")
public class QuestionController extends CrudResource<Question, QuestionService> {

    private final QuestionService questionService;

    public QuestionController (QuestionService questionService){
        this.questionService = questionService;
    }

    @Override
    public QuestionService getService() {
        return questionService;
    }

    @Override
    public Question beforeUpdate(Question oldEntity, Question newEntity) {
        return oldEntity;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public Question createQuestion (@ModelAttribute QuestionDto question){
        return questionService.createQuestion(question);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public Question findById(@PathVariable(value = "id") Long id) {
        return questionService.findQuestionById(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all")
    public Page<Question> findAllQuestions(Boolean active, Pageable pageable) {
        return questionService.findAllByActive(active, pageable);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @GetMapping("/allQuestions")
    public Page<Question> findAll(Pageable pageable) {
        return questionService.findAll(pageable);
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/update")
    public Question updateQuestion(@ModelAttribute QuestionDto question) {
        return questionService.updateQuestion(question);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete/{id}")
    public Question deleteQuestion(@PathVariable(value = "id") Long id) {
        return questionService.setInactive(id);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/unDelete/{id}")
    public Question unDeleteQuestion(@PathVariable(value = "id") Long id) {
        return questionService.setActive(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/allQuestionsNotActive")
    public Page<Question> findAllNotActive(Boolean active, Pageable pageable) {
        return questionService.findAllByActiveAndByAuthor(active, pageable);
    }

    @GetMapping("/questions-by-areas")
    public Page<Question> getAllQuestionsByAreasOfInterest (@RequestParam (value = "areas")List<String> areas, Pageable pageable){
        return questionService.getAllQuestionsByUserAreasOfInterest(areas, pageable);
    }

    @GetMapping("/numberOfMessagesForQuestion/{questionId}")
    public Integer numberOfMessagesForQuestion(@PathVariable Long questionId) {
        return questionService.getNumberOfCommentsForQuestion(questionId);
    }


}
