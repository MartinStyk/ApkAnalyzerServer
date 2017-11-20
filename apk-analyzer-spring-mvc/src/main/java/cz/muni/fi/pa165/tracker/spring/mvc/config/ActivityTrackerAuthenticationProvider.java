package cz.muni.fi.pa165.tracker.spring.mvc.config;

import cz.muni.fi.pa165.tracker.dto.UserAuthenticateDTO;
import cz.muni.fi.pa165.tracker.dto.UserDTO;
import cz.muni.fi.pa165.tracker.exception.NonExistingEntityException;
import cz.muni.fi.pa165.tracker.facade.UserFacade;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom authentication provider.
 *
 * @author Martin Styk
 * @version 7.12.2016
 */
@Component
public class ActivityTrackerAuthenticationProvider implements AuthenticationProvider {

    @Inject
    private UserFacade userFacade;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDTO user = null;

        try {
            user = userFacade.findUserByEmail(email);
        } catch (NonExistingEntityException e) {
            return null; //we can not do anything when user doesn't exist
        }

        UserAuthenticateDTO authData = new UserAuthenticateDTO(user.getId(), password);

        if (userFacade.logIn(authData)) {
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

            if (userFacade.isAdmin(user)) {
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            } else {
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            }
            return new UsernamePasswordAuthenticationToken(email, password, grantedAuthorities);
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> authClass) {
        return authClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
