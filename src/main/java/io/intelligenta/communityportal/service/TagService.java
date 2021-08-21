package io.intelligenta.communityportal.service;

import io.intelligenta.communityportal.models.Tag;
import io.intelligenta.communityportal.models.dto.TagWithInstitutionDto;

import java.util.List;

public interface TagService extends BaseEntityCrudService<Tag> {

    Tag createTag(Tag tag);

    Tag findById(Long Id);

    List<Tag> findAllTags();

    Tag updateTag(TagWithInstitutionDto tag);

    void deleteTagById(Long Id);


    List<TagWithInstitutionDto> getAllTagsWithInstituions();
}
