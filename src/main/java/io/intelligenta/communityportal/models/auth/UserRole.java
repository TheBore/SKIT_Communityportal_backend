package io.intelligenta.communityportal.models.auth;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority  {
    ROLE_ADMIN,
    ROLE_INSTITUTIONAL_MODERATOR,
    ROLE_USER,
    ROLE_EVALUATOR,
    ROLE_MODERATOR_EVALUATOR;

    public String getAuthority() {
        return name();
    }
}
