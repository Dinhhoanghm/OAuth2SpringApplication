package vn.aivhub.oauth.repository;

import io.reactivex.rxjava3.core.Single;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Repository;
import vn.aivhub.data.tables.pojos.User;
import vn.aivhub.data.tables.records.UserRecord;

import java.util.List;
import java.util.Optional;

import static vn.aivhub.data.Tables.USER;
import static vn.aivhub.oauth.util.PostgresqlUtil.toInsertQueries;
import static vn.aivhub.oauth.util.RxTemplate.rxSchedulerIo;

@Repository
public class UserRepository extends AbsRepository<UserRecord, User, Integer> {
  private final DSLContext dslContext;

  public UserRepository(DSLContext dslContext) {
    this.dslContext = dslContext;
  }

  @Override
  protected DSLContext getDslContext() {
    return dslContext;
  }

  @Override
  protected TableImpl<UserRecord> getTable() {
    return USER;
  }

  public User findByEmail(String email) {
    return dslContext.select()
      .from(USER)
      .where(USER.EMAIL.eq(email))
      .fetchOneInto(User.class);
  }

  public List<User> findAll(Condition condition) {
    return dslContext.select()
      .from(USER)
      .where(condition)
      .fetchInto(User.class);
  }

  public Optional<User> findUserById(Integer id) {
    return dslContext.select()
      .from(USER)
      .where(USER.ID.eq(id))
      .fetchOptionalInto(User.class);
  }

  public User save(User user) {
    dslContext.insertInto(USER)
      .set(toInsertQueries(USER, user))
      .execute();
    user.setId(dslContext.lastID().intValue());
    return user;
  }


  public void delete(User user) {
    dslContext.delete(USER)
      .where(USER.ID.eq(user.getId()))
      .execute();
  }

  public User update(User user) {
    dslContext.update(USER)
      .set(toInsertQueries(USER, user))
      .where(USER.ID.eq(user.getId()))
      .execute();
    return user;
  }

  public Single<String> updateUserPassWord(User user, String oldPassword) {
    return rxSchedulerIo(() -> {
      dslContext.update(USER)
        .set(USER.PASSWORD, user.getPassword())
        .where(USER.ID.eq(user.getId())
          .and(USER.PASSWORD.eq(oldPassword)))
        .execute();
      return "Successful update password";
    });
  }
  public Single<User> checkExist(Integer userId) {
    return rxSchedulerIo(() -> {
      return dslContext.select()
        .from(USER)
        .where(USER.ID.eq(userId))
        .fetchOneInto(User.class);
    });
  }
}
