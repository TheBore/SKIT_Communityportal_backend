package io.intelligenta.communityportal.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AnnPubUserDto {

    private String firstName;

    private String lastName;

    private String title;

    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss", timezone="Europe/Skopje")
    private LocalDateTime readAt;

}
