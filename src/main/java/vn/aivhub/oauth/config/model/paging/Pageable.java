package vn.aivhub.oauth.config.model.paging;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class Pageable {
    public static final Integer DEFAULT_PAGE = 1;
    public static final Integer DEFAULT_PAGE_SIZE = 10;
    public static final Integer MAXIMUM_PAGE_SIZE = 500;

    private int page;
    private String keyword;
    private int pageSize;
    private Integer offset;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private long total;
    private List<Order> sorts;

    private Boolean loadMoreAble;
    private int limit;

    public Pageable() {
        this.page = DEFAULT_PAGE;
        this.pageSize = DEFAULT_PAGE_SIZE;
        this.offset = Math.max((page - 1) * pageSize, 0);
        this.total = 0L;
    }

    public Pageable(int page, int pageSize) {
        this.page = page > 0 ? page : DEFAULT_PAGE;
        this.pageSize = pageSize > 0 ? pageSize : DEFAULT_PAGE_SIZE;
        this.offset = Math.max((page - 1) * pageSize, 0);
        this.total = 0L;
    }

    public Integer getOffset() {
        if (offset == null || offset <= 0) {
            return Math.max((page - 1) * pageSize, 0);
        }
        return offset;
    }

    public String getKeyword() {
        if (keyword == null) return null;
        return keyword.trim();
    }

}
