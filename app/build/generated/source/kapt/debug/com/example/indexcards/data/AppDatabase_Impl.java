package com.example.indexcards.data;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile AppDao _appDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(10) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `Box` (`boxId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `topic` TEXT NOT NULL, `reminders` INTEGER NOT NULL DEFAULT 0, `description` TEXT NOT NULL, `dateAdded` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `Card` (`cardId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `word` TEXT NOT NULL, `meaning` TEXT NOT NULL, `notes` TEXT NOT NULL, `dateAdded` INTEGER NOT NULL, `level` INTEGER NOT NULL, `boxId` INTEGER NOT NULL, `memoURI` TEXT NOT NULL DEFAULT '')");
        db.execSQL("CREATE TABLE IF NOT EXISTS `Tag` (`tagId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `boxId` INTEGER NOT NULL, `text` TEXT NOT NULL, `color` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `TagCardCrossRef` (`tagId` INTEGER NOT NULL, `cardId` INTEGER NOT NULL, PRIMARY KEY(`tagId`, `cardId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '4304d0695f2bb9fbc511767fcde9bc1f')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `Box`");
        db.execSQL("DROP TABLE IF EXISTS `Card`");
        db.execSQL("DROP TABLE IF EXISTS `Tag`");
        db.execSQL("DROP TABLE IF EXISTS `TagCardCrossRef`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsBox = new HashMap<String, TableInfo.Column>(6);
        _columnsBox.put("boxId", new TableInfo.Column("boxId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBox.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBox.put("topic", new TableInfo.Column("topic", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBox.put("reminders", new TableInfo.Column("reminders", "INTEGER", true, 0, "0", TableInfo.CREATED_FROM_ENTITY));
        _columnsBox.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBox.put("dateAdded", new TableInfo.Column("dateAdded", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBox = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBox = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBox = new TableInfo("Box", _columnsBox, _foreignKeysBox, _indicesBox);
        final TableInfo _existingBox = TableInfo.read(db, "Box");
        if (!_infoBox.equals(_existingBox)) {
          return new RoomOpenHelper.ValidationResult(false, "Box(com.example.indexcards.data.Box).\n"
                  + " Expected:\n" + _infoBox + "\n"
                  + " Found:\n" + _existingBox);
        }
        final HashMap<String, TableInfo.Column> _columnsCard = new HashMap<String, TableInfo.Column>(8);
        _columnsCard.put("cardId", new TableInfo.Column("cardId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCard.put("word", new TableInfo.Column("word", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCard.put("meaning", new TableInfo.Column("meaning", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCard.put("notes", new TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCard.put("dateAdded", new TableInfo.Column("dateAdded", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCard.put("level", new TableInfo.Column("level", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCard.put("boxId", new TableInfo.Column("boxId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCard.put("memoURI", new TableInfo.Column("memoURI", "TEXT", true, 0, "''", TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCard = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCard = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCard = new TableInfo("Card", _columnsCard, _foreignKeysCard, _indicesCard);
        final TableInfo _existingCard = TableInfo.read(db, "Card");
        if (!_infoCard.equals(_existingCard)) {
          return new RoomOpenHelper.ValidationResult(false, "Card(com.example.indexcards.data.Card).\n"
                  + " Expected:\n" + _infoCard + "\n"
                  + " Found:\n" + _existingCard);
        }
        final HashMap<String, TableInfo.Column> _columnsTag = new HashMap<String, TableInfo.Column>(4);
        _columnsTag.put("tagId", new TableInfo.Column("tagId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTag.put("boxId", new TableInfo.Column("boxId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTag.put("text", new TableInfo.Column("text", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTag.put("color", new TableInfo.Column("color", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTag = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTag = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTag = new TableInfo("Tag", _columnsTag, _foreignKeysTag, _indicesTag);
        final TableInfo _existingTag = TableInfo.read(db, "Tag");
        if (!_infoTag.equals(_existingTag)) {
          return new RoomOpenHelper.ValidationResult(false, "Tag(com.example.indexcards.data.Tag).\n"
                  + " Expected:\n" + _infoTag + "\n"
                  + " Found:\n" + _existingTag);
        }
        final HashMap<String, TableInfo.Column> _columnsTagCardCrossRef = new HashMap<String, TableInfo.Column>(2);
        _columnsTagCardCrossRef.put("tagId", new TableInfo.Column("tagId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTagCardCrossRef.put("cardId", new TableInfo.Column("cardId", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTagCardCrossRef = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTagCardCrossRef = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTagCardCrossRef = new TableInfo("TagCardCrossRef", _columnsTagCardCrossRef, _foreignKeysTagCardCrossRef, _indicesTagCardCrossRef);
        final TableInfo _existingTagCardCrossRef = TableInfo.read(db, "TagCardCrossRef");
        if (!_infoTagCardCrossRef.equals(_existingTagCardCrossRef)) {
          return new RoomOpenHelper.ValidationResult(false, "TagCardCrossRef(com.example.indexcards.data.TagCardCrossRef).\n"
                  + " Expected:\n" + _infoTagCardCrossRef + "\n"
                  + " Found:\n" + _existingTagCardCrossRef);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "4304d0695f2bb9fbc511767fcde9bc1f", "c1049c07523cff280ada7d7159227964");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "Box","Card","Tag","TagCardCrossRef");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `Box`");
      _db.execSQL("DELETE FROM `Card`");
      _db.execSQL("DELETE FROM `Tag`");
      _db.execSQL("DELETE FROM `TagCardCrossRef`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(AppDao.class, AppDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    _autoMigrations.add(new AppDatabase_AutoMigration_8_9_Impl());
    _autoMigrations.add(new AppDatabase_AutoMigration_9_10_Impl());
    return _autoMigrations;
  }

  @Override
  public AppDao appDao() {
    if (_appDao != null) {
      return _appDao;
    } else {
      synchronized(this) {
        if(_appDao == null) {
          _appDao = new AppDao_Impl(this);
        }
        return _appDao;
      }
    }
  }
}
