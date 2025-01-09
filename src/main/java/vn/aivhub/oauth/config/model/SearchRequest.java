package vn.aivhub.oauth.config.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;
import vn.aivhub.oauth.config.model.paging.Order;
import vn.aivhub.oauth.config.model.paging.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class SearchRequest {
  private long total;
  private int page;
  private String keyword;
  private int pageSize = 10;
  private LocalDateTime fromDate;
  private LocalDateTime toDate;
  private List<Order> sorts;

  @JsonIgnore
  public Integer getOffset() {
    return Math.max((page - 1) * pageSize, 0);
  }

  public int getPageSize() {
    if (this.pageSize < 0) return Pageable.MAXIMUM_PAGE_SIZE;
    return pageSize;
  }

  public List<Order> getSorts() {
    if (sorts == null) {
      sorts = new ArrayList<>();
    }
    if (sorts.isEmpty()) {
      sorts.add(new Order()
        .setProperty("id")
        .setDirection(Order.Direction.desc.name()));
    }
    return sorts;
  }

  public String getKeyword() {
    if (keyword == null) return null;
    return keyword.trim();
  }

}