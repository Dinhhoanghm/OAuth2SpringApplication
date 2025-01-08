package vn.aivhub.oauth.service;

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
}
