package br.com.andrebuarque.financeapi.interceptor;

import br.com.andrebuarque.financeapi.entity.User;
import br.com.andrebuarque.financeapi.service.UserService;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.representations.AccessToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AccessTokenInterceptor implements HandlerInterceptor {
    private final UserService userService;

    public AccessTokenInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (isAnonymousUser(authentication)) {
            return true;
        }

        final User user = saveUser(getAccessToken(authentication));
        request.setAttribute("loggedUser", user);

        return true;
    }

    private User saveUser(final AccessToken accessToken) {
        final User user = extractUserFromAccessToken(accessToken);
        return userService.save(user);
    }

    private User extractUserFromAccessToken(final AccessToken accessToken) {
        return User.builder()
            .id(accessToken.getSubject())
            .username(accessToken.getPreferredUsername())
            .name(accessToken.getGivenName())
            .lastname(accessToken.getFamilyName())
            .email(accessToken.getEmail())
            .build();
    }

    private boolean isAnonymousUser(Authentication authentication) {
        return "anonymousUser".equalsIgnoreCase(authentication.getPrincipal().toString());
    }

    private AccessToken getAccessToken(Authentication authentication) {
        final SimpleKeycloakAccount keycloakAccount = (SimpleKeycloakAccount) authentication.getDetails();
        final KeycloakPrincipal keycloakPrincipal = (KeycloakPrincipal) keycloakAccount.getPrincipal();
        return keycloakPrincipal.getKeycloakSecurityContext().getToken();
    }
}
