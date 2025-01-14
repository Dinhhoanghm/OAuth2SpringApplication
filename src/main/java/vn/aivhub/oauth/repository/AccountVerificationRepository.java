package vn.aivhub.oauth.repository;

import org.jooq.DSLContext;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Repository;
import vn.aivhub.data.tables.pojos.AccountVerifycation;
import vn.aivhub.data.tables.records.AccountVerifycationRecord;

import java.util.Optional;

import static vn.aivhub.data.tables.AccountVerifycation.ACCOUNT_VERIFYCATION;
import static vn.aivhub.oauth.util.PostgresqlUtil.toInsertQueries;

@Repository
public class AccountVerificationRepository extends AbsRepository<AccountVerifycationRecord, AccountVerifycation, Integer> {

  private final DSLContext dslContext;

  public AccountVerificationRepository(DSLContext dslContext) {
    this.dslContext = dslContext;
  }

  @Override
  protected DSLContext getDslContext() {
    return dslContext;
  }

  @Override
  protected TableImpl<AccountVerifycationRecord> getTable() {
    return ACCOUNT_VERIFYCATION;
  }

  public AccountVerifycation save(AccountVerifycation accountVerifycation) {
    dslContext.insertInto(ACCOUNT_VERIFYCATION)
      .set(toInsertQueries(ACCOUNT_VERIFYCATION, accountVerifycation))
      .execute();
    return accountVerifycation;
  }

  public Optional<AccountVerifycation> findById(String id) {
    return dslContext.select()
      .from(ACCOUNT_VERIFYCATION)
      .where(ACCOUNT_VERIFYCATION.ID.eq(id))
      .fetchOptionalInto(AccountVerifycation.class);
  }

  public void deleteById(String id) {
    dslContext.delete(ACCOUNT_VERIFYCATION)
      .where(ACCOUNT_VERIFYCATION.ID.eq(id))
      .execute();
  }
}
