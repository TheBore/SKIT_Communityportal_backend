package io.intelligenta.communityportal.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
@Entity
@Data
public class Tag extends BaseEntity {

    @Column(unique = true)
    private String tagNameMk;

    @Column(unique = true)
    private String tagNameAl;

    @JsonIgnore
    @ManyToMany(mappedBy = "tags")
    List<Institution> institutions;

    @Override
    public String toString() {
        return "Tag{" +
                "tagNameMk='" + tagNameMk + '\'' +
                ", tagNameAl='" + tagNameAl + '\'' +
                '}';
    }
}
