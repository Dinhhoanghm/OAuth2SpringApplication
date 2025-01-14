package vn.aivhub.oauth.service;

import org.jooq.impl.DSL;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.aivhub.data.tables.pojos.ChargePlan;
import vn.aivhub.data.tables.pojos.PlanUser;
import vn.aivhub.oauth.data.response.PlanUserResponse;
import vn.aivhub.oauth.repository.ChargePlanRepository;
import vn.aivhub.oauth.repository.ChargeUserRepository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static vn.aivhub.data.tables.ChargePlan.CHARGE_PLAN;
import static vn.aivhub.data.tables.PlanUser.PLAN_USER;
import static vn.aivhub.oauth.util.CollectionUtils.collectToMap;

@Service
public class PlanUserService {
  private final ChargeUserRepository planUserRepository;
  private final PasswordEncoder passwordEncoder;
  private final ChargePlanRepository chargePlanRepository;

  public PlanUserService(ChargeUserRepository planUserRepository, PasswordEncoder passwordEncoder, ChargePlanRepository chargePlanRepository) {
    this.planUserRepository = planUserRepository;
    this.passwordEncoder = passwordEncoder;
    this.chargePlanRepository = chargePlanRepository;
  }

  public List<PlanUser> findAll() {
    return planUserRepository.findAll(DSL.trueCondition());
  }

  public List<PlanUserResponse> findAllPlanByUserId(Integer userId) {
    List<PlanUser> planUsers = planUserRepository.findAll(PLAN_USER.USER_ID.eq(userId));
    List<Integer> planIds = planUsers.stream().map(PlanUser::getPlanId).collect(Collectors.toList());
    List<ChargePlan> chargePlans = chargePlanRepository.findAll(CHARGE_PLAN.ID.in(planIds));
    Map<Integer, ChargePlan> chargePlanMap = collectToMap(chargePlans, ChargePlan::getId);
    List<PlanUserResponse> responses = planUsers.stream().map(s -> {
          ChargePlan chargePlan = chargePlanMap.getOrDefault(s.getPlanId(), null);
          if (chargePlan == null) {
            return null;
          }
          return new PlanUserResponse()
            .setId(s.getId())
            .setName(chargePlan.getName())
            .setProjectSize(chargePlan.getProjectSize())
            .setStorage(chargePlan.getStorage())
            .setMoney(chargePlan.getMoney())
            .setStatus(s.getStatus())
            .setUserId(s.getUserId())
            .setPlanId(s.getPlanId())
            .setSupportType(chargePlan.getSupportType());
        }
      )
      .filter(Objects::nonNull)
      .collect(Collectors.toList());
    return responses;
  }

  public PlanUser addPlanUser(PlanUser planUser) {
    return planUserRepository.save(planUser);
  }

  public PlanUser updatePlanUser(PlanUser planUser) {
    return planUserRepository.update(planUser);
  }

  public boolean deletePlanUser(Integer id) {
    planUserRepository.delete(id);
    return true;
  }
}
