package vn.aivhub.oauth.config.model.paging;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import lombok.experimental.Accessors;
import vn.aivhub.oauth.config.model.SearchRequest;

import java.util.Collection;
import java.util.List;

@Data
@Accessors(chain = true)
@JsonInclude(Include.NON_NULL)
public class Page<T> {
    private String key;
    private Long total;
    private Integer page;
    private Collection<T> items;
    private String maxId;
    private Boolean loadMoreAble;
    private Boolean preLoadAble;

    public Page() {
    }

    public Page(Long total, Integer page, Collection<T> items) {
        this.page = page;
        this.total = total;
        this.items = items;
    }

    public Page(Long total, Pageable pageable, Collection<T> items) {
        this.total = total;
        this.page = pageable.getPage();
        this.items = items;
        this.loadMoreAble = total != null &&
                (total.intValue() > (pageable.getOffset() + pageable.getPageSize()));
    }

    public Page(Pageable pageable, Collection<T> items) {
        this.items = items;
        this.total = pageable.getTotal();
        this.page = pageable.getPage();
        this.loadMoreAble = pageable.getLoadMoreAble();
    }

    public Page(Long total, SearchRequest searchRequest, List<T> items) {
        this.total = total;
        this.page = searchRequest.getPage();
        this.items = items;
        this.loadMoreAble = total != null &&
                (total.intValue() > (searchRequest.getOffset() + searchRequest.getPageSize()));
    }
}
