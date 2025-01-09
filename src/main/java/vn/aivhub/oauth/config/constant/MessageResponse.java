package vn.aivhub.oauth.config.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MessageResponse {
    public static final String SUCCESS = "Successful";
    public static final String FAIL = "Failed";
    public static final String RESOURCE_NOT_FOUND = "resource not found!";
    public static final String USER_PASS_INVALID = "Username or password invalid.";
    public static final String INSERT_FAIL = "fail to insert!";
    public static final String UPDATE_FAIL = "fail to update!";
    public static final String ID_MUST_NOT_BE_NULL = "ID_MUST_NOT_BE_NULL";
    public static final String CREATED_MUST_NOT_BE_NULL = "CREATED_MUST_NOT_BE_NULL";
    public static final String NOT_PERMISSION = "You don not have permission!";
}
