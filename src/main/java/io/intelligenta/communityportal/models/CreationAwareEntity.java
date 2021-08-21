package io.intelligenta.communityportal.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
public class CreationAwareEntity extends BaseEntity {

    @Column(name = "DATE_CREATED")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Europe/Skopje")
    private LocalDateTime dateCreated;

    @Column(name = "DATE_UPDATED")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Europe/Skopje")
    private LocalDateTime dateUpdated;

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDateTime getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(LocalDateTime dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    @PrePersist
    public void prePersist() {
//        this.dateCreated = LocalDateTime.now();
//        this.dateUpdated = LocalDateTime.now();
    }

//    @PreUpdate
//    public void preUpdate() {
//        this.dateUpdated = LocalDateTime.now();
//    }


}
