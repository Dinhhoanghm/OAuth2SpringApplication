package vn.aivhub.oauth.repository;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Repository;
import vn.aivhub.data.tables.pojos.BillingHistory;
import vn.aivhub.data.tables.records.BillingHistoryRecord;

import java.util.List;
import java.util.Optional;

import static vn.aivhub.data.Tables.BILLING_HISTORY;
import static vn.aivhub.oauth.util.PostgresqlUtil.toInsertQueries;

@Repository
public class BillingHistoryRepository extends AbsRepository<BillingHistoryRecord, BillingHistory, Integer> {
  private final DSLContext dslContext;

  public BillingHistoryRepository(DSLContext dslContext) {
    this.dslContext = dslContext;
  }

  @Override
  protected DSLContext getDslContext() {
    return dslContext;
  }

  @Override
  protected TableImpl<BillingHistoryRecord> getTable() {
    return BILLING_HISTORY;
  }

  public BillingHistory findByIdBlocking(Integer id) {
    return dslContext.select()
      .from(BILLING_HISTORY)
      .where(BILLING_HISTORY.ID.eq(id))
      .fetchOneInto(BillingHistory.class);
  }

  public List<BillingHistory> findAll(Condition condition) {
    return dslContext.select()
      .from(BILLING_HISTORY)
      .where(condition)
      .fetchInto(BillingHistory.class);
  }

  public Optional<BillingHistory> findBillingHistoryByPlanUserId(Integer planUserId) {
    return dslContext.select()
      .from(BILLING_HISTORY)
      .where(BILLING_HISTORY.PLAN_USER_ID.eq(planUserId))
      .fetchOptionalInto(BillingHistory.class);
  }

  public BillingHistory save(BillingHistory billingHistory) {
    dslContext.insertInto(BILLING_HISTORY)
      .set(toInsertQueries(BILLING_HISTORY, billingHistory))
      .execute();
    billingHistory.setId(dslContext.lastID().intValue());
    return billingHistory;
  }


  public void delete(BillingHistory billingHistory) {
    dslContext.delete(BILLING_HISTORY)
      .where(BILLING_HISTORY.ID.eq(billingHistory.getId()))
      .execute();
  }

  public BillingHistory update(BillingHistory billingHistory) {
    dslContext.update(BILLING_HISTORY)
      .set(toInsertQueries(BILLING_HISTORY, billingHistory))
      .where(BILLING_HISTORY.ID.eq(billingHistory.getId()))
      .execute();
    return billingHistory;
  }

  public void updateBySessionId(String sessionId, String status) {
    dslContext.update(BILLING_HISTORY)
      .set(BILLING_HISTORY.STATUS, status)
      .where(BILLING_HISTORY.SESSION_ID.eq(sessionId))
      .execute();
  }
}
