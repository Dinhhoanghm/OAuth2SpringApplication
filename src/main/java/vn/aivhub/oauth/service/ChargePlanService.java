package vn.aivhub.oauth.service;

import io.reactivex.rxjava3.core.Single;
import org.jooq.impl.DSL;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.aivhub.data.tables.pojos.ChargePlan;
import vn.aivhub.oauth.config.exception.ApiException;
import vn.aivhub.oauth.repository.ChargePlanRepository;
import vn.aivhub.oauth.repository.ChargeUserRepository;

import java.util.List;

@Service
public class ChargePlanService {
  private final ChargePlanRepository chargePlanRepository;
  private final PasswordEncoder passwordEncoder;
  private final ChargeUserRepository chargeUserRepository;

  public ChargePlanService(ChargePlanRepository chargePlanRepository, PasswordEncoder passwordEncoder, ChargeUserRepository chargeUserRepository) {
    this.chargePlanRepository = chargePlanRepository;
    this.passwordEncoder = passwordEncoder;
    this.chargeUserRepository = chargeUserRepository;
  }

  public List<ChargePlan> findAll() {
    return chargePlanRepository.findAll(DSL.trueCondition());
  }

  public Single<ChargePlan> findBySubscriptionId(Integer subscriptionId) throws ApiException {
    return chargeUserRepository.findById(subscriptionId)
      .map(optional -> {
        if (!optional.isPresent()) {
          throw new ApiException("Not exist subscription");
        }
        return optional.get();
      })
      .flatMap(s -> {
        Integer planId = s.getPlanId();
        return chargePlanRepository.findById(planId);
      })
      .map(s -> {
        if (s.isEmpty()) {
          throw new ApiException("Not exist plan");
        }
        return s.get();
      });
  }


  public ChargePlan addChargePlan(ChargePlan chargePlan) {
    return chargePlanRepository.save(chargePlan);
  }

  public ChargePlan updateChargePlan(ChargePlan chargePlan) {
    return chargePlanRepository.update(chargePlan);
  }

  public boolean deleteChargePlan(ChargePlan chargePlan) {
    chargePlanRepository.delete(chargePlan);
    return true;
  }
}
