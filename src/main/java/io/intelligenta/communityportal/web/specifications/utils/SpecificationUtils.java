package io.intelligenta.communityportal.web.specifications.utils;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.*;

import javax.persistence.criteria.*;

public class SpecificationUtils {

    public static Path getExpression(Root root, String expr) {
        String[] path = expr.split("\\.");
        Path expression = root;
        for (String p : path) {
            expression = expression.get(p);
        }
        return expression;
    }


    public static Path getJoinExpression(Root root, String expr) {
        String[] path = expr.split("\\.");
        if (path.length == 1) {
            return root.get(expr);
        } else {
            Join expression = root.join(path[0]);
            for (int i = 1; i < path.length - 1; i++) {
                expression = expression.join(path[i]);
            }

            return expression.get(path[path.length - 1]);
        }
    }

    public interface Operation {

        Predicate execute(CriteriaBuilder cb, Expression expr, Object... params);
    }


    public static Operation equal = new Operation() {
        @Override
        public Predicate execute(final CriteriaBuilder cb, final Expression expr, final Object... params) {
            return cb.equal(expr, params[0]);
        }
    };


    public static Operation greaterThan = new Operation() {
        @Override
        public Predicate execute(final CriteriaBuilder cb, final Expression expr, final Object... params) {
            Comparable from = (Comparable) params[0];
            Comparable to = (Comparable) params[1];
            return cb.greaterThan(expr, from);
        }
    };

    public static Operation greaterThanOrEqualTo = new Operation() {
        @Override
        public Predicate execute(final CriteriaBuilder cb, final Expression expr, final Object... params) {
            Comparable from = (Comparable) params[0];
            Comparable to = (Comparable) params[1];
            return cb.greaterThanOrEqualTo(expr, from);
        }
    };


    public static Operation lessThan = new Operation() {
        @Override
        public Predicate execute(final CriteriaBuilder cb, final Expression expr, final Object... params) {
            Comparable from = (Comparable) params[0];
            Comparable to = (Comparable) params[1];
            return cb.lessThan(expr, from);
        }
    };


    public static Operation lessThanOrEqualTo = new Operation() {
        @Override
        public Predicate execute(final CriteriaBuilder cb, final Expression expr, final Object... params) {
            Comparable from = (Comparable) params[0];
            Comparable to = (Comparable) params[1];
            return cb.lessThanOrEqualTo(expr, from);
        }
    };


    public static <T> Specification<T> equal(final String expr, final Object val) {
        return equal(expr, val, false);
    }


    public static <T> Specification<T> equal(final String expr, final Object val, final boolean joined) {

        return Specification.where(new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);
                if (joined)
                    return cb.equal(getJoinExpression(root, expr), val);
                else
                    return cb.equal(getExpression(root, expr), val);
            }
        });
    }

    public static <T> Specification<T> notEqual(final String expr, final Object val) {
        return Specifications.where(new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.notEqual(getExpression(root, expr), val);
            }
        });
    }

    public static <T> Specification<T> fn(final Operation operation, final String fn, final String expr, final Object value) {
        return Specification.where(new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Expression expression = getExpression(root, expr);
                Class valueClass = value.getClass();
                Expression fnExpression = cb.function(fn, valueClass, expression);
                return operation.execute(cb, fnExpression, value);
            }
        });
    }
}
