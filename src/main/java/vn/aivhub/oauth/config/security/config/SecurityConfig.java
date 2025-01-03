package vn.aivhub.oauth.config.security.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import vn.aivhub.oauth.config.authentication.JwtAuthenticationFilter;
import vn.aivhub.oauth.config.security.HttpSecurityResolver;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final HttpSecurityResolver httpSecurityResolver;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;


  public SecurityConfig(
    HttpSecurityResolver httpSecurityResolver, JwtAuthenticationFilter jwtAuthenticationFilter) {
    this.httpSecurityResolver = httpSecurityResolver;

    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
  }


  @Override
  protected void configure(HttpSecurity http) throws Exception {
    httpSecurityResolver.configure(http);
    http.addFilterAfter(jwtAuthenticationFilter, BasicAuthenticationFilter.class);
  }

  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers("/swagger-ui/**", "/v3/api-docs/**");
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManager() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
