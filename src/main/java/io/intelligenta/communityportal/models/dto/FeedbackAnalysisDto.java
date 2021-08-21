package io.intelligenta.communityportal.models.dto;

import io.intelligenta.communityportal.models.feedback.Feedback;
import io.intelligenta.communityportal.models.feedback.FeedbackItem;
import io.intelligenta.communityportal.models.feedback.FeedbackItemAnswer;

import java.util.HashMap;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */

public class FeedbackAnalysisDto {

    Feedback feedback;

    HashMap<Long, FeedbackItemResultsDto> items = new HashMap<>();

    public void registerAnswer(FeedbackItemAnswer answer) {
        if (this.feedback == null) {
            this.setFeedback(answer.getItem().getFeedback());
        }
        if (!this.items.containsKey(answer.getItem().getId())) {
            FeedbackItemResultsDto itemDto = new FeedbackItemResultsDto();
            itemDto.setItem(answer.getItem());
            this.items.put(answer.getItem().getId(), itemDto);

        }
        FeedbackItemResultsDto itemDto = this.items.get(answer.getItem().getId());
        itemDto.registerAnswer(answer);
    }

    @Override
    public String toString() {
        return "FeedbackAnalysisDto{" +
                "feedback=" + feedback +
                ", items=" + items +
                '}';
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public HashMap<Long, FeedbackItemResultsDto> getItems() {
        return items;
    }

    public void setItems(HashMap<Long, FeedbackItemResultsDto> items) {
        this.items = items;
    }
}
