package cz.muni.fi.pa165.tracker.spring.mvc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.inject.Inject;

/**
 * Configure security context of application.
 *
 * @author Martin Styk
 * @version 5.12.2016
 */
@Configuration
@EnableWebSecurity
public class WebApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final Logger log = LoggerFactory.getLogger(WebApplicationSecurityConfiguration.class);

    @Inject
    private ActivityTrackerAuthenticationProvider authenticationProvider;

    /**
     * Initialize authentication in our application - use custom authentication provider
     */
    @Inject
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        log.debug("Configuring security - registering authentication provider");

        auth.authenticationProvider(authenticationProvider);
    }

    /**
     * Configure secured URLs inside out application
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/sports/create/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/sports/update/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/sports/remove/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/sports/**").access("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
                .antMatchers("/reports/**").access("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
                .antMatchers("/settings/**").access("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
                .antMatchers("/users/remove/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/users/makeAdmin/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/users/makeRegular/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/users/**").access("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
                .antMatchers("/teams/**").access("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
                .and()
                .formLogin()
                .loginPage("/login").loginProcessingUrl("/j_spring_security_check")
                .failureUrl("/login?error=invalidLoginAttempt")
                .usernameParameter("user").passwordParameter("pass")
                .and()
                .logout().logoutSuccessUrl("/")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .and()
                .csrf().disable();

    }

}
