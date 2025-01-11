package vn.aivhub.oauth.service;

import io.reactivex.rxjava3.core.Single;
import org.jooq.impl.DSL;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.aivhub.data.tables.pojos.User;
import vn.aivhub.oauth.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public List<User> findAll() {
    return userRepository.findAll(DSL.trueCondition());
  }

  public Single<User> getById(Integer id) {
    return userRepository.findById(id)
      .map(optional -> optional.orElseGet(null));
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
