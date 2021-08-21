package io.intelligenta.communityportal.web.specifications;

import io.intelligenta.communityportal.models.BaseEntity;
import org.springframework.data.jpa.domain.Specification;

public interface BaseSpecification<T extends BaseEntity> {
  Specification<T> getSpecification(String field, String value);
}
