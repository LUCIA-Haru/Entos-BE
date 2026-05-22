package com.lr.entos.identity.securityConfig;

import com.lr.entos.identity.entity.User;
import com.lr.entos.identity.repository.RoleRepository;
import com.lr.entos.identity.repository.UserRepository;
import com.lr.entos.infra.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${entos.oauth2.authorized-redirect-uri}")
    private String redirectUri;

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication auth)
        throws IOException{
        OAuth2User oAuth2User = (OAuth2User) auth.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name",  oAuth2User.getAttribute("name"));
        extraClaims.put("picture",  oAuth2User.getAttribute("picture"));

        extraClaims.put("provider", "GOOGLE");

         userRepository.findByEmail(email).orElseGet(() -> {
            return createNewOAuthUser(oAuth2User, email);
                }
        );

        String token = jwtUtils.generateToken(email,extraClaims);
        String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("token", token).build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);


    }

    private  User createNewOAuthUser(OAuth2User oAuth2User,String email){
        User u = new User();
        u.setEmail(email);
        u.setUsername(generateCustomUsername(email));
        u.setPassword("");
        return userRepository.save(u);
    }

    private String generateCustomUsername(String email){
        String prefix = email.split("@")[0];
        int randomNum = (int) ((Math.random() + 90000) + 1000);
        return prefix + randomNum;
    }
}
