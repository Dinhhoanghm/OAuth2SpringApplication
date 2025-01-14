
package vn.aivhub.oauth.config.model.paging;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;
import vn.aivhub.oauth.util.TimeUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Component
public class PageableParamParser {
  private static HashSet<String> ignoreKey = new HashSet<>() {{
    add("per_page");
    add("page");
    add("from_date");
    add("to_date");
    add("keyword");
    add("page_size");
    add("sort");
    add("offset");
  }};

  public PageableParamParser() {
  }

  public Class<Pageable> type() {
    return Pageable.class;
  }

  public static Pageable parser(Map<String, String[]> parameters) {
    Pageable pageable = new Pageable();
    int page = Pageable.DEFAULT_PAGE;
    if (parameters.containsKey("page") && !isEmpty(parameters.get("page")[0])) {
      page = NumberUtils.toInt(parameters.get("page")[0], Pageable.DEFAULT_PAGE);
    }
    pageable.setPage(page);

    if (parameters.containsKey("from_date") && !isEmpty(parameters.get("from_date")[0])) {
      pageable.setFromDate(TimeUtil.longToLocalDateTimeOrNow(TimeUtil.parserTimeFromDateString(parameters.get("from_date")[0])));
    }
    if (parameters.containsKey("to_date") && !isEmpty(parameters.get("to_date")[0])) {
      pageable.setToDate(TimeUtil.longToLocalDateTimeOrNow(TimeUtil.parserTimeFromDateString(parameters.get("to_date")[0])));
    }

    String keyword = null;
    if (parameters.containsKey("keyword") && !isEmpty(parameters.get("keyword")[0])) {
      keyword = parameters.get("keyword")[0];
    } else if (parameters.containsKey("query") && !isEmpty(parameters.get("query")[0])) {
      keyword = parameters.get("query")[0];
    }
    pageable.setKeyword(keyword);

    int pageSize = Pageable.DEFAULT_PAGE_SIZE;
    if (parameters.containsKey("per_page") && !isEmpty(parameters.get("per_page")[0])) {
      pageSize = NumberUtils.toInt(parameters.get("per_page")[0], Pageable.DEFAULT_PAGE_SIZE);
    } else if (parameters.containsKey("page_size") && !isEmpty(parameters.get("page_size")[0])) {
      pageSize = NumberUtils.toInt(parameters.get("page_size")[0], Pageable.DEFAULT_PAGE_SIZE);
    }

    pageSize = pageSize < 0 ? Pageable.MAXIMUM_PAGE_SIZE : pageSize;
    pageable.setPageSize(pageSize);

    String offset = null;
    if (parameters.containsKey("offset") && !isEmpty(parameters.get("offset")[0])) {
      offset = parameters.get("offset")[0];
    }
    pageable.setOffset(NumberUtils.toInt(offset, (page - 1) * pageSize));

    if (parameters.containsKey("sort") && !isEmpty(parameters.get("sort")[0])) {
      List<Order> orders = getOrder(parameters.get("sort"));
      if (!orders.isEmpty()) pageable.setSorts(orders);
    } else if (parameters.containsKey("sort_by") && !isEmpty(parameters.get("sort_by")[0])) {
      List<Order> orders = getOrderBy_(parameters.get("sort_by"));
      if (!orders.isEmpty()) pageable.setSorts(orders);
    }
    return pageable;
  }

  private static List<Order> getOrder(String[] orders) {
    return orders == null ?
      null :
      Stream.of(orders)
        .filter(Objects::nonNull)
        .map(PageableParamParser::getOrder)
        .filter(Objects::nonNull)
        .collect(toList());
  }

  private static List<Order> getOrderBy_(String[] orders) {
    return orders == null ?
      null :
      Stream.of(orders)
        .filter(Objects::nonNull)
        .map(PageableParamParser::getOrderBy_)
        .collect(toList());
  }

  private static Order getOrder(String order) {
    String[] arr = order.split(",");
    if (arr.length == 1) {
      return new Order(arr[0], Order.Direction.desc.name());
    } else if (arr.length != 2) {
      return null;
    } else {
      return new Order(arr[0], arr[1].toLowerCase());
    }
  }

  private static Order getOrderBy_(String order) {
    String[] arr = order.split("_");
    if (arr.length <= 1 || !(order.endsWith(Order.Direction.desc.name()) || order.endsWith(Order.Direction.asc.name()))) {
      return new Order(order, Order.Direction.desc.name());
    } else {
      String direction = order.substring(order.lastIndexOf("_") + 1);
      String field = order.substring(0, order.lastIndexOf("_"));
      return new Order(field, direction);
    }
  }
}