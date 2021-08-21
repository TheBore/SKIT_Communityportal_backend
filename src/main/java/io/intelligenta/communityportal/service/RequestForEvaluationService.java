package io.intelligenta.communityportal.service;

import io.intelligenta.communityportal.models.RequestForEvaluation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RequestForEvaluationService {

    Page<RequestForEvaluation> findAllPaged(Pageable pageable);

    void sendRequestsForEvaluation(List<Long> institutionIds, String title, String body);

    void sendRequestsForEvaluationTags(List<Long> tagsIds, String title, String body);

    void notifyDirectors(List<String> emails, String message);
}

