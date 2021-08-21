package io.intelligenta.communityportal.models;

import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class PublicDocumentType extends BaseEntity {

    private String name;
}
