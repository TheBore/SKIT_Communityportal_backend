package io.intelligenta.communityportal.web;

import io.intelligenta.communityportal.models.Activity;
import io.intelligenta.communityportal.models.Evaluation;
import io.intelligenta.communityportal.models.dto.ActivityDto;
import io.intelligenta.communityportal.models.dto.EvaluationDto;
import io.intelligenta.communityportal.service.EvaluationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/evaluation")
public class EvaluationController {

    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public Evaluation createEvaluation(@RequestBody EvaluationDto evaluation){
        return evaluationService.createEvaluation(evaluation);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PostMapping("/create/{id}")
    public Evaluation updateEvaluation(@PathVariable Long id, @RequestBody EvaluationDto evaluation){
        return evaluationService.updateEvaluation(id, evaluation);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get-by-nap/{napId}")
    public Evaluation getEvaluationByNapId(@PathVariable Long napId){
        return evaluationService.getEvaluationByNapId(napId);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all-by-nap/{napId}")
    public List<Evaluation> getAllEvaluations(@PathVariable Long napId){
        return evaluationService.getAllEvaluations(napId);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PutMapping("/open-old/{evaluationId}")
    public void openOldEvaluation(@PathVariable Long evaluationId){
        evaluationService.openOldEvaluation(evaluationId);
    }
}
