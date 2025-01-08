package vn.aivhub.oauth.repository;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import vn.aivhub.data.tables.pojos.ChargePlan;

import java.util.List;
import java.util.Optional;

import static vn.aivhub.data.Tables.CHARGE_PLAN;
import static vn.aivhub.oauth.util.PostgresqlUtil.toInsertQueries;

@Repository
public class ChargePlanRepository {
  private final DSLContext dslContext;

  public ChargePlanRepository(DSLContext dslContext) {
    this.dslContext = dslContext;
  }


  public ChargePlan findByName(String name) {
    return dslContext.select()
      .from(CHARGE_PLAN)
      .where(CHARGE_PLAN.NAME.likeRegex(name))
      .fetchOneInto(ChargePlan.class);
  }

  public List<ChargePlan> findAll(Condition condition) {
    return dslContext.select()
      .from(CHARGE_PLAN)
      .where(condition)
      .fetchInto(ChargePlan.class);
  }

  public Optional<ChargePlan> findChargePlanById(Integer id) {
    return dslContext.select()
      .from(CHARGE_PLAN)
      .where(CHARGE_PLAN.ID.eq(id))
      .fetchOptionalInto(ChargePlan.class);
  }

  public ChargePlan save(ChargePlan chargePlan) {
    dslContext.insertInto(CHARGE_PLAN)
      .set(toInsertQueries(CHARGE_PLAN, chargePlan))
      .execute();
    chargePlan.setId(dslContext.lastID().intValue());
    return chargePlan;
  }


  public void delete(ChargePlan chargePlan) {
    dslContext.delete(CHARGE_PLAN)
      .where(CHARGE_PLAN.ID.eq(chargePlan.getId()))
      .execute();
  }

  public ChargePlan update(ChargePlan chargePlan) {
    dslContext.update(CHARGE_PLAN)
      .set(toInsertQueries(CHARGE_PLAN, chargePlan))
      .where(CHARGE_PLAN.ID.eq(chargePlan.getId()))
      .execute();
    return chargePlan;
  }
}
