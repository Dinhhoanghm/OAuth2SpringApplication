package vn.aivhub.oauth.data.mapper;

import org.mapstruct.Context;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.security.core.Authentication;
import vn.aivhub.oauth.data.request.UserRequest;
import vn.aivhub.data.tables.pojos.User;
import vn.aivhub.oauth.data.response.UserResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserRequest toUserRequest(User user);
  @Named("toPojo")
  UserResponse toUserResponse(User user);
  @IterableMapping(qualifiedByName = "toPojo")
  public List<UserResponse> toResponses(List<User> pojos);
}
