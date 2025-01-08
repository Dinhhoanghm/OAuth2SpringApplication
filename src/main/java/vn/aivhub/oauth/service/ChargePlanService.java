package vn.aivhub.oauth.service;

import org.jooq.impl.DSL;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.aivhub.data.tables.pojos.ChargePlan;
import vn.aivhub.oauth.repository.ChargePlanRepository;

import java.util.List;

@Service
public class ChargePlanService {
  private final ChargePlanRepository chargePlanRepository;
  private final PasswordEncoder passwordEncoder;

  public ChargePlanService(ChargePlanRepository chargePlanRepository, PasswordEncoder passwordEncoder) {
    this.chargePlanRepository = chargePlanRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public List<ChargePlan> findAll() {
    return chargePlanRepository.findAll(DSL.trueCondition());
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
