package fridge;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import fridge.InFridge;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table IN_FRIDGE.
*/
public class InFridgeDao extends AbstractDao<InFridge, Void> {

    public static final String TABLENAME = "IN_FRIDGE";

    /**
     * Properties of entity InFridge.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property InternalId = new Property(0, long.class, "internalId", false, "INTERNAL_ID");
        public final static Property Inserted = new Property(1, Long.class, "inserted", false, "INSERTED");
        public final static Property Expire = new Property(2, Long.class, "expire", false, "EXPIRE");
        public final static Property Anzahl = new Property(3, Integer.class, "anzahl", false, "ANZAHL");
    };


    public InFridgeDao(DaoConfig config) {
        super(config);
    }
    
    public InFridgeDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'IN_FRIDGE' (" + //
                "'INTERNAL_ID' INTEGER NOT NULL ," + // 0: internalId
                "'INSERTED' INTEGER," + // 1: inserted
                "'EXPIRE' INTEGER," + // 2: expire
                "'ANZAHL' INTEGER);"); // 3: anzahl
        // Add Indexes
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "IDX_IN_FRIDGE_EXPIRE_INTERNAL_ID ON IN_FRIDGE" +
                " (EXPIRE,INTERNAL_ID);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'IN_FRIDGE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, InFridge entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getInternalId());
 
        Long inserted = entity.getInserted();
        if (inserted != null) {
            stmt.bindLong(2, inserted);
        }
 
        Long expire = entity.getExpire();
        if (expire != null) {
            stmt.bindLong(3, expire);
        }
 
        Integer anzahl = entity.getAnzahl();
        if (anzahl != null) {
            stmt.bindLong(4, anzahl);
        }
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public InFridge readEntity(Cursor cursor, int offset) {
        InFridge entity = new InFridge( //
            cursor.getLong(offset + 0), // internalId
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // inserted
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // expire
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3) // anzahl
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, InFridge entity, int offset) {
        entity.setInternalId(cursor.getLong(offset + 0));
        entity.setInserted(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setExpire(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setAnzahl(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(InFridge entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(InFridge entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
