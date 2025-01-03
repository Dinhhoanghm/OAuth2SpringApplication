package vn.aivhub.oauth.config.datasource;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
@Accessors(chain = true)
public class DataSourceDTO {
  @Value("${spring.datasource.jdbc-url}")
  private String jdbcUrl;
  @Value("${spring.datasource.username}")
  private String username;
  @Value("${spring.datasource.password}")
  private String password;
}
