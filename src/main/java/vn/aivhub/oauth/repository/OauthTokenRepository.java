package vn.aivhub.oauth.repository;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import vn.aivhub.data.tables.pojos.OauthToken;
import vn.aivhub.data.tables.pojos.User;

import static vn.aivhub.data.tables.OauthToken.OAUTH_TOKEN;
import static vn.aivhub.oauth.util.PostgresqlUtil.toInsertQueries;

@Repository
public class OauthTokenRepository {
  private final DSLContext dslContext;

  public OauthTokenRepository(DSLContext dslContext) {
    this.dslContext = dslContext;
  }

  public OauthToken save(OauthToken oauthToken) {
    dslContext.insertInto(OAUTH_TOKEN)
      .set(toInsertQueries(OAUTH_TOKEN, oauthToken))
      .execute();
    oauthToken.setId(dslContext.lastID().intValue());
    return oauthToken;
  }

  public OauthToken updateAccessToken(OauthToken oauthToken) {
    dslContext.update(OAUTH_TOKEN)
      .set(OAUTH_TOKEN.ACCESS_TOKEN, oauthToken.getAccessToken())
      .where(OAUTH_TOKEN.ID.eq(oauthToken.getId()))
      .execute();
    oauthToken.setId(dslContext.lastID().intValue());
    return oauthToken;
  }


  public void deleteByUser(User user) {
    dslContext.deleteFrom(OAUTH_TOKEN)
      .where(OAUTH_TOKEN.USER_ID.eq(user.getId()))
      .execute();
  }

  public void delete(OauthToken oauthToken) {
    dslContext.deleteFrom(OAUTH_TOKEN)
      .where(OAUTH_TOKEN.ID.eq(oauthToken.getId()))
      .execute();
  }


  public OauthToken findByRefreshToken(String refreshToken) {
    return dslContext.select()
      .from(OAUTH_TOKEN)
      .where(OAUTH_TOKEN.REFRESH_TOKEN.eq(refreshToken))
      .fetchOneInto(OauthToken.class);
  }
}
