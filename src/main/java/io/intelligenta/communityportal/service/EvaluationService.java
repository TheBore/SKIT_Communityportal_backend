package io.intelligenta.communityportal.service;

import io.intelligenta.communityportal.models.Evaluation;
import io.intelligenta.communityportal.models.dto.EvaluationDto;

import java.util.List;

public interface EvaluationService extends BaseEntityCrudService<Evaluation>{

    Evaluation createEvaluation(EvaluationDto evaluation);

    Evaluation updateEvaluation(Long id, EvaluationDto evaluation);

    Evaluation getEvaluationByNapId(Long napId);

    List<Evaluation> getAllEvaluations(Long napId);

    void openOldEvaluation(Long evaluationId);

}
