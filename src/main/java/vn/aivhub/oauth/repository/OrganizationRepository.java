package vn.aivhub.oauth.repository;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Repository;
import vn.aivhub.data.tables.pojos.Organization;
import vn.aivhub.data.tables.records.OrganizationRecord;

import java.util.List;

import static vn.aivhub.data.Tables.ORGANIZATION;
import static vn.aivhub.oauth.util.PostgresqlUtil.toInsertQueries;

@Repository
public class OrganizationRepository extends AbsRepository<OrganizationRecord, Organization, Integer> {
  private final DSLContext dslContext;

  public OrganizationRepository(DSLContext dslContext) {
    this.dslContext = dslContext;
  }

  @Override
  protected DSLContext getDslContext() {
    return dslContext;
  }

  @Override
  protected TableImpl<OrganizationRecord> getTable() {
    return ORGANIZATION;
  }

  public Organization findByIdBlocking(Integer id) {
    return dslContext.select()
      .from(ORGANIZATION)
      .where(ORGANIZATION.ID.eq(id))
      .fetchOneInto(Organization.class);
  }

  public List<Organization> findAll(Condition condition) {
    return dslContext.select()
      .from(ORGANIZATION)
      .where(condition)
      .fetchInto(Organization.class);
  }

  public Organization save(Organization organization) {
    dslContext.insertInto(ORGANIZATION)
      .set(toInsertQueries(ORGANIZATION, organization))
      .execute();
    organization.setId(dslContext.lastID().intValue());
    return organization;
  }


  public void delete(Organization organization) {
    dslContext.delete(ORGANIZATION)
      .where(ORGANIZATION.ID.eq(organization.getId()))
      .execute();
  }

  public Organization update(Organization organization) {
    dslContext.update(ORGANIZATION)
      .set(toInsertQueries(ORGANIZATION, organization))
      .where(ORGANIZATION.ID.eq(organization.getId()))
      .execute();
    return organization;
  }

}
