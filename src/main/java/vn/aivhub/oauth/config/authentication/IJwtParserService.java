package vn.aivhub.oauth.config.authentication;
import vn.aivhub.oauth.config.model.SimpleSecurityUser;

public interface IJwtParserService {
  SimpleSecurityUser parserUser(String token);
}
