package com.rocksolid.security.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum Role {

    ADMIN(
            Set.of(
                    Permission.ADMIN_READ,
                    Permission.ADMIN_CREATE,
                    Permission.ADMIN_UPDATE,
                    Permission.ADMIN_DELETE
            )
    ),
    REVIEWER(
            Set.of(
                    Permission.REVIEWER_READ,
                    Permission.REVIEWER_CREATE,
                    Permission.REVIEWER_UPDATE,
                    Permission.REVIEWER_DELETE,
                    Permission.STUDENT_READ,
                    Permission.STUDENT_CREATE,
                    Permission.STUDENT_UPDATE,
                    Permission.STUDENT_DELETE
            )
    ),
    STUDENT(
            Set.of(
                    Permission.STUDENT_READ,
                    Permission.STUDENT_CREATE,
                    Permission.STUDENT_UPDATE,
                    Permission.STUDENT_DELETE
            )
    );

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getUserAuthorities() {
        final var autorities = permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        autorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return autorities;
    }
}
