package vn.aivhub.oauth.service;

import io.reactivex.rxjava3.core.Single;
import org.jooq.Condition;
import org.jooq.impl.DSL;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.aivhub.data.tables.pojos.User;
import vn.aivhub.data.tables.pojos.Organization;
import vn.aivhub.oauth.data.mapper.UserMapper;
import vn.aivhub.oauth.data.response.UserResponse;
import vn.aivhub.oauth.repository.OrganizationRepository;
import vn.aivhub.oauth.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static vn.aivhub.oauth.util.CollectionUtils.collectToMap;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final OrganizationRepository organizationRepository;
  private final UserMapper userMapper;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, OrganizationRepository organizationRepository, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.organizationRepository = organizationRepository;
    this.userMapper = userMapper;
  }

  public List<User> findAll() {
    return userRepository.findAll(DSL.trueCondition());
  }

  public Single<List<UserResponse>> findAllByCondition(Condition condition) {
    return userRepository.findAllByCondition(condition)
      .flatMap(s -> addExtraInfo(s));
  }
  private Single<List<UserResponse>> addExtraInfo(List<User> users) {
    List<Integer> orgIds = users.stream().map(User::getOrgId).distinct()
      .collect(Collectors.toList());
    List<User> validUsers = users.stream().filter(s -> s.getOrgId() != null).collect(Collectors.toList());
    Map<Integer,Integer> userCompanyMap = collectToMap(validUsers,User::getId,User::getOrgId);
    List<UserResponse> userResponses = userMapper.toResponses(users);
    return organizationRepository.findAllById(orgIds)
      .map(organizations -> {
        Map<Integer,Organization>  organizationMap = collectToMap(organizations,Organization::getId);
        userResponses.stream().forEach(user -> {
          Organization organization = organizationMap.getOrDefault(user.getOrgId(),new Organization());
          user.setCompany(organization.getName());
        });
        return userResponses;
      });
  }
  public Single<UserResponse> getById(Integer id) {
    return userRepository.findById(id)
      .map(optional -> optional.orElseGet(null))
      .flatMap(user -> {
        if (user != null) {
          Integer orgId = user.getOrgId();
          return organizationRepository.findById(orgId)
            .map(organization -> {
              if (organization.isPresent()) {
                UserResponse userResponse = userMapper.toUserResponse(user);
                return userResponse.setCompany(organization.get().getName());
              }
              return null;
            });
        }
        return Single.just(null);
      });
  }

  public User addUser(User user) {
    String password = passwordEncoder.encode(user.getPassword());
    user.setPassword(password);
    return userRepository.save(user);
  }

  public User updateUser(User user) {
    return userRepository.update(user);
  }

  public boolean deleteUser(User user) {
    userRepository.delete(user);
    return true;
  }

  public Single<String> changePassword(Integer userId, String newPassword, String oldPassword) {
    User user = new User();
    user.setId(userId);
    user.setPassword(passwordEncoder.encode(newPassword));
    return checkExist(userId, oldPassword)
      .flatMap(s -> {
        if (!s) {
          return Single.just("Current password is wrong");
        } else {
          return userRepository.updateUserPassWord(user, passwordEncoder.encode(oldPassword));
        }
      });
  }

  public Single<Boolean> checkExist(Integer userId, String password) {
    User user = new User();
    user.setId(userId);
    return userRepository.checkExist(userId)
      .map(s -> {
        return passwordEncoder.matches(password, s.getPassword());
      });
  }
}
