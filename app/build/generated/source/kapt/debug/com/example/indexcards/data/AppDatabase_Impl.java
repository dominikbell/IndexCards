package com.example.indexcards.data;

import androidx.annotation.NonNull;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenDelegate;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.SQLite;
import androidx.sqlite.SQLiteConnection;
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

@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile AppDao _appDao;

  @Override
  @NonNull
  protected RoomOpenDelegate createOpenDelegate() {
    final RoomOpenDelegate _openDelegate = new RoomOpenDelegate(13, "394b664a3d7eeecac73858766ff54387", "3b076603d6bb2e0f9273d98c8c8fbb11") {
      @Override
      public void createAllTables(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `Box` (`boxId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `topic` TEXT NOT NULL, `reminders` INTEGER NOT NULL DEFAULT 0, `categories` INTEGER NOT NULL DEFAULT 0, `description` TEXT NOT NULL, `dateAdded` INTEGER NOT NULL, `showNumberOfCards` INTEGER NOT NULL DEFAULT 1, `lastTrained1` INTEGER NOT NULL DEFAULT -1, `lastTrained2` INTEGER NOT NULL DEFAULT -1, `lastTrained3` INTEGER NOT NULL DEFAULT -1, `lastTrained4` INTEGER NOT NULL DEFAULT -1, `lastTrained5` INTEGER NOT NULL DEFAULT -1)");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `Card` (`cardId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `word` TEXT NOT NULL, `meaning` TEXT NOT NULL, `notes` TEXT NOT NULL, `dateAdded` INTEGER NOT NULL, `level` INTEGER NOT NULL, `boxId` INTEGER NOT NULL, `memoURI` TEXT NOT NULL DEFAULT '', `categoryId` INTEGER NOT NULL DEFAULT -1)");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `Tag` (`tagId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `boxId` INTEGER NOT NULL, `text` TEXT NOT NULL, `color` TEXT NOT NULL)");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `Category` (`categoryId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `boxId` INTEGER NOT NULL, `name` TEXT NOT NULL)");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `TagCardCrossRef` (`tagId` INTEGER NOT NULL, `cardId` INTEGER NOT NULL, PRIMARY KEY(`tagId`, `cardId`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        SQLite.execSQL(connection, "INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '394b664a3d7eeecac73858766ff54387')");
      }

      @Override
      public void dropAllTables(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `Box`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `Card`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `Tag`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `Category`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `TagCardCrossRef`");
      }

      @Override
      public void onCreate(@NonNull final SQLiteConnection connection) {
      }

      @Override
      public void onOpen(@NonNull final SQLiteConnection connection) {
        internalInitInvalidationTracker(connection);
      }

      @Override
      public void onPreMigrate(@NonNull final SQLiteConnection connection) {
        DBUtil.dropFtsSyncTriggers(connection);
      }

      @Override
      public void onPostMigrate(@NonNull final SQLiteConnection connection) {
      }

      @Override
      @NonNull
      public RoomOpenDelegate.ValidationResult onValidateSchema(
          @NonNull final SQLiteConnection connection) {
        final Map<String, TableInfo.Column> _columnsBox = new HashMap<String, TableInfo.Column>(13);
        _columnsBox.put("boxId", new TableInfo.Column("boxId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBox.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBox.put("topic", new TableInfo.Column("topic", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBox.put("reminders", new TableInfo.Column("reminders", "INTEGER", true, 0, "0", TableInfo.CREATED_FROM_ENTITY));
        _columnsBox.put("categories", new TableInfo.Column("categories", "INTEGER", true, 0, "0", TableInfo.CREATED_FROM_ENTITY));
        _columnsBox.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBox.put("dateAdded", new TableInfo.Column("dateAdded", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBox.put("showNumberOfCards", new TableInfo.Column("showNumberOfCards", "INTEGER", true, 0, "1", TableInfo.CREATED_FROM_ENTITY));
        _columnsBox.put("lastTrained1", new TableInfo.Column("lastTrained1", "INTEGER", true, 0, "-1", TableInfo.CREATED_FROM_ENTITY));
        _columnsBox.put("lastTrained2", new TableInfo.Column("lastTrained2", "INTEGER", true, 0, "-1", TableInfo.CREATED_FROM_ENTITY));
        _columnsBox.put("lastTrained3", new TableInfo.Column("lastTrained3", "INTEGER", true, 0, "-1", TableInfo.CREATED_FROM_ENTITY));
        _columnsBox.put("lastTrained4", new TableInfo.Column("lastTrained4", "INTEGER", true, 0, "-1", TableInfo.CREATED_FROM_ENTITY));
        _columnsBox.put("lastTrained5", new TableInfo.Column("lastTrained5", "INTEGER", true, 0, "-1", TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysBox = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesBox = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBox = new TableInfo("Box", _columnsBox, _foreignKeysBox, _indicesBox);
        final TableInfo _existingBox = TableInfo.read(connection, "Box");
        if (!_infoBox.equals(_existingBox)) {
          return new RoomOpenDelegate.ValidationResult(false, "Box(com.example.indexcards.data.Box).\n"
                  + " Expected:\n" + _infoBox + "\n"
                  + " Found:\n" + _existingBox);
        }
        final Map<String, TableInfo.Column> _columnsCard = new HashMap<String, TableInfo.Column>(9);
        _columnsCard.put("cardId", new TableInfo.Column("cardId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCard.put("word", new TableInfo.Column("word", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCard.put("meaning", new TableInfo.Column("meaning", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCard.put("notes", new TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCard.put("dateAdded", new TableInfo.Column("dateAdded", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCard.put("level", new TableInfo.Column("level", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCard.put("boxId", new TableInfo.Column("boxId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCard.put("memoURI", new TableInfo.Column("memoURI", "TEXT", true, 0, "''", TableInfo.CREATED_FROM_ENTITY));
        _columnsCard.put("categoryId", new TableInfo.Column("categoryId", "INTEGER", true, 0, "-1", TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysCard = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesCard = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCard = new TableInfo("Card", _columnsCard, _foreignKeysCard, _indicesCard);
        final TableInfo _existingCard = TableInfo.read(connection, "Card");
        if (!_infoCard.equals(_existingCard)) {
          return new RoomOpenDelegate.ValidationResult(false, "Card(com.example.indexcards.data.Card).\n"
                  + " Expected:\n" + _infoCard + "\n"
                  + " Found:\n" + _existingCard);
        }
        final Map<String, TableInfo.Column> _columnsTag = new HashMap<String, TableInfo.Column>(4);
        _columnsTag.put("tagId", new TableInfo.Column("tagId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTag.put("boxId", new TableInfo.Column("boxId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTag.put("text", new TableInfo.Column("text", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTag.put("color", new TableInfo.Column("color", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysTag = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesTag = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTag = new TableInfo("Tag", _columnsTag, _foreignKeysTag, _indicesTag);
        final TableInfo _existingTag = TableInfo.read(connection, "Tag");
        if (!_infoTag.equals(_existingTag)) {
          return new RoomOpenDelegate.ValidationResult(false, "Tag(com.example.indexcards.data.Tag).\n"
                  + " Expected:\n" + _infoTag + "\n"
                  + " Found:\n" + _existingTag);
        }
        final Map<String, TableInfo.Column> _columnsCategory = new HashMap<String, TableInfo.Column>(3);
        _columnsCategory.put("categoryId", new TableInfo.Column("categoryId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategory.put("boxId", new TableInfo.Column("boxId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategory.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysCategory = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesCategory = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCategory = new TableInfo("Category", _columnsCategory, _foreignKeysCategory, _indicesCategory);
        final TableInfo _existingCategory = TableInfo.read(connection, "Category");
        if (!_infoCategory.equals(_existingCategory)) {
          return new RoomOpenDelegate.ValidationResult(false, "Category(com.example.indexcards.data.Category).\n"
                  + " Expected:\n" + _infoCategory + "\n"
                  + " Found:\n" + _existingCategory);
        }
        final Map<String, TableInfo.Column> _columnsTagCardCrossRef = new HashMap<String, TableInfo.Column>(2);
        _columnsTagCardCrossRef.put("tagId", new TableInfo.Column("tagId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTagCardCrossRef.put("cardId", new TableInfo.Column("cardId", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysTagCardCrossRef = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesTagCardCrossRef = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTagCardCrossRef = new TableInfo("TagCardCrossRef", _columnsTagCardCrossRef, _foreignKeysTagCardCrossRef, _indicesTagCardCrossRef);
        final TableInfo _existingTagCardCrossRef = TableInfo.read(connection, "TagCardCrossRef");
        if (!_infoTagCardCrossRef.equals(_existingTagCardCrossRef)) {
          return new RoomOpenDelegate.ValidationResult(false, "TagCardCrossRef(com.example.indexcards.data.TagCardCrossRef).\n"
                  + " Expected:\n" + _infoTagCardCrossRef + "\n"
                  + " Found:\n" + _existingTagCardCrossRef);
        }
        return new RoomOpenDelegate.ValidationResult(true, null);
      }
    };
    return _openDelegate;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final Map<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final Map<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "Box", "Card", "Tag", "Category", "TagCardCrossRef");
  }

  @Override
  public void clearAllTables() {
    super.performClear(false, "Box", "Card", "Tag", "Category", "TagCardCrossRef");
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final Map<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(AppDao.class, AppDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final Set<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    _autoMigrations.add(new AppDatabase_AutoMigration_8_9_Impl());
    _autoMigrations.add(new AppDatabase_AutoMigration_9_10_Impl());
    _autoMigrations.add(new AppDatabase_AutoMigration_10_11_Impl());
    _autoMigrations.add(new AppDatabase_AutoMigration_11_12_Impl());
    _autoMigrations.add(new AppDatabase_AutoMigration_12_13_Impl());
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
