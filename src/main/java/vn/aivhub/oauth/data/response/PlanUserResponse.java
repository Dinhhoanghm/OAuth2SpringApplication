package vn.aivhub.oauth.data.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PlanUserResponse {
  private Integer id;
  private Integer userId;
  private Integer planId;
  private String  name;
  private Integer teamSize;
  private Integer projectSize;
  private Double  storage;
  private String  supportType;
  private Double  money;
  private String  status;
}
