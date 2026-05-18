package lt.techin.shiftpilot.security.util;

import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.feature.user.model.UserRole;
import lt.techin.shiftpilot.security.principal.UserPrincipal;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

    public UserPrincipal getCurrentUser() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal principal)) {
            throw new AccessDeniedException("Not authenticated");
        }

        return (UserPrincipal) auth.getPrincipal();
    }

    public Long getUserId() {
        return getCurrentUser().getId();
    }

    public UserRole getRole() {
        return getCurrentUser().getRole();
    }
}
