package io.intelligenta.communityportal.service.impl;

import io.intelligenta.communityportal.models.Institution;
import io.intelligenta.communityportal.models.Tag;
import io.intelligenta.communityportal.models.dto.TagWithInstitutionDto;
import io.intelligenta.communityportal.models.exceptions.TagNotDeletedException;
import io.intelligenta.communityportal.models.exceptions.TagNotFoundException;
import io.intelligenta.communityportal.repository.TagRepository;
import io.intelligenta.communityportal.service.TagService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl extends BaseEntityCrudServiceImpl<Tag, TagRepository> implements TagService {

    private TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Tag createTag(Tag tag) {
        if (tag.getTagNameMk() == null) {
            tag.setTagNameMk(tag.getTagNameAl());
        }
        if (tag.getTagNameAl() == null) {
            tag.setTagNameAl(tag.getTagNameMk());
        }

        return tagRepository.save(tag);
    }

    @Override
    public Tag updateTag(TagWithInstitutionDto tag) {
        Tag updatedTag = tagRepository.findById(tag.getId()).orElseThrow(TagNotFoundException::new);
        updatedTag.setTagNameMk(tag.getNameMk());
        updatedTag.setTagNameAl(tag.getNameAl());
        return tagRepository.save(updatedTag);
    }

    @Override
    public Tag findById(Long Id) {
        return tagRepository.findById(Id).orElseThrow(TagNotFoundException::new);
    }

    @Override
    public List<Tag> findAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public void deleteTagById(Long Id) throws TagNotDeletedException{
        Tag tag = this.tagRepository.findById(Id).orElseThrow(TagNotFoundException::new);

        tag.getInstitutions().forEach(i -> i.getTags().remove(tag));
        tagRepository.deleteById(Id);
    }

    @Override
    public List<TagWithInstitutionDto> getAllTagsWithInstituions() {
        List<Tag> tagList = tagRepository.findAll();
        List<TagWithInstitutionDto> tags = new ArrayList<>();
        tagList.forEach(tag -> {
            TagWithInstitutionDto dto = new TagWithInstitutionDto();
            dto.setId(tag.getId());
            dto.setNameMk(tag.getTagNameMk());
            dto.setNameAl(tag.getTagNameAl());
            List<Institution> institutions = tag.getInstitutions().stream().filter(Institution::isActive).filter(institution -> !institution.isEdited()).collect(Collectors.toList());
            dto.setInstitutions(institutions);
            tags.add(dto);
        });
        return tags;
    }

    @Override
    protected TagRepository getRepository() {
        return tagRepository;
    }

}
