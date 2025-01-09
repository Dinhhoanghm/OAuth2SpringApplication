package vn.aivhub.oauth.repository;

import io.reactivex.rxjava3.core.Single;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;
import org.jooq.impl.TableRecordImpl;
import vn.aivhub.oauth.config.constant.MessageResponse;
import vn.aivhub.oauth.config.exception.DBException;
import vn.aivhub.oauth.config.model.SearchRequest;
import vn.aivhub.oauth.config.model.paging.Pageable;
import vn.aivhub.oauth.util.UpdateField;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.partition;
import static java.time.ZonedDateTime.now;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static vn.aivhub.oauth.config.constant.MessageResponse.NOT_PERMISSION;
import static vn.aivhub.oauth.config.constant.MessageResponse.RESOURCE_NOT_FOUND;
import static vn.aivhub.oauth.util.PostgresqlUtil.toInsertQueries;
import static vn.aivhub.oauth.util.PostgresqlUtil.toSortField;
import static vn.aivhub.oauth.util.RxTemplate.rxSchedulerIo;

@Log4j2
public abstract class AbsRepository<R extends TableRecordImpl<R>, P, ID> {
  protected abstract DSLContext getDslContext();

  protected Class<P> pojoClass;

  protected TableField<R, ID> fieldID;
  protected Field<LocalDateTime> deletedField;
  protected Field<Long> createdField;

  protected abstract TableImpl<R> getTable();

  @SneakyThrows
  @PostConstruct
  public void init() {
    log.info("init class {}", this.getClass().getSimpleName());
    this.pojoClass = ((Class<P>) ((ParameterizedType) getClass()
      .getGenericSuperclass()).getActualTypeArguments()[1]);
    this.fieldID = (TableField<R, ID>) Arrays.stream(getTable().fields())
      .filter(field -> field.getName().equalsIgnoreCase("id"))
      .findFirst()
      .orElse(null);
    this.createdField = getTable().field("created_by", Long.class);
    this.deletedField = getTable().field("deleted_at", LocalDateTime.class);
  }


  public Single<Integer> insert(P pojo) {
    return rxSchedulerIo(() -> getDslContext()
      .insertInto(getTable())
      .set(toInsertQueries(getTable(), pojo))
      .execute());
  }

  protected Single<Integer> insert(P pojo, DSLContext context) {
    return rxSchedulerIo(() -> context.insertInto(getTable())
      .set(toInsertQueries(getTable(), pojo))
      .execute());
  }


  public Single<Optional<P>> insertReturning(P pojo) {
    return rxSchedulerIo(() -> ofNullable(getDslContext().insertInto(getTable())
      .set(toInsertQueries(getTable(), pojo))
      .returning()
      .fetchOne())
      .map(r -> r.into(pojoClass)));
  }

  protected Optional<P> insertReturning(P pojo, DSLContext context) {
    return ofNullable(context.insertInto(getTable())
      .set(toInsertQueries(getTable(), pojo))
      .returning()
      .fetchOne())
      .map(r -> r.into(pojoClass));
  }


  public Single<Optional<P>> insertOnDuplicateKeyUpdate(P pojo) {
    return rxSchedulerIo(() -> ofNullable(getDslContext().insertInto(getTable())
      .set(toInsertQueries(getTable(), pojo))
      .onDuplicateKeyUpdate()
      .set(onDuplicateKeyUpdate(pojo))
      .returning()
      .fetchOne())
      .map(r -> r.into(pojoClass)));
  }


  public void insertOnDuplicateKeyUpdateBlocking(P pojo) {
    getDslContext().insertInto(getTable())
      .set(toInsertQueries(getTable(), pojo))
      .onDuplicateKeyUpdate()
      .set(onDuplicateKeyUpdate(pojo))
      .execute();
  }


  public void insertOnDuplicateKeyUpdateBlocking(P pojo, DSLContext context) {
    context.insertInto(getTable())
      .set(toInsertQueries(getTable(), pojo))
      .onDuplicateKeyUpdate()
      .set(onDuplicateKeyUpdate(pojo))
      .execute();
  }


  public void insertOnDuplicateKeyIgnoreBlocking(P pojo, DSLContext context) {
    context.insertInto(getTable())
      .set(toInsertQueries(getTable(), pojo))
      .onDuplicateKeyIgnore()
      .execute();
  }


  public void insertBlocking(Collection<P> pojos, DSLContext context) {
    if (pojos.isEmpty()) return;
    final List<InsertSetMoreStep<R>> insertSetMoreSteps = pojos.stream()
      .map(p -> toInsertQueries(getTable(), p))
      .map(fieldObjectMap -> getDslContext()
        .insertInto(getTable())
        .set(fieldObjectMap))
      .collect(toList());
    log.info("insert into: {} - size {}", getTable().getName(), pojos.size());
    context.batch(insertSetMoreSteps).execute();
  }


  public void insertBlockingOnDuplicateUpdate(Collection<P> pojos) {
    if (pojos.isEmpty()) return;
    insertBlockingOnDuplicateUpdate(pojos, getDslContext());
  }


  public void insertBlockingOnDuplicateUpdate(Collection<P> pojos, DSLContext context) {
    final List<InsertOnDuplicateSetMoreStep<R>> moreStepList = pojos.stream()
      .map(p -> toInsertQueries(getTable(), p))
      .map(fieldObjectMap -> getDslContext()
        .insertInto(getTable())
        .set(fieldObjectMap)
        .onDuplicateKeyUpdate()
        .set(fieldObjectMap))
      .collect(toList());
    getDslContext().batch(moreStepList).execute();
  }


  public void insertBlockingOnDuplicateIgnore(Collection<P> pojos, DSLContext context) {
    List<InsertReturningStep<R>> moreStepList = pojos.stream()
      .map(p -> toInsertQueries(getTable(), p))
      .map(fieldObjectMap -> getDslContext()
        .insertInto(getTable())
        .set(fieldObjectMap)
        .onDuplicateKeyIgnore())
      .collect(toList());
    getDslContext().batch(moreStepList).execute();
  }


  public void insertBlockingOnDuplicateIgnore(Collection<P> pojos) {
    insertBlockingOnDuplicateIgnore(pojos, getDslContext());
  }


  public List<P> getActiveBlocking() {
    return getDslContext()
      .select()
      .from(getTable())
      .where(filterActive())
      .fetchInto(pojoClass);
  }


  public void deleteHard(Condition condition) {
    getDslContext().delete(getTable())
      .where(condition)
      .execute();
  }


  public List<P> getBlockingByCondition(Condition... conditions) {
    return getDslContext()
      .select()
      .from(getTable())
      .where(conditions)
      .fetchInto(pojoClass);
  }


  public Optional<P> getOneBlockingByCondition(Condition... conditions) {
    return getDslContext()
      .select()
      .from(getTable())
      .where(conditions)
      .limit(1)
      .fetchOptionalInto(pojoClass);
  }


  public List<P> getBlockingByCondition(Condition condition, Field<?>... selects) {
    return getDslContext()
      .select(selects)
      .from(getTable())
      .where(condition)
      .fetchInto(pojoClass);
  }


  public Long countBlockingByCondition(Condition... conditions) {
    return getDslContext()
      .selectCount()
      .from(getTable())
      .where(conditions)
      .fetchOneInto(Long.class);
  }


  public Optional<BigDecimal> sumFieldByCondition(Field<? extends Number> field, Condition... conditions) {
    return getDslContext()
      .select(DSL.sum(field))
      .from(getTable())
      .where(conditions)
      .fetchOptionalInto(BigDecimal.class);
  }


  public Map<ID, P> getActiveMapBlocking() {
    return getDslContext()
      .select()
      .from(getTable())
      .where(filterActive())
      .fetchMap(r -> r.get(fieldID), r -> r.into(pojoClass));
  }

  public List<P> findActiveBlockingByIds(Collection<ID> ids) {
    return getDslContext()
      .select()
      .from(getTable())
      .where(filterActive().and(fieldID.in(ids)))
      .fetchInto(pojoClass);
  }


  public P findBlockingById(ID id) {
    return getDslContext()
      .select()
      .from(getTable())
      .where(fieldID.eq(id))
      .fetchOneInto(pojoClass);
  }


  public Map<ID, P> getMapBlockingByIds(Collection<ID> ids) {
    return getDslContext()
      .select()
      .from(getTable())
      .where(filterActive().and(fieldID.in(ids)))
      .fetchMap(r -> r.get(fieldID), r -> r.into(pojoClass));
  }


  public Single<List<Integer>> insert(Collection<P> pojos) {
    final List<InsertSetMoreStep<R>> insertSetMoreSteps = pojos.stream()
      .map(p -> toInsertQueries(getTable(), p))
      .map(fieldObjectMap -> getDslContext()
        .insertInto(getTable())
        .set(fieldObjectMap))
      .collect(toList());

    return rxSchedulerIo(() -> partition(insertSetMoreSteps, 100)
      .stream()
      .flatMap(lists -> Arrays.stream(getDslContext().batch(lists).execute()).boxed())
      .collect(toList()));
  }


  public Single<List<Integer>> insertOnDuplicateKeyIgnore(Collection<P> pojos) {
    final List<InsertReturningStep<R>> stepList = pojos.stream()
      .map(p -> toInsertQueries(getTable(), p))
      .map(fieldObjectMap -> getDslContext()
        .insertInto(getTable())
        .set(fieldObjectMap)
        .onDuplicateKeyIgnore())
      .collect(toList());

    return rxSchedulerIo(() -> partition(stepList, 100)
      .stream()
      .flatMap(lists -> {
        log.info("[INSERT-MANY] class: {}, size: {}",
          pojos.getClass().getSimpleName(), lists.size());
        return Arrays.stream(getDslContext().batch(lists).execute()).boxed();
      })
      .collect(toList()));
  }


  public Single<List<Integer>> insertOnDuplicateKeyUpdate(Collection<P> pojos) {
    final List<InsertOnDuplicateSetMoreStep<R>> moreStepList = pojos.stream()
      .map(p -> toInsertQueries(getTable(), p))
      .map(fieldObjectMap -> getDslContext()
        .insertInto(getTable())
        .set(fieldObjectMap)
        .onDuplicateKeyUpdate()
        .set(fieldObjectMap))
      .collect(toList());

    return rxSchedulerIo(() -> partition(moreStepList, 100)
      .stream()
      .flatMap(lists -> Arrays.stream(getDslContext().batch(lists).execute()).boxed())
      .collect(toList()));
  }


  public Single<Integer> update(ID id, P pojo) {
    if (fieldID != null)
      return rxSchedulerIo(() -> getDslContext().update(getTable())
        .set(toInsertQueries(getTable(), pojo))
        .where(fieldID.eq(id))
        .execute());
    return Single.error(new DBException(MessageResponse.ID_MUST_NOT_BE_NULL));
  }


  public Single<Integer> update(ID id, P pojo, List<String> fields) {
    if (fieldID != null)
      return rxSchedulerIo(() -> getDslContext().update(getTable())
        .set(toInsertQueries(getTable(), pojo, fields))
        .where(fieldID.eq(id))
        .execute());
    return Single.error(new DBException(MessageResponse.ID_MUST_NOT_BE_NULL));
  }


  public Single<Integer> update(ID id, UpdateField updateField) {
    if (fieldID != null)
      return rxSchedulerIo(() -> getDslContext().update(getTable())
        .set(updateField.getFieldValueMap())
        .where(fieldID.eq(id))
        .execute());
    return Single.error(new DBException(MessageResponse.ID_MUST_NOT_BE_NULL));
  }


  public Single<P> updateReturning(ID id, UpdateField updateField) {
    if (fieldID != null)
      return rxSchedulerIo(() -> getDslContext().update(getTable())
        .set(updateField.getFieldValueMap())
        .where(fieldID.eq(id))
        .returning()
        .fetchOne()
        .into(pojoClass));
    return Single.error(new DBException(MessageResponse.ID_MUST_NOT_BE_NULL));
  }


  public P updateReturningBlocking(ID id, UpdateField updateField) {
    if (fieldID != null) {
      AtomicReference<P> result = new AtomicReference<>();
      getDslContext().transaction(config -> {
        DSLContext context = DSL.using(config);
        context.update(getTable())
          .set(updateField.getFieldValueMap())
          .where(fieldID.eq(id))
          .execute();
        result.set(context.select()
          .from(getTable())
          .where(fieldID.eq(id))
          .fetchOneInto(pojoClass));
      });
      return result.get();
    }
    return null;
  }


  public Single<Integer> update(P pojo, Condition condition) {
    return rxSchedulerIo(() -> getDslContext().update(getTable())
      .set(toInsertQueries(getTable(), pojo))
      .where(condition)
      .execute());
  }


  public Single<Integer> update(P pojo, Condition... conditions) {
    return rxSchedulerIo(() -> getDslContext().update(getTable())
      .set(toInsertQueries(getTable(), pojo))
      .where(conditions)
      .execute());
  }


  public Single<Optional<P>> findById(ID id) {
    if (id == null || fieldID == null) {
      return Single.just(Optional.empty());
    }

    return rxSchedulerIo(() -> getDslContext()
      .select()
      .from(getTable())
      .where(fieldID.eq(id).and(filterActive()))
      .limit(1)
      .fetchOptionalInto(pojoClass));
  }


  public Single<Optional<P>> findByIdOfDrugStore(ID id, Long drugStoreId) {
    if (id == null || fieldID == null) {
      return Single.just(Optional.empty());
    }
    Field<Long> drugStoreIdField = (Field<Long>) Arrays.stream(getTable().fields())
      .filter(field -> field.getName().equalsIgnoreCase("drug_store_id"))
      .findFirst()
      .orElse(null);
    if (drugStoreIdField == null) return findById(id);

    return rxSchedulerIo(() -> getDslContext()
      .select()
      .from(getTable())
      .where(fieldID.eq(id)
        .and(filterActive())
        .and(drugStoreIdField.eq(drugStoreId)))
      .limit(1)
      .fetchOptionalInto(pojoClass));
  }


  public Single<Optional<P>> findOne(Condition condition) {
    return rxSchedulerIo(() -> getDslContext()
      .select()
      .from(getTable())
      .where(condition.and(filterActive()))
      .limit(1)
      .fetchOptionalInto(pojoClass));
  }


  public Single<Optional<P>> findByIdWithDeleted(ID id) {
    if (fieldID == null) return Single.just(Optional.empty());
    return rxSchedulerIo(() -> getDslContext()
      .select()
      .from(getTable())
      .where(fieldID.eq(id))
      .limit(1)
      .fetchOptionalInto(pojoClass));
  }


  public Single<List<P>> findAllById(Collection<ID> ids) {
    if (fieldID == null) return Single.just(new ArrayList<>());
    return rxSchedulerIo(() -> getDslContext()
      .select()
      .from(getTable())
      .where(fieldID.in(ids).and(filterActive()))
      .fetchInto(pojoClass));
  }


  public Single<List<P>> getActive() {
    return rxSchedulerIo(() -> getDslContext()
      .select()
      .from(getTable())
      .where(filterActive())
      .fetchInto(pojoClass));
  }


  public Single<Map<ID, P>> getMap(Collection<ID> ids) {
    if (fieldID == null) return Single.just(new HashMap<>());
    return rxSchedulerIo(() -> getDslContext()
      .select()
      .from(getTable())
      .where(fieldID.in(ids).and(filterActive()))
      .fetchMap(r -> r.get(fieldID), r -> r.into(pojoClass)));
  }


  public Single<List<P>> findAllByIdsWithDeleted(Collection<ID> ids) {
    if (fieldID == null) return Single.just(new ArrayList<>());
    return rxSchedulerIo(() -> getDslContext()
      .select()
      .from(getTable())
      .where(fieldID.in(ids))
      .fetchInto(pojoClass));
  }


  public Single<List<P>> findAll() {
    return rxSchedulerIo(() -> getDslContext()
      .select()
      .from(getTable())
      .where(filterActive())
      .fetchInto(pojoClass));
  }


  public Single<List<P>> findAll(Long userId) {
    return rxSchedulerIo(() -> {
      Condition condition = createdField != null ?
        filterActive().and(createdField.eq(userId)) : filterActive();
      return getDslContext().select()
        .from(getTable())
        .where(condition)
        .fetchInto(pojoClass);
    });
  }


  public Single<List<P>> findAllByCondition(Condition condition) {
    return rxSchedulerIo(() -> {
      return getDslContext().select()
        .from(getTable())
        .where(condition)
        .fetchInto(pojoClass);
    });
  }


  public Single<List<P>> getWithOffsetLimit(int offset, int limit) {
    return rxSchedulerIo(() -> getDslContext().select()
      .from(getTable())
      .where(filterActive())
      .offset(offset)
      .limit(limit)
      .fetchInto(pojoClass));
  }


  public Single<Integer> deletedById(ID id) {
    if (fieldID != null) {
      if (deletedField != null)
        return rxSchedulerIo(() -> getDslContext()
          .update(getTable())
          .set(deletedField, now().toLocalDateTime())
          .where(fieldID.eq(id))
          .execute());
      return rxSchedulerIo(() -> getDslContext()
        .deleteFrom(getTable())
        .where(fieldID.eq(id))
        .execute());
    }
    return Single.error(new DBException(MessageResponse.ID_MUST_NOT_BE_NULL));
  }


  public Single<Integer> deletedByIds(List<ID> ids) {
    if (ids.isEmpty()) return Single.just(1);
    if (fieldID != null) {
      if (deletedField != null)
        return rxSchedulerIo(() -> getDslContext().update(getTable())
          .set(deletedField, now().toLocalDateTime())
          .where(fieldID.in(ids))
          .execute());
      return rxSchedulerIo(() -> getDslContext().delete(getTable())
        .where(fieldID.in(ids))
        .execute());
    }
    return Single.error(new DBException(MessageResponse.ID_MUST_NOT_BE_NULL));
  }


  public Single<Integer> deletedByIdWithCreatedBy(ID id, Long userId) {
    if (createdField == null) return Single.error(new DBException(MessageResponse.CREATED_MUST_NOT_BE_NULL));
    if (fieldID == null) return Single.error(new DBException(MessageResponse.ID_MUST_NOT_BE_NULL));
    return checkCreatedBy(id, userId)
      .flatMap(isPermit -> {
        if (!isPermit) return Single.error(new DBException(NOT_PERMISSION));
        if (deletedField != null)
          return rxSchedulerIo(() -> getDslContext().update(getTable())
            .set(deletedField, now().toLocalDateTime())
            .where(fieldID.eq(id))
            .execute());
        return rxSchedulerIo(() -> getDslContext().delete(getTable())
          .where(fieldID.eq(id))
          .execute());
      });
  }

  private Single<Boolean> checkCreatedBy(ID id, Long userId) {
    return rxSchedulerIo(() -> {
      Record fetch = getDslContext().select()
        .from(getTable())
        .where(fieldID.eq(id))
        .limit(1)
        .fetchOptional()
        .orElse(null);
      if (fetch == null) throw new DBException(RESOURCE_NOT_FOUND);
      Long originCreatedBy = fetch.get(createdField, Long.class);
      return Objects.equals(originCreatedBy, userId);
    });
  }

  protected Condition filterActive() {
    if (deletedField != null) return deletedField.isNull();
    return DSL.trueCondition();
  }


  public Single<List<P>> getActiveBySearchRequest(SearchRequest searchRequest, Condition condition) {
    return rxSchedulerIo(() -> getDslContext().select()
      .from(getTable())
      .where(filterActive()
        .and(condition))
      .orderBy(toSortField(searchRequest.getSorts(), getTable().fields()))
      .offset(searchRequest.getOffset())
      .limit(searchRequest.getPageSize())
      .fetchInto(pojoClass));
  }


  public Single<Long> countBySearchRequest(SearchRequest searchRequest, Condition condition) {
    return rxSchedulerIo(() -> getDslContext()
      .select(DSL.count())
      .from(getTable())
      .where(filterActive()
        .and(condition)
      )
      .fetchOneInto(Long.class));
  }


  public Single<List<P>> getActiveByPageable(Pageable pageable) {
    return rxSchedulerIo(() -> getDslContext().select()
      .from(getTable())
      .orderBy(toSortField(pageable.getSorts(), getTable().fields()))
      .offset(pageable.getOffset())
      .limit(pageable.getPageSize())
      .fetchInto(pojoClass));
  }


  public Single<List<P>> getActiveByPageable(Pageable pageable, Long userId) {
    return rxSchedulerIo(() -> getDslContext()
      .select()
      .from(getTable())
      .where(filterActive().and(createdField.eq(userId)))
      .orderBy(toSortField(pageable.getSorts(), getTable().fields()))
      .offset(pageable.getOffset())
      .limit(pageable.getPageSize())
      .fetchInto(pojoClass));
  }


  public Single<List<P>> getActiveByCondition(Condition condition) {
    return getActiveList(condition.and(filterActive()));
  }


  public Single<List<P>> getActiveByPageable(Pageable pageable, Condition condition) {
    return rxSchedulerIo(() -> getActiveBlockingByPageable(pageable, condition));
  }


  public List<P> getActiveBlockingByPageable(Pageable pageable, Condition condition) {
    return getDslContext().select()
      .from(getTable())
      .where(condition.and(filterActive())
      )
      .orderBy(toSortField(pageable.getSorts(), getTable().fields()))
      .offset(pageable.getOffset())
      .limit(pageable.getPageSize())
      .fetchInto(pojoClass);
  }


  public Single<List<ID>> getIdsByPageable(Pageable pageable, Condition condition) {
    return rxSchedulerIo(() -> getDslContext().select(fieldID)
      .from(getTable())
      .where(condition.and(filterActive()))
      .orderBy(toSortField(pageable.getSorts(), getTable().fields()))
      .offset(pageable.getOffset())
      .limit(pageable.getPageSize())
      .fetchInto(fieldID.getType()));
  }


  public Single<Long> countActive() {
    return rxSchedulerIo(() -> getDslContext()
      .select(DSL.count())
      .from(getTable())
      .where(filterActive())
      .fetchOneInto(Long.class));
  }


  public Single<Long> countActive(Long userId) {
    Condition condition = createdField != null ?
      filterActive().and(createdField.eq(userId)) : filterActive();
    return rxSchedulerIo(() -> getDslContext()
      .select(DSL.count())
      .from(getTable())
      .where(condition)
      .fetchOneInto(Long.class));
  }


  public Single<Long> countActive(Condition condition) {
    return rxSchedulerIo(() -> getDslContext()
      .select(DSL.count())
      .from(getTable())
      .where(filterActive().and(condition))
      .fetchOneInto(Long.class));
  }

  protected Single<Integer> update(Condition condition, Map<Field<?>, Object> values) {
    return rxSchedulerIo(() -> getDslContext().update(getTable())
      .set(values)
      .where(condition)
      .execute());
  }

  protected <T> Single<Integer> update(Condition condition, TableField<R, T> tableField, T value) {
    return rxSchedulerIo(() -> getDslContext().update(getTable())
      .set(tableField, value)
      .where(condition)
      .execute());
  }

  protected Single<Optional<P>> getOneOptional(OrderField<?> orderField, Condition... conditions) {
    return rxSchedulerIo(() -> getDslContext().select()
      .from(getTable())
      .where(conditions)
      .orderBy(orderField)
      .limit(1)
      .fetchOptionalInto(pojoClass));
  }

  protected Single<Optional<P>> getOneOptional(Condition condition) {
    return rxSchedulerIo(() -> ofNullable(getDslContext().select()
      .from(getTable())
      .where(condition)
      .limit(1)
      .fetchOneInto(pojoClass)));
  }

  protected Single<Optional<P>> getOneOptional(Condition... conditions) {
    return rxSchedulerIo(() -> ofNullable(getDslContext().select()
      .from(getTable())
      .where(conditions)
      .limit(1)
      .fetchOneInto(pojoClass)));
  }

  protected Single<List<P>> getActiveList(Condition... conditions) {
    List<Condition> conditionArrayList = Arrays.stream(conditions)
      .collect(Collectors.toCollection(ArrayList::new));
    conditionArrayList.add(filterActive());
    return rxSchedulerIo(() -> getDslContext().select()
      .from(getTable())
      .where(conditionArrayList)
      .fetchInto(pojoClass));
  }


  protected Map<Field<?>, Object> onDuplicateKeyUpdate(P p) {
    return toInsertQueries(getTable(), p);
  }

  public Single<List<Integer>> insertOnConflictKeyUpdate(Collection<P> pojos) {
    return rxSchedulerIo(() -> partition((List<P>) pojos, 1000)
      .stream()
      .map(ps -> ps.stream()
        .map(p -> toInsertQueries(getTable(), p))
        .map(fieldObjectMap -> getDslContext()
          .insertInto(getTable())
          .set(fieldObjectMap)
          .onConflict(fieldID)
          .doUpdate()
          .set(fieldObjectMap))
        .collect(toList()))
      .flatMap(lists -> {
        System.out.println("[START-INSERT-DATA, size: ]" + lists.size());
        return Arrays.stream(getDslContext().batch(lists).execute()).boxed();
      })
      .collect(toList()));
  }

  public Single<List<Integer>> insertOnConflictKeyUpdate(Collection<P> pojos, UniqueKey uniqueKey) {
    return rxSchedulerIo(() -> partition((List<P>) pojos, 1000)
      .stream()
      .map(ps -> ps.stream()
        .map(p -> toInsertQueries(getTable(), p))
        .map(fieldObjectMap -> getDslContext()
          .insertInto(getTable())
          .set(fieldObjectMap)
          .onConflictOnConstraint(uniqueKey)
          .doUpdate()
          .set(fieldObjectMap))
        .collect(toList()))
      .flatMap(lists -> {
        System.out.println("[START-INSERT-DATA, size: ]" + lists.size());
        return Arrays.stream(getDslContext().batch(lists).execute()).boxed();
      })
      .collect(toList()));
  }

  public Single<Integer> insertOnConflictKeyUpdate(P pojo) {
    Map<Field<?>, Object> fieldObjectMap = toInsertQueries(getTable(), pojo);
    return rxSchedulerIo(() -> getDslContext()
      .insertInto(getTable())
      .set(fieldObjectMap)
      .onConflict(fieldID)
      .doUpdate()
      .set(fieldObjectMap)
      .execute());
  }


  public P insertBlocking(P pojo) {
    return insertBlocking(pojo, getDslContext());
  }

  public P insertBlocking(P pojo, DSLContext context) {
    return context
      .insertInto(getTable())
      .set(toInsertQueries(getTable(), pojo))
      .returning()
      .fetchOne()
      .map(r -> r.into(pojoClass));
  }

  public Integer updateBlocking(P pojo, Condition condition) {
    return getDslContext().update(getTable())
      .set(toInsertQueries(getTable(), pojo))
      .where(condition)
      .execute();
  }


  @SneakyThrows
  public Integer updateBlocking(ID id, P pojo) {
    if (fieldID == null) {
      throw new DBException("ID_MUST_NOT_BE_NULL");
    }
    return getDslContext().update(getTable())
      .set(toInsertQueries(getTable(), pojo))
      .where(fieldID.eq(id))
      .execute();
  }


  @SneakyThrows
  public void updateBlocking(ID id, P pojo, DSLContext context) {
    if (fieldID == null) {
      throw new DBException("ID_MUST_NOT_BE_NULL");
    }
    context.update(getTable())
      .set(toInsertQueries(getTable(), pojo))
      .where(fieldID.eq(id))
      .execute();
  }

  @SneakyThrows

  public Integer updateBlocking(ID id, UpdateField updateField) {
    return updateBlocking(id, updateField, getDslContext());
  }

  @SneakyThrows

  public Integer updateBlocking(ID id, UpdateField updateField, DSLContext context) {
    if (fieldID == null) {
      throw new DBException("ID_MUST_NOT_BE_NULL");
    }
    return context.update(getTable())
      .set(updateField.getFieldValueMap())
      .where(fieldID.eq(id))
      .execute();
  }


  public Integer updateBlockingByCondition(Condition condition,
                                           UpdateField updateField,
                                           DSLContext context) {
    return context.update(getTable())
      .set(updateField.getFieldValueMap())
      .where(condition)
      .execute();
  }


  public Integer updateBlockingByCondition(Condition condition,
                                           UpdateField updateField) {
    return updateBlockingByCondition(condition, updateField, getDslContext());
  }


  public Single<Integer> hardDeleteById(ID id) {
    if (fieldID == null) {
      return Single.error(new DBException("ID_MUST_NOT_BE_NULL"));
    }
    return rxSchedulerIo(() -> getDslContext().delete(getTable())
      .where(fieldID.eq(id))
      .execute());
  }


  public Single<List<P>> getActiveByDrugStoreId(Long drugStoreId) {
    Field<Long> drugStoreIdField = (Field<Long>) Arrays.stream(getTable().fields())
      .filter(field -> field.getName().equalsIgnoreCase("drug_store_id"))
      .findFirst()
      .orElse(null);
    return drugStoreIdField != null ?
      getActiveList(drugStoreIdField.eq(drugStoreIdField)) :
      getActive();
  }
}
