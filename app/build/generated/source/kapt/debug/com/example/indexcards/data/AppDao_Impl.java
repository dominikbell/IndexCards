package com.example.indexcards.data;

import androidx.annotation.NonNull;
import androidx.collection.LongSparseArray;
import androidx.room.EntityDeleteOrUpdateAdapter;
import androidx.room.EntityInsertAdapter;
import androidx.room.EntityUpsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.coroutines.FlowUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.SQLiteConnection;
import androidx.sqlite.SQLiteStatement;
import java.lang.Class;
import java.lang.Integer;
import java.lang.Long;
import java.lang.NullPointerException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class AppDao_Impl implements AppDao {
  private final RoomDatabase __db;

  private final EntityDeleteOrUpdateAdapter<TagCardCrossRef> __deleteAdapterOfTagCardCrossRef;

  private final EntityUpsertAdapter<Box> __upsertAdapterOfBox;

  private final EntityUpsertAdapter<Category> __upsertAdapterOfCategory;

  private final EntityUpsertAdapter<Card> __upsertAdapterOfCard;

  private final EntityUpsertAdapter<Tag> __upsertAdapterOfTag;

  private final EntityUpsertAdapter<TagCardCrossRef> __upsertAdapterOfTagCardCrossRef;

  public AppDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__deleteAdapterOfTagCardCrossRef = new EntityDeleteOrUpdateAdapter<TagCardCrossRef>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `TagCardCrossRef` WHERE `tagId` = ? AND `cardId` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          @NonNull final TagCardCrossRef entity) {
        statement.bindLong(1, entity.getTagId());
        statement.bindLong(2, entity.getCardId());
      }
    };
    this.__upsertAdapterOfBox = new EntityUpsertAdapter<Box>(new EntityInsertAdapter<Box>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT INTO `Box` (`boxId`,`name`,`topic`,`reminders`,`categories`,`description`,`dateAdded`,`showNumberOfCards`,`lastTrained1`,`lastTrained2`,`lastTrained3`,`lastTrained4`,`lastTrained5`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, @NonNull final Box entity) {
        statement.bindLong(1, entity.getBoxId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getName());
        }
        if (entity.getTopic() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getTopic());
        }
        final int _tmp = entity.getReminders() ? 1 : 0;
        statement.bindLong(4, _tmp);
        final int _tmp_1 = entity.getCategories() ? 1 : 0;
        statement.bindLong(5, _tmp_1);
        if (entity.getDescription() == null) {
          statement.bindNull(6);
        } else {
          statement.bindText(6, entity.getDescription());
        }
        statement.bindLong(7, entity.getDateAdded());
        final int _tmp_2 = entity.getShowNumberOfCards() ? 1 : 0;
        statement.bindLong(8, _tmp_2);
        statement.bindLong(9, entity.getLastTrained1());
        statement.bindLong(10, entity.getLastTrained2());
        statement.bindLong(11, entity.getLastTrained3());
        statement.bindLong(12, entity.getLastTrained4());
        statement.bindLong(13, entity.getLastTrained5());
      }
    }, new EntityDeleteOrUpdateAdapter<Box>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE `Box` SET `boxId` = ?,`name` = ?,`topic` = ?,`reminders` = ?,`categories` = ?,`description` = ?,`dateAdded` = ?,`showNumberOfCards` = ?,`lastTrained1` = ?,`lastTrained2` = ?,`lastTrained3` = ?,`lastTrained4` = ?,`lastTrained5` = ? WHERE `boxId` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, @NonNull final Box entity) {
        statement.bindLong(1, entity.getBoxId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getName());
        }
        if (entity.getTopic() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getTopic());
        }
        final int _tmp = entity.getReminders() ? 1 : 0;
        statement.bindLong(4, _tmp);
        final int _tmp_1 = entity.getCategories() ? 1 : 0;
        statement.bindLong(5, _tmp_1);
        if (entity.getDescription() == null) {
          statement.bindNull(6);
        } else {
          statement.bindText(6, entity.getDescription());
        }
        statement.bindLong(7, entity.getDateAdded());
        final int _tmp_2 = entity.getShowNumberOfCards() ? 1 : 0;
        statement.bindLong(8, _tmp_2);
        statement.bindLong(9, entity.getLastTrained1());
        statement.bindLong(10, entity.getLastTrained2());
        statement.bindLong(11, entity.getLastTrained3());
        statement.bindLong(12, entity.getLastTrained4());
        statement.bindLong(13, entity.getLastTrained5());
        statement.bindLong(14, entity.getBoxId());
      }
    });
    this.__upsertAdapterOfCategory = new EntityUpsertAdapter<Category>(new EntityInsertAdapter<Category>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT INTO `Category` (`categoryId`,`boxId`,`name`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          @NonNull final Category entity) {
        statement.bindLong(1, entity.getCategoryId());
        statement.bindLong(2, entity.getBoxId());
        if (entity.getName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getName());
        }
      }
    }, new EntityDeleteOrUpdateAdapter<Category>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE `Category` SET `categoryId` = ?,`boxId` = ?,`name` = ? WHERE `categoryId` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          @NonNull final Category entity) {
        statement.bindLong(1, entity.getCategoryId());
        statement.bindLong(2, entity.getBoxId());
        if (entity.getName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getName());
        }
        statement.bindLong(4, entity.getCategoryId());
      }
    });
    this.__upsertAdapterOfCard = new EntityUpsertAdapter<Card>(new EntityInsertAdapter<Card>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT INTO `Card` (`cardId`,`word`,`meaning`,`notes`,`dateAdded`,`level`,`boxId`,`memoURI`,`categoryId`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, @NonNull final Card entity) {
        statement.bindLong(1, entity.getCardId());
        if (entity.getWord() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getWord());
        }
        if (entity.getMeaning() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getMeaning());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getNotes());
        }
        statement.bindLong(5, entity.getDateAdded());
        statement.bindLong(6, entity.getLevel());
        statement.bindLong(7, entity.getBoxId());
        if (entity.getMemoURI() == null) {
          statement.bindNull(8);
        } else {
          statement.bindText(8, entity.getMemoURI());
        }
        statement.bindLong(9, entity.getCategoryId());
      }
    }, new EntityDeleteOrUpdateAdapter<Card>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE `Card` SET `cardId` = ?,`word` = ?,`meaning` = ?,`notes` = ?,`dateAdded` = ?,`level` = ?,`boxId` = ?,`memoURI` = ?,`categoryId` = ? WHERE `cardId` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, @NonNull final Card entity) {
        statement.bindLong(1, entity.getCardId());
        if (entity.getWord() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getWord());
        }
        if (entity.getMeaning() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getMeaning());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getNotes());
        }
        statement.bindLong(5, entity.getDateAdded());
        statement.bindLong(6, entity.getLevel());
        statement.bindLong(7, entity.getBoxId());
        if (entity.getMemoURI() == null) {
          statement.bindNull(8);
        } else {
          statement.bindText(8, entity.getMemoURI());
        }
        statement.bindLong(9, entity.getCategoryId());
        statement.bindLong(10, entity.getCardId());
      }
    });
    this.__upsertAdapterOfTag = new EntityUpsertAdapter<Tag>(new EntityInsertAdapter<Tag>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT INTO `Tag` (`tagId`,`boxId`,`text`,`color`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, @NonNull final Tag entity) {
        statement.bindLong(1, entity.getTagId());
        statement.bindLong(2, entity.getBoxId());
        if (entity.getText() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getText());
        }
        if (entity.getColor() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getColor());
        }
      }
    }, new EntityDeleteOrUpdateAdapter<Tag>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE `Tag` SET `tagId` = ?,`boxId` = ?,`text` = ?,`color` = ? WHERE `tagId` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, @NonNull final Tag entity) {
        statement.bindLong(1, entity.getTagId());
        statement.bindLong(2, entity.getBoxId());
        if (entity.getText() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getText());
        }
        if (entity.getColor() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getColor());
        }
        statement.bindLong(5, entity.getTagId());
      }
    });
    this.__upsertAdapterOfTagCardCrossRef = new EntityUpsertAdapter<TagCardCrossRef>(new EntityInsertAdapter<TagCardCrossRef>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT INTO `TagCardCrossRef` (`tagId`,`cardId`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          @NonNull final TagCardCrossRef entity) {
        statement.bindLong(1, entity.getTagId());
        statement.bindLong(2, entity.getCardId());
      }
    }, new EntityDeleteOrUpdateAdapter<TagCardCrossRef>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE `TagCardCrossRef` SET `tagId` = ?,`cardId` = ? WHERE `tagId` = ? AND `cardId` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          @NonNull final TagCardCrossRef entity) {
        statement.bindLong(1, entity.getTagId());
        statement.bindLong(2, entity.getCardId());
        statement.bindLong(3, entity.getTagId());
        statement.bindLong(4, entity.getCardId());
      }
    });
  }

  @Override
  public Object deleteTagCardCrossRef(final TagCardCrossRef crossRef,
      final Continuation<? super Unit> $completion) {
    if (crossRef == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      __deleteAdapterOfTagCardCrossRef.handle(_connection, crossRef);
      return Unit.INSTANCE;
    }, $completion);
  }

  @Override
  public Object upsertBox(final Box box, final Continuation<? super Unit> $completion) {
    if (box == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      __upsertAdapterOfBox.upsert(_connection, box);
      return Unit.INSTANCE;
    }, $completion);
  }

  @Override
  public Object upsertCategory(final Category category,
      final Continuation<? super Unit> $completion) {
    if (category == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      __upsertAdapterOfCategory.upsert(_connection, category);
      return Unit.INSTANCE;
    }, $completion);
  }

  @Override
  public Object upsertCard(final Card card, final Continuation<? super Unit> $completion) {
    if (card == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      __upsertAdapterOfCard.upsert(_connection, card);
      return Unit.INSTANCE;
    }, $completion);
  }

  @Override
  public Object upsertTag(final Tag tag, final Continuation<? super Unit> $completion) {
    if (tag == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      __upsertAdapterOfTag.upsert(_connection, tag);
      return Unit.INSTANCE;
    }, $completion);
  }

  @Override
  public Object upsertTagCardCrossRef(final TagCardCrossRef crossRef,
      final Continuation<? super Unit> $completion) {
    if (crossRef == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      __upsertAdapterOfTagCardCrossRef.upsert(_connection, crossRef);
      return Unit.INSTANCE;
    }, $completion);
  }

  @Override
  public Flow<List<Box>> getAllBoxes() {
    final String _sql = "SELECT * FROM box ORDER BY dateAdded DESC";
    return FlowUtil.createFlow(__db, false, new String[] {"box"}, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfBoxId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "boxId");
        final int _columnIndexOfName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "name");
        final int _columnIndexOfTopic = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "topic");
        final int _columnIndexOfReminders = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "reminders");
        final int _columnIndexOfCategories = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "categories");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final int _columnIndexOfDateAdded = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "dateAdded");
        final int _columnIndexOfShowNumberOfCards = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "showNumberOfCards");
        final int _columnIndexOfLastTrained1 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastTrained1");
        final int _columnIndexOfLastTrained2 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastTrained2");
        final int _columnIndexOfLastTrained3 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastTrained3");
        final int _columnIndexOfLastTrained4 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastTrained4");
        final int _columnIndexOfLastTrained5 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastTrained5");
        final List<Box> _result = new ArrayList<Box>();
        while (_stmt.step()) {
          final Box _item;
          final long _tmpBoxId;
          _tmpBoxId = _stmt.getLong(_columnIndexOfBoxId);
          final String _tmpName;
          if (_stmt.isNull(_columnIndexOfName)) {
            _tmpName = null;
          } else {
            _tmpName = _stmt.getText(_columnIndexOfName);
          }
          final String _tmpTopic;
          if (_stmt.isNull(_columnIndexOfTopic)) {
            _tmpTopic = null;
          } else {
            _tmpTopic = _stmt.getText(_columnIndexOfTopic);
          }
          final boolean _tmpReminders;
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfReminders));
          _tmpReminders = _tmp != 0;
          final boolean _tmpCategories;
          final int _tmp_1;
          _tmp_1 = (int) (_stmt.getLong(_columnIndexOfCategories));
          _tmpCategories = _tmp_1 != 0;
          final String _tmpDescription;
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null;
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription);
          }
          final long _tmpDateAdded;
          _tmpDateAdded = _stmt.getLong(_columnIndexOfDateAdded);
          final boolean _tmpShowNumberOfCards;
          final int _tmp_2;
          _tmp_2 = (int) (_stmt.getLong(_columnIndexOfShowNumberOfCards));
          _tmpShowNumberOfCards = _tmp_2 != 0;
          final long _tmpLastTrained1;
          _tmpLastTrained1 = _stmt.getLong(_columnIndexOfLastTrained1);
          final long _tmpLastTrained2;
          _tmpLastTrained2 = _stmt.getLong(_columnIndexOfLastTrained2);
          final long _tmpLastTrained3;
          _tmpLastTrained3 = _stmt.getLong(_columnIndexOfLastTrained3);
          final long _tmpLastTrained4;
          _tmpLastTrained4 = _stmt.getLong(_columnIndexOfLastTrained4);
          final long _tmpLastTrained5;
          _tmpLastTrained5 = _stmt.getLong(_columnIndexOfLastTrained5);
          _item = new Box(_tmpBoxId,_tmpName,_tmpTopic,_tmpReminders,_tmpCategories,_tmpDescription,_tmpDateAdded,_tmpShowNumberOfCards,_tmpLastTrained1,_tmpLastTrained2,_tmpLastTrained3,_tmpLastTrained4,_tmpLastTrained5);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public Flow<Box> getBox(final long boxId) {
    final String _sql = "SELECT * FROM box WHERE boxId = ?";
    return FlowUtil.createFlow(__db, false, new String[] {"box"}, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, boxId);
        final int _columnIndexOfBoxId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "boxId");
        final int _columnIndexOfName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "name");
        final int _columnIndexOfTopic = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "topic");
        final int _columnIndexOfReminders = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "reminders");
        final int _columnIndexOfCategories = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "categories");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final int _columnIndexOfDateAdded = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "dateAdded");
        final int _columnIndexOfShowNumberOfCards = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "showNumberOfCards");
        final int _columnIndexOfLastTrained1 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastTrained1");
        final int _columnIndexOfLastTrained2 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastTrained2");
        final int _columnIndexOfLastTrained3 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastTrained3");
        final int _columnIndexOfLastTrained4 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastTrained4");
        final int _columnIndexOfLastTrained5 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastTrained5");
        final Box _result;
        if (_stmt.step()) {
          final long _tmpBoxId;
          _tmpBoxId = _stmt.getLong(_columnIndexOfBoxId);
          final String _tmpName;
          if (_stmt.isNull(_columnIndexOfName)) {
            _tmpName = null;
          } else {
            _tmpName = _stmt.getText(_columnIndexOfName);
          }
          final String _tmpTopic;
          if (_stmt.isNull(_columnIndexOfTopic)) {
            _tmpTopic = null;
          } else {
            _tmpTopic = _stmt.getText(_columnIndexOfTopic);
          }
          final boolean _tmpReminders;
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfReminders));
          _tmpReminders = _tmp != 0;
          final boolean _tmpCategories;
          final int _tmp_1;
          _tmp_1 = (int) (_stmt.getLong(_columnIndexOfCategories));
          _tmpCategories = _tmp_1 != 0;
          final String _tmpDescription;
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null;
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription);
          }
          final long _tmpDateAdded;
          _tmpDateAdded = _stmt.getLong(_columnIndexOfDateAdded);
          final boolean _tmpShowNumberOfCards;
          final int _tmp_2;
          _tmp_2 = (int) (_stmt.getLong(_columnIndexOfShowNumberOfCards));
          _tmpShowNumberOfCards = _tmp_2 != 0;
          final long _tmpLastTrained1;
          _tmpLastTrained1 = _stmt.getLong(_columnIndexOfLastTrained1);
          final long _tmpLastTrained2;
          _tmpLastTrained2 = _stmt.getLong(_columnIndexOfLastTrained2);
          final long _tmpLastTrained3;
          _tmpLastTrained3 = _stmt.getLong(_columnIndexOfLastTrained3);
          final long _tmpLastTrained4;
          _tmpLastTrained4 = _stmt.getLong(_columnIndexOfLastTrained4);
          final long _tmpLastTrained5;
          _tmpLastTrained5 = _stmt.getLong(_columnIndexOfLastTrained5);
          _result = new Box(_tmpBoxId,_tmpName,_tmpTopic,_tmpReminders,_tmpCategories,_tmpDescription,_tmpDateAdded,_tmpShowNumberOfCards,_tmpLastTrained1,_tmpLastTrained2,_tmpLastTrained3,_tmpLastTrained4,_tmpLastTrained5);
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public Flow<Card> getCard(final long cardId) {
    final String _sql = "SELECT * FROM card WHERE cardId = ?";
    return FlowUtil.createFlow(__db, false, new String[] {"card"}, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, cardId);
        final int _columnIndexOfCardId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cardId");
        final int _columnIndexOfWord = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "word");
        final int _columnIndexOfMeaning = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "meaning");
        final int _columnIndexOfNotes = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "notes");
        final int _columnIndexOfDateAdded = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "dateAdded");
        final int _columnIndexOfLevel = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "level");
        final int _columnIndexOfBoxId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "boxId");
        final int _columnIndexOfMemoURI = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "memoURI");
        final int _columnIndexOfCategoryId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "categoryId");
        final Card _result;
        if (_stmt.step()) {
          final long _tmpCardId;
          _tmpCardId = _stmt.getLong(_columnIndexOfCardId);
          final String _tmpWord;
          if (_stmt.isNull(_columnIndexOfWord)) {
            _tmpWord = null;
          } else {
            _tmpWord = _stmt.getText(_columnIndexOfWord);
          }
          final String _tmpMeaning;
          if (_stmt.isNull(_columnIndexOfMeaning)) {
            _tmpMeaning = null;
          } else {
            _tmpMeaning = _stmt.getText(_columnIndexOfMeaning);
          }
          final String _tmpNotes;
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null;
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes);
          }
          final long _tmpDateAdded;
          _tmpDateAdded = _stmt.getLong(_columnIndexOfDateAdded);
          final int _tmpLevel;
          _tmpLevel = (int) (_stmt.getLong(_columnIndexOfLevel));
          final long _tmpBoxId;
          _tmpBoxId = _stmt.getLong(_columnIndexOfBoxId);
          final String _tmpMemoURI;
          if (_stmt.isNull(_columnIndexOfMemoURI)) {
            _tmpMemoURI = null;
          } else {
            _tmpMemoURI = _stmt.getText(_columnIndexOfMemoURI);
          }
          final long _tmpCategoryId;
          _tmpCategoryId = _stmt.getLong(_columnIndexOfCategoryId);
          _result = new Card(_tmpCardId,_tmpWord,_tmpMeaning,_tmpNotes,_tmpDateAdded,_tmpLevel,_tmpBoxId,_tmpMemoURI,_tmpCategoryId);
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public Flow<Tag> getTag(final long tagId) {
    final String _sql = "SELECT * FROM tag WHERE tagId = ?";
    return FlowUtil.createFlow(__db, false, new String[] {"tag"}, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, tagId);
        final int _columnIndexOfTagId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "tagId");
        final int _columnIndexOfBoxId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "boxId");
        final int _columnIndexOfText = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "text");
        final int _columnIndexOfColor = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "color");
        final Tag _result;
        if (_stmt.step()) {
          final long _tmpTagId;
          _tmpTagId = _stmt.getLong(_columnIndexOfTagId);
          final long _tmpBoxId;
          _tmpBoxId = _stmt.getLong(_columnIndexOfBoxId);
          final String _tmpText;
          if (_stmt.isNull(_columnIndexOfText)) {
            _tmpText = null;
          } else {
            _tmpText = _stmt.getText(_columnIndexOfText);
          }
          final String _tmpColor;
          if (_stmt.isNull(_columnIndexOfColor)) {
            _tmpColor = null;
          } else {
            _tmpColor = _stmt.getText(_columnIndexOfColor);
          }
          _result = new Tag(_tmpTagId,_tmpBoxId,_tmpText,_tmpColor);
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public Flow<BoxWithCards> getBoxWithCards(final long boxId) {
    final String _sql = "SELECT * FROM box WHERE boxId = ?";
    return FlowUtil.createFlow(__db, false, new String[] {"Card", "box"}, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, boxId);
        final int _columnIndexOfBoxId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "boxId");
        final int _columnIndexOfName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "name");
        final int _columnIndexOfTopic = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "topic");
        final int _columnIndexOfReminders = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "reminders");
        final int _columnIndexOfCategories = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "categories");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final int _columnIndexOfDateAdded = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "dateAdded");
        final int _columnIndexOfShowNumberOfCards = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "showNumberOfCards");
        final int _columnIndexOfLastTrained1 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastTrained1");
        final int _columnIndexOfLastTrained2 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastTrained2");
        final int _columnIndexOfLastTrained3 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastTrained3");
        final int _columnIndexOfLastTrained4 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastTrained4");
        final int _columnIndexOfLastTrained5 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastTrained5");
        final LongSparseArray<ArrayList<Card>> _collectionCards = new LongSparseArray<ArrayList<Card>>();
        while (_stmt.step()) {
          final long _tmpKey;
          _tmpKey = _stmt.getLong(_columnIndexOfBoxId);
          if (!_collectionCards.containsKey(_tmpKey)) {
            _collectionCards.put(_tmpKey, new ArrayList<Card>());
          }
        }
        _stmt.reset();
        __fetchRelationshipCardAscomExampleIndexcardsDataCard(_connection, _collectionCards);
        final BoxWithCards _result;
        if (_stmt.step()) {
          final Box _tmpBox;
          final long _tmpBoxId;
          _tmpBoxId = _stmt.getLong(_columnIndexOfBoxId);
          final String _tmpName;
          if (_stmt.isNull(_columnIndexOfName)) {
            _tmpName = null;
          } else {
            _tmpName = _stmt.getText(_columnIndexOfName);
          }
          final String _tmpTopic;
          if (_stmt.isNull(_columnIndexOfTopic)) {
            _tmpTopic = null;
          } else {
            _tmpTopic = _stmt.getText(_columnIndexOfTopic);
          }
          final boolean _tmpReminders;
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfReminders));
          _tmpReminders = _tmp != 0;
          final boolean _tmpCategories;
          final int _tmp_1;
          _tmp_1 = (int) (_stmt.getLong(_columnIndexOfCategories));
          _tmpCategories = _tmp_1 != 0;
          final String _tmpDescription;
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null;
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription);
          }
          final long _tmpDateAdded;
          _tmpDateAdded = _stmt.getLong(_columnIndexOfDateAdded);
          final boolean _tmpShowNumberOfCards;
          final int _tmp_2;
          _tmp_2 = (int) (_stmt.getLong(_columnIndexOfShowNumberOfCards));
          _tmpShowNumberOfCards = _tmp_2 != 0;
          final long _tmpLastTrained1;
          _tmpLastTrained1 = _stmt.getLong(_columnIndexOfLastTrained1);
          final long _tmpLastTrained2;
          _tmpLastTrained2 = _stmt.getLong(_columnIndexOfLastTrained2);
          final long _tmpLastTrained3;
          _tmpLastTrained3 = _stmt.getLong(_columnIndexOfLastTrained3);
          final long _tmpLastTrained4;
          _tmpLastTrained4 = _stmt.getLong(_columnIndexOfLastTrained4);
          final long _tmpLastTrained5;
          _tmpLastTrained5 = _stmt.getLong(_columnIndexOfLastTrained5);
          _tmpBox = new Box(_tmpBoxId,_tmpName,_tmpTopic,_tmpReminders,_tmpCategories,_tmpDescription,_tmpDateAdded,_tmpShowNumberOfCards,_tmpLastTrained1,_tmpLastTrained2,_tmpLastTrained3,_tmpLastTrained4,_tmpLastTrained5);
          final ArrayList<Card> _tmpCardsCollection;
          final long _tmpKey_1;
          _tmpKey_1 = _stmt.getLong(_columnIndexOfBoxId);
          _tmpCardsCollection = _collectionCards.get(_tmpKey_1);
          _result = new BoxWithCards(_tmpBox,_tmpCardsCollection);
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public Flow<BoxWithTags> getBoxWithTags(final long boxId) {
    final String _sql = "SELECT * FROM box WHERE boxId = ?";
    return FlowUtil.createFlow(__db, false, new String[] {"Tag", "box"}, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, boxId);
        final int _columnIndexOfBoxId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "boxId");
        final int _columnIndexOfName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "name");
        final int _columnIndexOfTopic = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "topic");
        final int _columnIndexOfReminders = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "reminders");
        final int _columnIndexOfCategories = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "categories");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final int _columnIndexOfDateAdded = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "dateAdded");
        final int _columnIndexOfShowNumberOfCards = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "showNumberOfCards");
        final int _columnIndexOfLastTrained1 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastTrained1");
        final int _columnIndexOfLastTrained2 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastTrained2");
        final int _columnIndexOfLastTrained3 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastTrained3");
        final int _columnIndexOfLastTrained4 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastTrained4");
        final int _columnIndexOfLastTrained5 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastTrained5");
        final LongSparseArray<ArrayList<Tag>> _collectionTags = new LongSparseArray<ArrayList<Tag>>();
        while (_stmt.step()) {
          final long _tmpKey;
          _tmpKey = _stmt.getLong(_columnIndexOfBoxId);
          if (!_collectionTags.containsKey(_tmpKey)) {
            _collectionTags.put(_tmpKey, new ArrayList<Tag>());
          }
        }
        _stmt.reset();
        __fetchRelationshipTagAscomExampleIndexcardsDataTag(_connection, _collectionTags);
        final BoxWithTags _result;
        if (_stmt.step()) {
          final Box _tmpBox;
          final long _tmpBoxId;
          _tmpBoxId = _stmt.getLong(_columnIndexOfBoxId);
          final String _tmpName;
          if (_stmt.isNull(_columnIndexOfName)) {
            _tmpName = null;
          } else {
            _tmpName = _stmt.getText(_columnIndexOfName);
          }
          final String _tmpTopic;
          if (_stmt.isNull(_columnIndexOfTopic)) {
            _tmpTopic = null;
          } else {
            _tmpTopic = _stmt.getText(_columnIndexOfTopic);
          }
          final boolean _tmpReminders;
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfReminders));
          _tmpReminders = _tmp != 0;
          final boolean _tmpCategories;
          final int _tmp_1;
          _tmp_1 = (int) (_stmt.getLong(_columnIndexOfCategories));
          _tmpCategories = _tmp_1 != 0;
          final String _tmpDescription;
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null;
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription);
          }
          final long _tmpDateAdded;
          _tmpDateAdded = _stmt.getLong(_columnIndexOfDateAdded);
          final boolean _tmpShowNumberOfCards;
          final int _tmp_2;
          _tmp_2 = (int) (_stmt.getLong(_columnIndexOfShowNumberOfCards));
          _tmpShowNumberOfCards = _tmp_2 != 0;
          final long _tmpLastTrained1;
          _tmpLastTrained1 = _stmt.getLong(_columnIndexOfLastTrained1);
          final long _tmpLastTrained2;
          _tmpLastTrained2 = _stmt.getLong(_columnIndexOfLastTrained2);
          final long _tmpLastTrained3;
          _tmpLastTrained3 = _stmt.getLong(_columnIndexOfLastTrained3);
          final long _tmpLastTrained4;
          _tmpLastTrained4 = _stmt.getLong(_columnIndexOfLastTrained4);
          final long _tmpLastTrained5;
          _tmpLastTrained5 = _stmt.getLong(_columnIndexOfLastTrained5);
          _tmpBox = new Box(_tmpBoxId,_tmpName,_tmpTopic,_tmpReminders,_tmpCategories,_tmpDescription,_tmpDateAdded,_tmpShowNumberOfCards,_tmpLastTrained1,_tmpLastTrained2,_tmpLastTrained3,_tmpLastTrained4,_tmpLastTrained5);
          final ArrayList<Tag> _tmpTagsCollection;
          final long _tmpKey_1;
          _tmpKey_1 = _stmt.getLong(_columnIndexOfBoxId);
          _tmpTagsCollection = _collectionTags.get(_tmpKey_1);
          _result = new BoxWithTags(_tmpBox,_tmpTagsCollection);
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public Flow<BoxWithCategories> getBoxWithCategories(final long boxId) {
    final String _sql = "SELECT * FROM box WHERE boxId = ?";
    return FlowUtil.createFlow(__db, false, new String[] {"Category", "box"}, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, boxId);
        final int _columnIndexOfBoxId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "boxId");
        final int _columnIndexOfName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "name");
        final int _columnIndexOfTopic = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "topic");
        final int _columnIndexOfReminders = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "reminders");
        final int _columnIndexOfCategories = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "categories");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final int _columnIndexOfDateAdded = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "dateAdded");
        final int _columnIndexOfShowNumberOfCards = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "showNumberOfCards");
        final int _columnIndexOfLastTrained1 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastTrained1");
        final int _columnIndexOfLastTrained2 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastTrained2");
        final int _columnIndexOfLastTrained3 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastTrained3");
        final int _columnIndexOfLastTrained4 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastTrained4");
        final int _columnIndexOfLastTrained5 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastTrained5");
        final LongSparseArray<ArrayList<Category>> _collectionCategories = new LongSparseArray<ArrayList<Category>>();
        while (_stmt.step()) {
          final long _tmpKey;
          _tmpKey = _stmt.getLong(_columnIndexOfBoxId);
          if (!_collectionCategories.containsKey(_tmpKey)) {
            _collectionCategories.put(_tmpKey, new ArrayList<Category>());
          }
        }
        _stmt.reset();
        __fetchRelationshipCategoryAscomExampleIndexcardsDataCategory(_connection, _collectionCategories);
        final BoxWithCategories _result;
        if (_stmt.step()) {
          final Box _tmpBox;
          final long _tmpBoxId;
          _tmpBoxId = _stmt.getLong(_columnIndexOfBoxId);
          final String _tmpName;
          if (_stmt.isNull(_columnIndexOfName)) {
            _tmpName = null;
          } else {
            _tmpName = _stmt.getText(_columnIndexOfName);
          }
          final String _tmpTopic;
          if (_stmt.isNull(_columnIndexOfTopic)) {
            _tmpTopic = null;
          } else {
            _tmpTopic = _stmt.getText(_columnIndexOfTopic);
          }
          final boolean _tmpReminders;
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfReminders));
          _tmpReminders = _tmp != 0;
          final boolean _tmpCategories;
          final int _tmp_1;
          _tmp_1 = (int) (_stmt.getLong(_columnIndexOfCategories));
          _tmpCategories = _tmp_1 != 0;
          final String _tmpDescription;
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null;
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription);
          }
          final long _tmpDateAdded;
          _tmpDateAdded = _stmt.getLong(_columnIndexOfDateAdded);
          final boolean _tmpShowNumberOfCards;
          final int _tmp_2;
          _tmp_2 = (int) (_stmt.getLong(_columnIndexOfShowNumberOfCards));
          _tmpShowNumberOfCards = _tmp_2 != 0;
          final long _tmpLastTrained1;
          _tmpLastTrained1 = _stmt.getLong(_columnIndexOfLastTrained1);
          final long _tmpLastTrained2;
          _tmpLastTrained2 = _stmt.getLong(_columnIndexOfLastTrained2);
          final long _tmpLastTrained3;
          _tmpLastTrained3 = _stmt.getLong(_columnIndexOfLastTrained3);
          final long _tmpLastTrained4;
          _tmpLastTrained4 = _stmt.getLong(_columnIndexOfLastTrained4);
          final long _tmpLastTrained5;
          _tmpLastTrained5 = _stmt.getLong(_columnIndexOfLastTrained5);
          _tmpBox = new Box(_tmpBoxId,_tmpName,_tmpTopic,_tmpReminders,_tmpCategories,_tmpDescription,_tmpDateAdded,_tmpShowNumberOfCards,_tmpLastTrained1,_tmpLastTrained2,_tmpLastTrained3,_tmpLastTrained4,_tmpLastTrained5);
          final ArrayList<Category> _tmpCategoriesCollection;
          final long _tmpKey_1;
          _tmpKey_1 = _stmt.getLong(_columnIndexOfBoxId);
          _tmpCategoriesCollection = _collectionCategories.get(_tmpKey_1);
          _result = new BoxWithCategories(_tmpBox,_tmpCategoriesCollection);
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public Flow<TagWithCards> getTagWithCards(final long tagId) {
    final String _sql = "SELECT * FROM tag WHERE tagId = ?";
    return FlowUtil.createFlow(__db, true, new String[] {"TagCardCrossRef", "Card",
        "tag"}, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, tagId);
        final int _columnIndexOfTagId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "tagId");
        final int _columnIndexOfBoxId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "boxId");
        final int _columnIndexOfText = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "text");
        final int _columnIndexOfColor = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "color");
        final LongSparseArray<ArrayList<Card>> _collectionCards = new LongSparseArray<ArrayList<Card>>();
        while (_stmt.step()) {
          final long _tmpKey;
          _tmpKey = _stmt.getLong(_columnIndexOfTagId);
          if (!_collectionCards.containsKey(_tmpKey)) {
            _collectionCards.put(_tmpKey, new ArrayList<Card>());
          }
        }
        _stmt.reset();
        __fetchRelationshipCardAscomExampleIndexcardsDataCard_1(_connection, _collectionCards);
        final TagWithCards _result;
        if (_stmt.step()) {
          final Tag _tmpTag;
          final long _tmpTagId;
          _tmpTagId = _stmt.getLong(_columnIndexOfTagId);
          final long _tmpBoxId;
          _tmpBoxId = _stmt.getLong(_columnIndexOfBoxId);
          final String _tmpText;
          if (_stmt.isNull(_columnIndexOfText)) {
            _tmpText = null;
          } else {
            _tmpText = _stmt.getText(_columnIndexOfText);
          }
          final String _tmpColor;
          if (_stmt.isNull(_columnIndexOfColor)) {
            _tmpColor = null;
          } else {
            _tmpColor = _stmt.getText(_columnIndexOfColor);
          }
          _tmpTag = new Tag(_tmpTagId,_tmpBoxId,_tmpText,_tmpColor);
          final ArrayList<Card> _tmpCardsCollection;
          final long _tmpKey_1;
          _tmpKey_1 = _stmt.getLong(_columnIndexOfTagId);
          _tmpCardsCollection = _collectionCards.get(_tmpKey_1);
          _result = new TagWithCards(_tmpTag,_tmpCardsCollection);
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public Flow<CardWithTags> getCardWithTags(final long cardId) {
    final String _sql = "SELECT * FROM card WHERE cardId = ?";
    return FlowUtil.createFlow(__db, true, new String[] {"TagCardCrossRef", "Tag",
        "card"}, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, cardId);
        final int _columnIndexOfCardId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cardId");
        final int _columnIndexOfWord = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "word");
        final int _columnIndexOfMeaning = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "meaning");
        final int _columnIndexOfNotes = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "notes");
        final int _columnIndexOfDateAdded = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "dateAdded");
        final int _columnIndexOfLevel = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "level");
        final int _columnIndexOfBoxId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "boxId");
        final int _columnIndexOfMemoURI = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "memoURI");
        final int _columnIndexOfCategoryId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "categoryId");
        final LongSparseArray<ArrayList<Tag>> _collectionTags = new LongSparseArray<ArrayList<Tag>>();
        while (_stmt.step()) {
          final long _tmpKey;
          _tmpKey = _stmt.getLong(_columnIndexOfCardId);
          if (!_collectionTags.containsKey(_tmpKey)) {
            _collectionTags.put(_tmpKey, new ArrayList<Tag>());
          }
        }
        _stmt.reset();
        __fetchRelationshipTagAscomExampleIndexcardsDataTag_1(_connection, _collectionTags);
        final CardWithTags _result;
        if (_stmt.step()) {
          final Card _tmpCard;
          final long _tmpCardId;
          _tmpCardId = _stmt.getLong(_columnIndexOfCardId);
          final String _tmpWord;
          if (_stmt.isNull(_columnIndexOfWord)) {
            _tmpWord = null;
          } else {
            _tmpWord = _stmt.getText(_columnIndexOfWord);
          }
          final String _tmpMeaning;
          if (_stmt.isNull(_columnIndexOfMeaning)) {
            _tmpMeaning = null;
          } else {
            _tmpMeaning = _stmt.getText(_columnIndexOfMeaning);
          }
          final String _tmpNotes;
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null;
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes);
          }
          final long _tmpDateAdded;
          _tmpDateAdded = _stmt.getLong(_columnIndexOfDateAdded);
          final int _tmpLevel;
          _tmpLevel = (int) (_stmt.getLong(_columnIndexOfLevel));
          final long _tmpBoxId;
          _tmpBoxId = _stmt.getLong(_columnIndexOfBoxId);
          final String _tmpMemoURI;
          if (_stmt.isNull(_columnIndexOfMemoURI)) {
            _tmpMemoURI = null;
          } else {
            _tmpMemoURI = _stmt.getText(_columnIndexOfMemoURI);
          }
          final long _tmpCategoryId;
          _tmpCategoryId = _stmt.getLong(_columnIndexOfCategoryId);
          _tmpCard = new Card(_tmpCardId,_tmpWord,_tmpMeaning,_tmpNotes,_tmpDateAdded,_tmpLevel,_tmpBoxId,_tmpMemoURI,_tmpCategoryId);
          final ArrayList<Tag> _tmpTagsCollection;
          final long _tmpKey_1;
          _tmpKey_1 = _stmt.getLong(_columnIndexOfCardId);
          _tmpTagsCollection = _collectionTags.get(_tmpKey_1);
          _result = new CardWithTags(_tmpCard,_tmpTagsCollection);
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public Flow<List<CardWithTags>> getAllCardsWithTagsOfBox(final long boxId) {
    final String _sql = "SELECT * FROM card WHERE boxId = ?";
    return FlowUtil.createFlow(__db, true, new String[] {"TagCardCrossRef", "Tag",
        "card"}, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, boxId);
        final int _columnIndexOfCardId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cardId");
        final int _columnIndexOfWord = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "word");
        final int _columnIndexOfMeaning = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "meaning");
        final int _columnIndexOfNotes = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "notes");
        final int _columnIndexOfDateAdded = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "dateAdded");
        final int _columnIndexOfLevel = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "level");
        final int _columnIndexOfBoxId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "boxId");
        final int _columnIndexOfMemoURI = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "memoURI");
        final int _columnIndexOfCategoryId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "categoryId");
        final LongSparseArray<ArrayList<Tag>> _collectionTags = new LongSparseArray<ArrayList<Tag>>();
        while (_stmt.step()) {
          final long _tmpKey;
          _tmpKey = _stmt.getLong(_columnIndexOfCardId);
          if (!_collectionTags.containsKey(_tmpKey)) {
            _collectionTags.put(_tmpKey, new ArrayList<Tag>());
          }
        }
        _stmt.reset();
        __fetchRelationshipTagAscomExampleIndexcardsDataTag_1(_connection, _collectionTags);
        final List<CardWithTags> _result = new ArrayList<CardWithTags>();
        while (_stmt.step()) {
          final CardWithTags _item;
          final Card _tmpCard;
          final long _tmpCardId;
          _tmpCardId = _stmt.getLong(_columnIndexOfCardId);
          final String _tmpWord;
          if (_stmt.isNull(_columnIndexOfWord)) {
            _tmpWord = null;
          } else {
            _tmpWord = _stmt.getText(_columnIndexOfWord);
          }
          final String _tmpMeaning;
          if (_stmt.isNull(_columnIndexOfMeaning)) {
            _tmpMeaning = null;
          } else {
            _tmpMeaning = _stmt.getText(_columnIndexOfMeaning);
          }
          final String _tmpNotes;
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null;
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes);
          }
          final long _tmpDateAdded;
          _tmpDateAdded = _stmt.getLong(_columnIndexOfDateAdded);
          final int _tmpLevel;
          _tmpLevel = (int) (_stmt.getLong(_columnIndexOfLevel));
          final long _tmpBoxId;
          _tmpBoxId = _stmt.getLong(_columnIndexOfBoxId);
          final String _tmpMemoURI;
          if (_stmt.isNull(_columnIndexOfMemoURI)) {
            _tmpMemoURI = null;
          } else {
            _tmpMemoURI = _stmt.getText(_columnIndexOfMemoURI);
          }
          final long _tmpCategoryId;
          _tmpCategoryId = _stmt.getLong(_columnIndexOfCategoryId);
          _tmpCard = new Card(_tmpCardId,_tmpWord,_tmpMeaning,_tmpNotes,_tmpDateAdded,_tmpLevel,_tmpBoxId,_tmpMemoURI,_tmpCategoryId);
          final ArrayList<Tag> _tmpTagsCollection;
          final long _tmpKey_1;
          _tmpKey_1 = _stmt.getLong(_columnIndexOfCardId);
          _tmpTagsCollection = _collectionTags.get(_tmpKey_1);
          _item = new CardWithTags(_tmpCard,_tmpTagsCollection);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public Object getBiggestBoxId(final Continuation<? super Long> $completion) {
    final String _sql = "SELECT MAX(boxId) FROM box";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final Long _result;
        if (_stmt.step()) {
          final Long _tmp;
          if (_stmt.isNull(0)) {
            _tmp = null;
          } else {
            _tmp = _stmt.getLong(0);
          }
          _result = _tmp;
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object getBiggestCategoryId(final Continuation<? super Long> $completion) {
    final String _sql = "SELECT MAX(categoryId) FROM category";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final Long _result;
        if (_stmt.step()) {
          final Long _tmp;
          if (_stmt.isNull(0)) {
            _tmp = null;
          } else {
            _tmp = _stmt.getLong(0);
          }
          _result = _tmp;
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object getBiggestCardId(final Continuation<? super Long> $completion) {
    final String _sql = "SELECT MAX(cardId) FROM card";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final Long _result;
        if (_stmt.step()) {
          final Long _tmp;
          if (_stmt.isNull(0)) {
            _tmp = null;
          } else {
            _tmp = _stmt.getLong(0);
          }
          _result = _tmp;
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object getBiggestTagId(final Continuation<? super Long> $completion) {
    final String _sql = "SELECT MAX(tagId) FROM tag";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final Long _result;
        if (_stmt.step()) {
          final Long _tmp;
          if (_stmt.isNull(0)) {
            _tmp = null;
          } else {
            _tmp = _stmt.getLong(0);
          }
          _result = _tmp;
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object getNumberOfCardsOfLevelInBox(final long boxId, final int level,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM card WHERE boxId = ? AND level = ?";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, boxId);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, level);
        final Integer _result;
        if (_stmt.step()) {
          final Integer _tmp;
          if (_stmt.isNull(0)) {
            _tmp = null;
          } else {
            _tmp = (int) (_stmt.getLong(0));
          }
          _result = _tmp;
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object updateTag(final long tagId, final String text, final String color,
      final Continuation<? super Unit> $completion) {
    final String _sql = "UPDATE tag SET text = ?, color = ? WHERE tagId = ?";
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (text == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, text);
        }
        _argIndex = 2;
        if (color == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, color);
        }
        _argIndex = 3;
        _stmt.bindLong(_argIndex, tagId);
        _stmt.step();
        return Unit.INSTANCE;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object deleteBox(final long boxId, final Continuation<? super Unit> $completion) {
    final String _sql = "DELETE FROM box WHERE boxId = ?";
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, boxId);
        _stmt.step();
        return Unit.INSTANCE;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object deleteCategory(final long categoryId,
      final Continuation<? super Unit> $completion) {
    final String _sql = "DELETE FROM category WHERE categoryId = ?";
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, categoryId);
        _stmt.step();
        return Unit.INSTANCE;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object deleteCard(final long cardId, final Continuation<? super Unit> $completion) {
    final String _sql = "DELETE FROM card WHERE cardId = ?";
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, cardId);
        _stmt.step();
        return Unit.INSTANCE;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object deleteTag(final long tagId, final Continuation<? super Unit> $completion) {
    final String _sql = "DELETE FROM tag WHERE tagId = ?";
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, tagId);
        _stmt.step();
        return Unit.INSTANCE;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object deleteCardsFromBox(final long boxId, final Continuation<? super Unit> $completion) {
    final String _sql = "DELETE FROM card WHERE boxId = ?";
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, boxId);
        _stmt.step();
        return Unit.INSTANCE;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object deleteCategoriesFromBox(final long boxId,
      final Continuation<? super Unit> $completion) {
    final String _sql = "DELETE FROM category WHERE boxId = ?";
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, boxId);
        _stmt.step();
        return Unit.INSTANCE;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object deleteTagsFromBox(final long boxId, final Continuation<? super Unit> $completion) {
    final String _sql = "DELETE FROM tag WHERE boxId = ?";
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, boxId);
        _stmt.step();
        return Unit.INSTANCE;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object deleteTagsFromCard(final long tagId, final Continuation<? super Unit> $completion) {
    final String _sql = "DELETE FROM tagcardcrossref WHERE tagId = ?";
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, tagId);
        _stmt.step();
        return Unit.INSTANCE;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object deleteCardFromTags(final long cardId,
      final Continuation<? super Unit> $completion) {
    final String _sql = "DELETE FROM tagcardcrossref WHERE cardId = ?";
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, cardId);
        _stmt.step();
        return Unit.INSTANCE;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object upgradeLevelOnCard(final long cardId,
      final Continuation<? super Unit> $completion) {
    final String _sql = "UPDATE Card SET level = level + 1 WHERE cardId = ?";
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, cardId);
        _stmt.step();
        return Unit.INSTANCE;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object downgradeLevelOnCard(final long cardId,
      final Continuation<? super Unit> $completion) {
    final String _sql = "UPDATE Card SET level = level - 1 WHERE cardId = ?";
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, cardId);
        _stmt.step();
        return Unit.INSTANCE;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object enableNotificationsForBox(final long boxId,
      final Continuation<? super Unit> $completion) {
    final String _sql = "UPDATE Box SET reminders = 1 WHERE boxId = ?";
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, boxId);
        _stmt.step();
        return Unit.INSTANCE;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object disableNotificationsForBox(final long boxId,
      final Continuation<? super Unit> $completion) {
    final String _sql = "UPDATE Box SET reminders = 0 WHERE boxId = ?";
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, boxId);
        _stmt.step();
        return Unit.INSTANCE;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private void __fetchRelationshipCardAscomExampleIndexcardsDataCard(
      @NonNull final SQLiteConnection _connection,
      @NonNull final LongSparseArray<ArrayList<Card>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > 999) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (_tmpMap) -> {
        __fetchRelationshipCardAscomExampleIndexcardsDataCard(_connection, _tmpMap);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = new StringBuilder();
    _stringBuilder.append("SELECT `cardId`,`word`,`meaning`,`notes`,`dateAdded`,`level`,`boxId`,`memoURI`,`categoryId` FROM `Card` WHERE `boxId` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final SQLiteStatement _stmt = _connection.prepare(_sql);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    try {
      final int _itemKeyIndex = SQLiteStatementUtil.getColumnIndex(_stmt, "boxId");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _columnIndexOfCardId = 0;
      final int _columnIndexOfWord = 1;
      final int _columnIndexOfMeaning = 2;
      final int _columnIndexOfNotes = 3;
      final int _columnIndexOfDateAdded = 4;
      final int _columnIndexOfLevel = 5;
      final int _columnIndexOfBoxId = 6;
      final int _columnIndexOfMemoURI = 7;
      final int _columnIndexOfCategoryId = 8;
      while (_stmt.step()) {
        final long _tmpKey;
        _tmpKey = _stmt.getLong(_itemKeyIndex);
        final ArrayList<Card> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final Card _item_1;
          final long _tmpCardId;
          _tmpCardId = _stmt.getLong(_columnIndexOfCardId);
          final String _tmpWord;
          if (_stmt.isNull(_columnIndexOfWord)) {
            _tmpWord = null;
          } else {
            _tmpWord = _stmt.getText(_columnIndexOfWord);
          }
          final String _tmpMeaning;
          if (_stmt.isNull(_columnIndexOfMeaning)) {
            _tmpMeaning = null;
          } else {
            _tmpMeaning = _stmt.getText(_columnIndexOfMeaning);
          }
          final String _tmpNotes;
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null;
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes);
          }
          final long _tmpDateAdded;
          _tmpDateAdded = _stmt.getLong(_columnIndexOfDateAdded);
          final int _tmpLevel;
          _tmpLevel = (int) (_stmt.getLong(_columnIndexOfLevel));
          final long _tmpBoxId;
          _tmpBoxId = _stmt.getLong(_columnIndexOfBoxId);
          final String _tmpMemoURI;
          if (_stmt.isNull(_columnIndexOfMemoURI)) {
            _tmpMemoURI = null;
          } else {
            _tmpMemoURI = _stmt.getText(_columnIndexOfMemoURI);
          }
          final long _tmpCategoryId;
          _tmpCategoryId = _stmt.getLong(_columnIndexOfCategoryId);
          _item_1 = new Card(_tmpCardId,_tmpWord,_tmpMeaning,_tmpNotes,_tmpDateAdded,_tmpLevel,_tmpBoxId,_tmpMemoURI,_tmpCategoryId);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _stmt.close();
    }
  }

  private void __fetchRelationshipTagAscomExampleIndexcardsDataTag(
      @NonNull final SQLiteConnection _connection,
      @NonNull final LongSparseArray<ArrayList<Tag>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > 999) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (_tmpMap) -> {
        __fetchRelationshipTagAscomExampleIndexcardsDataTag(_connection, _tmpMap);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = new StringBuilder();
    _stringBuilder.append("SELECT `tagId`,`boxId`,`text`,`color` FROM `Tag` WHERE `boxId` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final SQLiteStatement _stmt = _connection.prepare(_sql);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    try {
      final int _itemKeyIndex = SQLiteStatementUtil.getColumnIndex(_stmt, "boxId");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _columnIndexOfTagId = 0;
      final int _columnIndexOfBoxId = 1;
      final int _columnIndexOfText = 2;
      final int _columnIndexOfColor = 3;
      while (_stmt.step()) {
        final long _tmpKey;
        _tmpKey = _stmt.getLong(_itemKeyIndex);
        final ArrayList<Tag> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final Tag _item_1;
          final long _tmpTagId;
          _tmpTagId = _stmt.getLong(_columnIndexOfTagId);
          final long _tmpBoxId;
          _tmpBoxId = _stmt.getLong(_columnIndexOfBoxId);
          final String _tmpText;
          if (_stmt.isNull(_columnIndexOfText)) {
            _tmpText = null;
          } else {
            _tmpText = _stmt.getText(_columnIndexOfText);
          }
          final String _tmpColor;
          if (_stmt.isNull(_columnIndexOfColor)) {
            _tmpColor = null;
          } else {
            _tmpColor = _stmt.getText(_columnIndexOfColor);
          }
          _item_1 = new Tag(_tmpTagId,_tmpBoxId,_tmpText,_tmpColor);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _stmt.close();
    }
  }

  private void __fetchRelationshipCategoryAscomExampleIndexcardsDataCategory(
      @NonNull final SQLiteConnection _connection,
      @NonNull final LongSparseArray<ArrayList<Category>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > 999) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (_tmpMap) -> {
        __fetchRelationshipCategoryAscomExampleIndexcardsDataCategory(_connection, _tmpMap);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = new StringBuilder();
    _stringBuilder.append("SELECT `categoryId`,`boxId`,`name` FROM `Category` WHERE `boxId` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final SQLiteStatement _stmt = _connection.prepare(_sql);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    try {
      final int _itemKeyIndex = SQLiteStatementUtil.getColumnIndex(_stmt, "boxId");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _columnIndexOfCategoryId = 0;
      final int _columnIndexOfBoxId = 1;
      final int _columnIndexOfName = 2;
      while (_stmt.step()) {
        final long _tmpKey;
        _tmpKey = _stmt.getLong(_itemKeyIndex);
        final ArrayList<Category> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final Category _item_1;
          final long _tmpCategoryId;
          _tmpCategoryId = _stmt.getLong(_columnIndexOfCategoryId);
          final long _tmpBoxId;
          _tmpBoxId = _stmt.getLong(_columnIndexOfBoxId);
          final String _tmpName;
          if (_stmt.isNull(_columnIndexOfName)) {
            _tmpName = null;
          } else {
            _tmpName = _stmt.getText(_columnIndexOfName);
          }
          _item_1 = new Category(_tmpCategoryId,_tmpBoxId,_tmpName);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _stmt.close();
    }
  }

  private void __fetchRelationshipCardAscomExampleIndexcardsDataCard_1(
      @NonNull final SQLiteConnection _connection,
      @NonNull final LongSparseArray<ArrayList<Card>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > 999) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (_tmpMap) -> {
        __fetchRelationshipCardAscomExampleIndexcardsDataCard_1(_connection, _tmpMap);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = new StringBuilder();
    _stringBuilder.append("SELECT `Card`.`cardId` AS `cardId`,`Card`.`word` AS `word`,`Card`.`meaning` AS `meaning`,`Card`.`notes` AS `notes`,`Card`.`dateAdded` AS `dateAdded`,`Card`.`level` AS `level`,`Card`.`boxId` AS `boxId`,`Card`.`memoURI` AS `memoURI`,`Card`.`categoryId` AS `categoryId`,_junction.`tagId` FROM `TagCardCrossRef` AS _junction INNER JOIN `Card` ON (_junction.`cardId` = `Card`.`cardId`) WHERE _junction.`tagId` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final SQLiteStatement _stmt = _connection.prepare(_sql);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    try {
      // _junction.tagId;
      final int _itemKeyIndex = 9;
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _columnIndexOfCardId = 0;
      final int _columnIndexOfWord = 1;
      final int _columnIndexOfMeaning = 2;
      final int _columnIndexOfNotes = 3;
      final int _columnIndexOfDateAdded = 4;
      final int _columnIndexOfLevel = 5;
      final int _columnIndexOfBoxId = 6;
      final int _columnIndexOfMemoURI = 7;
      final int _columnIndexOfCategoryId = 8;
      while (_stmt.step()) {
        final long _tmpKey;
        _tmpKey = _stmt.getLong(_itemKeyIndex);
        final ArrayList<Card> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final Card _item_1;
          final long _tmpCardId;
          _tmpCardId = _stmt.getLong(_columnIndexOfCardId);
          final String _tmpWord;
          if (_stmt.isNull(_columnIndexOfWord)) {
            _tmpWord = null;
          } else {
            _tmpWord = _stmt.getText(_columnIndexOfWord);
          }
          final String _tmpMeaning;
          if (_stmt.isNull(_columnIndexOfMeaning)) {
            _tmpMeaning = null;
          } else {
            _tmpMeaning = _stmt.getText(_columnIndexOfMeaning);
          }
          final String _tmpNotes;
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null;
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes);
          }
          final long _tmpDateAdded;
          _tmpDateAdded = _stmt.getLong(_columnIndexOfDateAdded);
          final int _tmpLevel;
          _tmpLevel = (int) (_stmt.getLong(_columnIndexOfLevel));
          final long _tmpBoxId;
          _tmpBoxId = _stmt.getLong(_columnIndexOfBoxId);
          final String _tmpMemoURI;
          if (_stmt.isNull(_columnIndexOfMemoURI)) {
            _tmpMemoURI = null;
          } else {
            _tmpMemoURI = _stmt.getText(_columnIndexOfMemoURI);
          }
          final long _tmpCategoryId;
          _tmpCategoryId = _stmt.getLong(_columnIndexOfCategoryId);
          _item_1 = new Card(_tmpCardId,_tmpWord,_tmpMeaning,_tmpNotes,_tmpDateAdded,_tmpLevel,_tmpBoxId,_tmpMemoURI,_tmpCategoryId);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _stmt.close();
    }
  }

  private void __fetchRelationshipTagAscomExampleIndexcardsDataTag_1(
      @NonNull final SQLiteConnection _connection,
      @NonNull final LongSparseArray<ArrayList<Tag>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > 999) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (_tmpMap) -> {
        __fetchRelationshipTagAscomExampleIndexcardsDataTag_1(_connection, _tmpMap);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = new StringBuilder();
    _stringBuilder.append("SELECT `Tag`.`tagId` AS `tagId`,`Tag`.`boxId` AS `boxId`,`Tag`.`text` AS `text`,`Tag`.`color` AS `color`,_junction.`cardId` FROM `TagCardCrossRef` AS _junction INNER JOIN `Tag` ON (_junction.`tagId` = `Tag`.`tagId`) WHERE _junction.`cardId` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final SQLiteStatement _stmt = _connection.prepare(_sql);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    try {
      // _junction.cardId;
      final int _itemKeyIndex = 4;
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _columnIndexOfTagId = 0;
      final int _columnIndexOfBoxId = 1;
      final int _columnIndexOfText = 2;
      final int _columnIndexOfColor = 3;
      while (_stmt.step()) {
        final long _tmpKey;
        _tmpKey = _stmt.getLong(_itemKeyIndex);
        final ArrayList<Tag> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final Tag _item_1;
          final long _tmpTagId;
          _tmpTagId = _stmt.getLong(_columnIndexOfTagId);
          final long _tmpBoxId;
          _tmpBoxId = _stmt.getLong(_columnIndexOfBoxId);
          final String _tmpText;
          if (_stmt.isNull(_columnIndexOfText)) {
            _tmpText = null;
          } else {
            _tmpText = _stmt.getText(_columnIndexOfText);
          }
          final String _tmpColor;
          if (_stmt.isNull(_columnIndexOfColor)) {
            _tmpColor = null;
          } else {
            _tmpColor = _stmt.getText(_columnIndexOfColor);
          }
          _item_1 = new Tag(_tmpTagId,_tmpBoxId,_tmpText,_tmpColor);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _stmt.close();
    }
  }
}
