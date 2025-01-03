package vn.aivhub.oauth.config.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.stereotype.Service;

@Service
public class DefaultHttpSecurityResolver implements HttpSecurityResolver {
  private static final String[] WHITELIST_URLS = {
    "/register",
    "/login",
    "/grantcode",
    "/github/grantcode",
    "/accountverification",
    "/generateresetpasswordlink",
    "/changepassword",
    "/refreshaccesstoken",
    "/",
    "/index.html",
    "/register.html",
    "/forgot_password.html",
    "/styles.css",
    "/sign-google.jpg",
    "/sing-git.jpg"
  };

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable()
      .exceptionHandling().and()
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
      .authorizeRequests()
      .antMatchers("/file/**").permitAll()
      .antMatchers(WHITELIST_URLS).permitAll()
      .anyRequest()
      .authenticated();
  }
}
