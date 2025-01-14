package vn.aivhub.oauth.util;

import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;
import org.jooq.impl.TableRecordImpl;
import org.jooq.impl.UpdatableRecordImpl;
import vn.aivhub.oauth.config.model.paging.Order;

import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class PostgresqlUtil {
  private PostgresqlUtil() {
  }

  public static List<SortField<Object>> toSortField(List<Order> orderProperties, Field<?>[] fields) {
    if (isEmpty(orderProperties)) return new ArrayList<>();
    Set<String> fieldNames = Arrays.stream(fields)
      .map(Field::getName)
      .collect(Collectors.toSet());
    return orderProperties
      .stream()
      .filter(order -> fieldNames.contains(order.getProperty()))
      .map(order -> {
        if (order.getDirection().equals(Order.Direction.asc.name()))
          return DSL.field(order.getProperty()).asc();
        else return DSL.field(order.getProperty()).desc();
      })
      .collect(Collectors.toList());
  }


  public static <T extends UpdatableRecordImpl<T>> Map<Field<?>, Object>
  recordToUpdateQueries(T record, Object o, TableField<T, ?>... ignoreFields) {
    record.from(o);
    Map<Field<?>, Object> values = new HashMap<>();
    for (Field<?> f : record.fields()) {
      if (record.getValue(f) != null) {
        if (Arrays.stream(ignoreFields).noneMatch(f::equals))
          values.put(f, record.getValue(f));
      }
    }
    return values;
  }

  public static <T extends TableRecordImpl<T>> Map<Field<?>, Object>
  toInsertQueries(T record, Object o) {
    record.from(o);
    Map<Field<?>, Object> values = new HashMap<>();
    for (Field<?> f : record.fields()) {
      if (record.getValue(f) != null) {
        values.put(f, record.getValue(f));
      }
    }
    return values;
  }

  public static <R extends Record> Map<Field<?>, Object>
  toInsertQueries(TableImpl<R> table, Object o) {
    R record = table.newRecord();
    record.from(o);
    Map<Field<?>, Object> values = new HashMap<>();
    for (Field<?> f : record.fields()) {
      if (record.getValue(f) != null) {
        values.put(f, record.getValue(f));
      }
    }
    return values;
  }

  public static <T extends TableRecordImpl<T>> Map<Field<?>, Object>
  toInsertQueries(T record, Object o, List<String> fields) {
    record.from(o);
    final HashSet<String> fieldSet = new HashSet<>(fields);
    Map<Field<?>, Object> values = new HashMap<>();
    for (Field<?> f : record.fields()) {
      if (fieldSet.contains(f.getName())) {
        values.put(f, record.getValue(f));
      }
    }
    return values;
  }

  public static <R extends Record> Map<Field<?>, Object>
  toInsertQueries(TableImpl<R> table, Object o, List<String> fields) {
    R record = table.newRecord();
    record.from(o);
    final HashSet<String> fieldSet = new HashSet<>(fields);
    Map<Field<?>, Object> values = new HashMap<>();
    for (Field<?> f : record.fields()) {
      if (fieldSet.contains(f.getName())) {
        values.put(f, record.getValue(f));
      }
    }
    return values;
  }

  public static <R extends Record> Condition buildSearchQueries(TableImpl<R> table, String keyword) {
    if (isEmpty(keyword)) return DSL.noCondition();
    final Condition[] condition = {DSL.noCondition()};
    Arrays.stream(table.fields())
      .filter(field -> String.class.isAssignableFrom(field.getType()) || Long.class.isAssignableFrom(field.getType()))
      .forEach(field -> condition[0] = condition[0].or(field.likeRegex(keyword)));
    return condition[0];
  }
}
