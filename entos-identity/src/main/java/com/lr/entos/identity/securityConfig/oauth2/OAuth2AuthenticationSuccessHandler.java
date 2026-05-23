package com.lr.entos.identity.securityConfig.oauth2;

import com.lr.entos.identity.entity.User;
import com.lr.entos.identity.securityConfig.properties.OAuth2Properties;
import org.springframework.security.core.GrantedAuthority;
import com.lr.entos.identity.repository.UserRepository;
import com.lr.entos.infra.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final OAuth2Properties oAuth2Properties;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication auth) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) auth.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        // 1. Fetch existing user or provision a new one safely
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> createNewOAuthUser(oAuth2User, email));

        // 2. Build claims map
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("guid", user.getGuid()); // Highly useful for frontend state
        extraClaims.put("name", oAuth2User.getAttribute("name"));
        extraClaims.put("picture", oAuth2User.getAttribute("picture"));
        extraClaims.put("provider", "GOOGLE");

        // 3. Map authorities/roles into your token so @PreAuthorize functions correctly
        List<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        extraClaims.put("roles", roles);

        // 4. Generate token and safely build redirection path
        String token = jwtUtils.generateToken(email, extraClaims);
        String targetUrl = UriComponentsBuilder.fromUriString(oAuth2Properties.authorizedRedirectUri())
                .queryParam("token", token)
                .build()
                .toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private User createNewOAuthUser(OAuth2User oAuth2User, String email) {
        User u = new User();
        u.setEmail(email);
        u.setUsername(generateCustomUsername(email));
        u.setPassword(""); // OAuth users won't use traditional passwords
        // If your User entity defaults a standard role (like ROLE_USER), ensure it's assigned here
        return userRepository.save(u);
    }

    private String generateCustomUsername(String email) {
        String prefix = email.split("@")[0];
        // Fixed the math logic to ensure a neat, distinct numeric suffix
        int randomNum = 1000 + (int) (Math.random() * 9000);
        return prefix + randomNum;
    }
}
