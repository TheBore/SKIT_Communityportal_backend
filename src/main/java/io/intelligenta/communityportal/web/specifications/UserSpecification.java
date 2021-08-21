package io.intelligenta.communityportal.web.specifications;

import io.intelligenta.communityportal.models.auth.User;
import io.intelligenta.communityportal.web.specifications.utils.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author Konstantin Bogdanoski (konstantin.b@live.com)
 */
public class UserSpecification implements Specification<User> {
    private SearchCriteria criteria;

    public UserSpecification(final SearchCriteria criteria) {
        super();
        this.criteria = criteria;
    }

    @Override
    public Specification<User> and(Specification<User> other) {
        return null;
    }

    @Override
    public Specification<User> or(Specification<User> other) {
        return null;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (criteria.getOperation().equals(":"))
            return builder.equal(root.get(criteria.getKey()), criteria.getValue());
        if (criteria.getOperation().equals("null"))
            return builder.isNull(root.get(criteria.getKey()));
        if (criteria.getOperation().equals("not null"))
            return builder.isNotNull(root.get(criteria.getKey()));
        return null;
    }
}
