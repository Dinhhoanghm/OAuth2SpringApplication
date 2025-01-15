/*
 * This file is generated by jOOQ.
 */
package vn.aivhub.data;


import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;

import vn.aivhub.data.tables.BillingHistory;
import vn.aivhub.data.tables.ChargePlan;
import vn.aivhub.data.tables.OauthToken;
import vn.aivhub.data.tables.PlanUser;
import vn.aivhub.data.tables.ResetPasswordToken;
import vn.aivhub.data.tables.User;
import vn.aivhub.data.tables.records.BillingHistoryRecord;
import vn.aivhub.data.tables.records.ChargePlanRecord;
import vn.aivhub.data.tables.records.OauthTokenRecord;
import vn.aivhub.data.tables.records.PlanUserRecord;
import vn.aivhub.data.tables.records.ResetPasswordTokenRecord;
import vn.aivhub.data.tables.records.UserRecord;


/**
 * A class modelling foreign key relationships and constraints of tables in 
 * public.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<BillingHistoryRecord> BILLING_HISTORY_PK = Internal.createUniqueKey(BillingHistory.BILLING_HISTORY, DSL.name("billing_history_pk"), new TableField[] { BillingHistory.ID }, true);
    public static final UniqueKey<ChargePlanRecord> CHARGE_PLAN_PK = Internal.createUniqueKey(ChargePlan.CHARGE_PLAN, DSL.name("charge_plan_pk"), new TableField[] { ChargePlan.ID }, true);
    public static final UniqueKey<OauthTokenRecord> OAUTH_TOKEN_PK = Internal.createUniqueKey(OauthToken.OAUTH_TOKEN, DSL.name("oauth_token_pk"), new TableField[] { OauthToken.ID }, true);
    public static final UniqueKey<PlanUserRecord> PLAN_USER_PK = Internal.createUniqueKey(PlanUser.PLAN_USER, DSL.name("plan_user_pk"), new TableField[] { PlanUser.ID }, true);
    public static final UniqueKey<ResetPasswordTokenRecord> RESET_PASSWORD_TOKEN_PK = Internal.createUniqueKey(ResetPasswordToken.RESET_PASSWORD_TOKEN, DSL.name("reset_password_token_pk"), new TableField[] { ResetPasswordToken.ID }, true);
    public static final UniqueKey<UserRecord> USER_PK = Internal.createUniqueKey(User.USER, DSL.name("user_pk"), new TableField[] { User.ID }, true);
}
