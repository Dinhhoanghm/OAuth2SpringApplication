package vn.aivhub.oauth.repository;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import vn.aivhub.data.tables.pojos.ResetPasswordToken;

import java.util.Optional;

import static vn.aivhub.data.tables.ResetPasswordToken.RESET_PASSWORD_TOKEN;
import static vn.aivhub.oauth.util.PostgresqlUtil.toInsertQueries;

@Repository
public class ResetPasswordTokenRepository {
  private final DSLContext dslContext;

  public ResetPasswordTokenRepository(DSLContext dslContext) {
    this.dslContext = dslContext;
  }

  public ResetPasswordToken save(ResetPasswordToken resetPasswordToken) {
    dslContext.insertInto(RESET_PASSWORD_TOKEN)
      .set(toInsertQueries(RESET_PASSWORD_TOKEN, resetPasswordToken))
      .execute();
    resetPasswordToken.setId(dslContext.lastID().intValue());
    return resetPasswordToken;
  }

  public Optional<ResetPasswordToken> findById(Integer id) {
    return dslContext.select()
      .from(RESET_PASSWORD_TOKEN)
      .where(RESET_PASSWORD_TOKEN.ID.eq(id))
      .fetchOptionalInto(ResetPasswordToken.class);
  }

  public Optional<ResetPasswordToken> findByToken(String token) {
    return dslContext.select()
      .from(RESET_PASSWORD_TOKEN)
      .where(RESET_PASSWORD_TOKEN.TOKEN.eq(token))
      .fetchOptionalInto(ResetPasswordToken.class);
  }
}
