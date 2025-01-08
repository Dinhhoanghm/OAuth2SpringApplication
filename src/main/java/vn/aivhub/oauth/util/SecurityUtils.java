package vn.aivhub.oauth.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import vn.aivhub.oauth.config.model.SimpleSecurityUser;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.math.NumberUtils.createInteger;
import static org.apache.commons.lang3.math.NumberUtils.isDigits;

public class SecurityUtils {
    private SecurityUtils() {
    }

    public static List<String> getRoles(Authentication authentication) {
        if (authentication == null) return new ArrayList<>();
        return authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }
    public static Integer userId(Authentication authentication) {
        if (authentication == null ||
            authentication.getAuthorities() == null ||
            !isDigits(authentication.getPrincipal().toString())) return null;
        return createInteger(authentication.getPrincipal().toString());
    }

    public static Integer bsnId(Authentication authentication) {
        if (authentication == null ||
            authentication.getCredentials() == null) return null;
        if (authentication.getCredentials() instanceof SimpleSecurityUser simpleSecurityUser) {
            return simpleSecurityUser.getBsnId();
        }
        return null;

    }

    public static SimpleSecurityUser extractUserInfo(Authentication authentication) {
        if (authentication == null ||
            authentication.getCredentials() == null) return null;
        if (authentication.getCredentials() instanceof SimpleSecurityUser simpleSecurityUser){
            return simpleSecurityUser;
        }
        return null;
    }

}
