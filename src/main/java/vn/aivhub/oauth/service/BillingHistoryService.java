package vn.aivhub.oauth.service;

import org.jooq.impl.DSL;
import org.springframework.stereotype.Service;
import vn.aivhub.data.tables.pojos.BillingHistory;
import vn.aivhub.oauth.repository.BillingHistoryRepository;

import java.util.List;

@Service
public class BillingHistoryService {
  private final BillingHistoryRepository billingHistoryRepository;

  public BillingHistoryService(BillingHistoryRepository billingHistoryRepository) {
    this.billingHistoryRepository = billingHistoryRepository;
  }


  public List<BillingHistory> findAll() {
    return billingHistoryRepository.findAll(DSL.trueCondition());
  }

  public BillingHistory addBillingHistory(BillingHistory billingHistory) {
    return billingHistoryRepository.save(billingHistory);
  }

  public BillingHistory updateBillingHistory(BillingHistory billingHistory) {
    return billingHistoryRepository.update(billingHistory);
  }

  public boolean deleteBillingHistory(BillingHistory billingHistory) {
    billingHistoryRepository.delete(billingHistory);
    return true;
  }
}
