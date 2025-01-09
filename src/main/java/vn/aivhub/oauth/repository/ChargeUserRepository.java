package vn.aivhub.oauth.repository;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Repository;
import vn.aivhub.data.tables.pojos.PlanUser;
import vn.aivhub.data.tables.records.PlanUserRecord;

import java.util.List;

import static vn.aivhub.data.Tables.PLAN_USER;
import static vn.aivhub.oauth.util.PostgresqlUtil.toInsertQueries;

@Repository
public class ChargeUserRepository extends AbsRepository<PlanUserRecord, PlanUser, Integer> {


  private final DSLContext dslContext;

  public ChargeUserRepository(DSLContext dslContext) {
    this.dslContext = dslContext;
  }

  @Override
  protected DSLContext getDslContext() {
    return dslContext;
  }

  @Override
  protected TableImpl<PlanUserRecord> getTable() {
    return PLAN_USER;
  }


  public List<PlanUser> findByPlanUserId(Integer userId) {
    return dslContext.select()
      .from(PLAN_USER)
      .where(PLAN_USER.USER_ID.eq(userId))
      .fetchInto(PlanUser.class);
  }

  public List<PlanUser> findAll(Condition condition) {
    return dslContext.select()
      .from(PLAN_USER)
      .where(condition)
      .fetchInto(PlanUser.class);
  }

  public List<PlanUser> findByPlanId(Integer planId) {
    return dslContext.select()
      .from(PLAN_USER)
      .where(PLAN_USER.PLAN_ID.eq(planId))
      .fetchInto(PlanUser.class);
  }

  public PlanUser findByIdBlocking(Integer id) {
    return dslContext.select()
      .from(PLAN_USER)
      .where(PLAN_USER.ID.eq(id))
      .fetchOneInto(PlanUser.class);
  }

  public PlanUser save(PlanUser planUser) {
    dslContext.insertInto(PLAN_USER)
      .set(toInsertQueries(PLAN_USER, planUser))
      .execute();
    planUser.setId(dslContext.lastID().intValue());
    return planUser;
  }


  public void delete(PlanUser planUser) {
    dslContext.delete(PLAN_USER)
      .where(PLAN_USER.ID.eq(planUser.getId()))
      .execute();
  }

  public PlanUser update(PlanUser planUser) {
    dslContext.update(PLAN_USER)
      .set(toInsertQueries(PLAN_USER, planUser))
      .where(PLAN_USER.ID.eq(planUser.getId()))
      .execute();
    return planUser;
  }
}
