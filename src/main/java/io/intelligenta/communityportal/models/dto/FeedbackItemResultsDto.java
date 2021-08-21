package io.intelligenta.communityportal.models.dto;

import io.intelligenta.communityportal.models.exceptions.InvalidChoiceAnswer;
import io.intelligenta.communityportal.models.feedback.FeedbackItem;
import io.intelligenta.communityportal.models.feedback.FeedbackItemAnswer;
import io.intelligenta.communityportal.models.feedback.FeedbackItemType;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
@Data
public class FeedbackItemResultsDto {

    FeedbackItem item;

    private boolean choice = false;


    HashMap<String, Integer> answerFrequency = new HashMap<>();

    public void setItem(FeedbackItem item) {
        this.item = item;
        if (FeedbackItemType.MULTIPLE_CHOICE.equals(item.getType()) ||
                FeedbackItemType.SINGLE_CHOICE.equals(item.getType())) {
            List<String> options = item.getOptions();
            options.forEach(opt -> this.answerFrequency.put(opt, 0));
            this.setChoice(true);
        }
    }

    public void registerAnswer(FeedbackItemAnswer answer) {
        String value = answer.getValue();
        if (this.isChoice() && value.contains(";")) {
            Stream.of(value.split(";")).forEach(this::incrementValue);
        } else {
            this.incrementValue(value);
        }
    }

    private void incrementValue(String value) {

        if (!this.answerFrequency.containsKey(value)) {
            if (this.isChoice()) {
                throw new InvalidChoiceAnswer();
            } else {
                this.answerFrequency.put(value, 0);
            }
        }
        this.answerFrequency.put(value, this.answerFrequency.get(value) + 1);
    }
}
