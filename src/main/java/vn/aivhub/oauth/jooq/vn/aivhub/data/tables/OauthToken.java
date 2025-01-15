/*
 * This file is generated by jOOQ.
 */
package vn.aivhub.data.tables;


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row6;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import vn.aivhub.data.Keys;
import vn.aivhub.data.Public;
import vn.aivhub.data.tables.records.OauthTokenRecord;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class OauthToken extends TableImpl<OauthTokenRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.oauth_token</code>
     */
    public static final OauthToken OAUTH_TOKEN = new OauthToken();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<OauthTokenRecord> getRecordType() {
        return OauthTokenRecord.class;
    }

    /**
     * The column <code>public.oauth_token.id</code>.
     */
    public static final TableField<OauthTokenRecord, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false).identity(true), OAUTH_TOKEN, "");

    /**
     * The column <code>public.oauth_token.refresh_token</code>.
     */
    public static final TableField<OauthTokenRecord, String> REFRESH_TOKEN = createField(DSL.name("refresh_token"), SQLDataType.VARCHAR(500), OAUTH_TOKEN, "");

    /**
     * The column <code>public.oauth_token.access_token</code>.
     */
    public static final TableField<OauthTokenRecord, String> ACCESS_TOKEN = createField(DSL.name("access_token"), SQLDataType.VARCHAR(500), OAUTH_TOKEN, "");

    /**
     * The column <code>public.oauth_token.user_id</code>.
     */
    public static final TableField<OauthTokenRecord, Integer> USER_ID = createField(DSL.name("user_id"), SQLDataType.INTEGER, OAUTH_TOKEN, "");

    /**
     * The column <code>public.oauth_token.access_expiration_time</code>.
     */
    public static final TableField<OauthTokenRecord, LocalDateTime> ACCESS_EXPIRATION_TIME = createField(DSL.name("access_expiration_time"), SQLDataType.LOCALDATETIME(6), OAUTH_TOKEN, "");

    /**
     * The column <code>public.oauth_token.refresh_expiration_time</code>.
     */
    public static final TableField<OauthTokenRecord, LocalDateTime> REFRESH_EXPIRATION_TIME = createField(DSL.name("refresh_expiration_time"), SQLDataType.LOCALDATETIME(6), OAUTH_TOKEN, "");

    private OauthToken(Name alias, Table<OauthTokenRecord> aliased) {
        this(alias, aliased, null);
    }

    private OauthToken(Name alias, Table<OauthTokenRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * No further instances allowed
     */
    private OauthToken() {
        this(DSL.name("oauth_token"), null);
    }

    public <O extends Record> OauthToken(Table<O> child, ForeignKey<O, OauthTokenRecord> key) {
        super(child, key, OAUTH_TOKEN);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public Identity<OauthTokenRecord, Integer> getIdentity() {
        return (Identity<OauthTokenRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<OauthTokenRecord> getPrimaryKey() {
        return Keys.OAUTH_TOKEN_PK;
    }

    @Override
    public List<UniqueKey<OauthTokenRecord>> getKeys() {
        return Arrays.<UniqueKey<OauthTokenRecord>>asList(Keys.OAUTH_TOKEN_PK);
    }

    // -------------------------------------------------------------------------
    // Row6 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row6<Integer, String, String, Integer, LocalDateTime, LocalDateTime> fieldsRow() {
        return (Row6) super.fieldsRow();
    }
}
