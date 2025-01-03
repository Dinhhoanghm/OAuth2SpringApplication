package vn.aivhub.oauth.config.datasource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DataSourceConfig {
  private final DataSourceDTO dataSourceDTO;

  public DataSourceConfig(DataSourceDTO dataSourceDTO) {
    this.dataSourceDTO = dataSourceDTO;
  }

  @Bean
  public DataSource getDataSource() {
    return DataSourceBuilder.create()
      .driverClassName("org.postgresql.Driver")
      .url(dataSourceDTO.getJdbcUrl())
      .username(dataSourceDTO.getUsername())
      .password(dataSourceDTO.getPassword())
      .build();
  }
}
