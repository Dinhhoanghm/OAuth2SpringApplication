/*
 * This file is generated by jOOQ.
 */
package vn.aivhub.data.tables.pojos;


import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class BillingHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer       id;
    private Integer       planUserId;
    private String        status;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;
    private Double        amount;
    private String        sessionId;

    public BillingHistory() {}

    public BillingHistory(BillingHistory value) {
        this.id = value.id;
        this.planUserId = value.planUserId;
        this.status = value.status;
        this.createdAt = value.createdAt;
        this.paidAt = value.paidAt;
        this.amount = value.amount;
        this.sessionId = value.sessionId;
    }

    public BillingHistory(
        Integer       id,
        Integer       planUserId,
        String        status,
        LocalDateTime createdAt,
        LocalDateTime paidAt,
        Double        amount,
        String        sessionId
    ) {
        this.id = id;
        this.planUserId = planUserId;
        this.status = status;
        this.createdAt = createdAt;
        this.paidAt = paidAt;
        this.amount = amount;
        this.sessionId = sessionId;
    }

    /**
     * Getter for <code>public.billing_history.id</code>.
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * Setter for <code>public.billing_history.id</code>.
     */
    public BillingHistory setId(Integer id) {
        this.id = id;
        return this;
    }

    /**
     * Getter for <code>public.billing_history.plan_user_id</code>.
     */
    public Integer getPlanUserId() {
        return this.planUserId;
    }

    /**
     * Setter for <code>public.billing_history.plan_user_id</code>.
     */
    public BillingHistory setPlanUserId(Integer planUserId) {
        this.planUserId = planUserId;
        return this;
    }

    /**
     * Getter for <code>public.billing_history.status</code>.
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * Setter for <code>public.billing_history.status</code>.
     */
    public BillingHistory setStatus(String status) {
        this.status = status;
        return this;
    }

    /**
     * Getter for <code>public.billing_history.created_at</code>.
     */
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Setter for <code>public.billing_history.created_at</code>.
     */
    public BillingHistory setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Getter for <code>public.billing_history.paid_at</code>.
     */
    public LocalDateTime getPaidAt() {
        return this.paidAt;
    }

    /**
     * Setter for <code>public.billing_history.paid_at</code>.
     */
    public BillingHistory setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
        return this;
    }

    /**
     * Getter for <code>public.billing_history.amount</code>.
     */
    public Double getAmount() {
        return this.amount;
    }

    /**
     * Setter for <code>public.billing_history.amount</code>.
     */
    public BillingHistory setAmount(Double amount) {
        this.amount = amount;
        return this;
    }

    /**
     * Getter for <code>public.billing_history.session_id</code>.
     */
    public String getSessionId() {
        return this.sessionId;
    }

    /**
     * Setter for <code>public.billing_history.session_id</code>.
     */
    public BillingHistory setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final BillingHistory other = (BillingHistory) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        if (planUserId == null) {
            if (other.planUserId != null)
                return false;
        }
        else if (!planUserId.equals(other.planUserId))
            return false;
        if (status == null) {
            if (other.status != null)
                return false;
        }
        else if (!status.equals(other.status))
            return false;
        if (createdAt == null) {
            if (other.createdAt != null)
                return false;
        }
        else if (!createdAt.equals(other.createdAt))
            return false;
        if (paidAt == null) {
            if (other.paidAt != null)
                return false;
        }
        else if (!paidAt.equals(other.paidAt))
            return false;
        if (amount == null) {
            if (other.amount != null)
                return false;
        }
        else if (!amount.equals(other.amount))
            return false;
        if (sessionId == null) {
            if (other.sessionId != null)
                return false;
        }
        else if (!sessionId.equals(other.sessionId))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.planUserId == null) ? 0 : this.planUserId.hashCode());
        result = prime * result + ((this.status == null) ? 0 : this.status.hashCode());
        result = prime * result + ((this.createdAt == null) ? 0 : this.createdAt.hashCode());
        result = prime * result + ((this.paidAt == null) ? 0 : this.paidAt.hashCode());
        result = prime * result + ((this.amount == null) ? 0 : this.amount.hashCode());
        result = prime * result + ((this.sessionId == null) ? 0 : this.sessionId.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("BillingHistory (");

        sb.append(id);
        sb.append(", ").append(planUserId);
        sb.append(", ").append(status);
        sb.append(", ").append(createdAt);
        sb.append(", ").append(paidAt);
        sb.append(", ").append(amount);
        sb.append(", ").append(sessionId);

        sb.append(")");
        return sb.toString();
    }
}
