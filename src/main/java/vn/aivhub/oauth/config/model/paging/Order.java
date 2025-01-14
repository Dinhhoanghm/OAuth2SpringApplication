package vn.aivhub.oauth.config.model.paging;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jooq.Field;

@Data
@Accessors(chain = true)
public class Order {
    private String property;
    private String direction;

    public Order() {
    }

    public Order(String property, String direction) {
        this.property = property;
        this.direction = direction;
    }

    public <T> Order(Field<T> field, Direction direction) {
        this.property = field.getName();
        this.direction = direction.name();
    }

    public enum Direction {
        asc, desc;
    }
}
