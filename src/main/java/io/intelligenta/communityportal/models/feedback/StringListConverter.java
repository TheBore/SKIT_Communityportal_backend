package io.intelligenta.communityportal.models.feedback;

import javax.persistence.AttributeConverter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringListConverter implements AttributeConverter<List<String>, String> {
    @Override
    public String convertToDatabaseColumn(List<String> strings) {
        if (strings == null) return null;
        return strings.stream().collect(Collectors.joining(";;"));
    }

    @Override
    public List<String> convertToEntityAttribute(String s) {
        if (s == null) return null;
        return Stream.of(s.split(";;")).collect(Collectors.toList());
    }
}
