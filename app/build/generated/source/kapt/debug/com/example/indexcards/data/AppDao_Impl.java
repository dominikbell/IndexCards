package com.example.indexcards.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.collection.LongSparseArray;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.EntityUpsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDao_Impl implements AppDao {
  private final RoomDatabase __db;

  private final EntityDeletionOrUpdateAdapter<Box> __deletionAdapterOfBox;

  private final EntityDeletionOrUpdateAdapter<Card> __deletionAdapterOfCard;

  private final EntityDeletionOrUpdateAdapter<Tag> __deletionAdapterOfTag;

  private final EntityUpsertionAdapter<Box> __upsertionAdapterOfBox;

  private final EntityUpsertionAdapter<Card> __upsertionAdapterOfCard;

  private final EntityUpsertionAdapter<Tag> __upsertionAdapterOfTag;

  public AppDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__deletionAdapterOfBox = new EntityDeletionOrUpdateAdapter<Box>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `Box` WHERE `boxId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Box entity) {
        statement.bindLong(1, entity.getBoxId());
      }
    };
    this.__deletionAdapterOfCard = new EntityDeletionOrUpdateAdapter<Card>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `cards` WHERE `cardId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Card entity) {
        statement.bindLong(1, entity.getCardId());
      }
    };
    this.__deletionAdapterOfTag = new EntityDeletionOrUpdateAdapter<Tag>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `tags` WHERE `tagId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Tag entity) {
        statement.bindLong(1, entity.getTagId());
      }
    };
    this.__upsertionAdapterOfBox = new EntityUpsertionAdapter<Box>(new EntityInsertionAdapter<Box>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT INTO `Box` (`boxId`,`name`,`topic`,`description`,`dateAdded`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Box entity) {
        statement.bindLong(1, entity.getBoxId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getTopic() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getTopic());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDescription());
        }
        statement.bindLong(5, entity.getDateAdded());
      }
    }, new EntityDeletionOrUpdateAdapter<Box>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE `Box` SET `boxId` = ?,`name` = ?,`topic` = ?,`description` = ?,`dateAdded` = ? WHERE `boxId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Box entity) {
        statement.bindLong(1, entity.getBoxId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getTopic() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getTopic());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDescription());
        }
        statement.bindLong(5, entity.getDateAdded());
        statement.bindLong(6, entity.getBoxId());
      }
    });
    this.__upsertionAdapterOfCard = new EntityUpsertionAdapter<Card>(new EntityInsertionAdapter<Card>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT INTO `cards` (`cardId`,`word`,`meaning`,`dateAdded`,`level`,`boxId`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Card entity) {
        statement.bindLong(1, entity.getCardId());
        if (entity.getWord() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getWord());
        }
        if (entity.getMeaning() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getMeaning());
        }
        statement.bindLong(4, entity.getDateAdded());
        statement.bindLong(5, entity.getLevel());
        statement.bindLong(6, entity.getBoxId());
      }
    }, new EntityDeletionOrUpdateAdapter<Card>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE `cards` SET `cardId` = ?,`word` = ?,`meaning` = ?,`dateAdded` = ?,`level` = ?,`boxId` = ? WHERE `cardId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Card entity) {
        statement.bindLong(1, entity.getCardId());
        if (entity.getWord() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getWord());
        }
        if (entity.getMeaning() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getMeaning());
        }
        statement.bindLong(4, entity.getDateAdded());
        statement.bindLong(5, entity.getLevel());
        statement.bindLong(6, entity.getBoxId());
        statement.bindLong(7, entity.getCardId());
      }
    });
    this.__upsertionAdapterOfTag = new EntityUpsertionAdapter<Tag>(new EntityInsertionAdapter<Tag>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT INTO `tags` (`tagId`,`text`) VALUES (nullif(?, 0),?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Tag entity) {
        statement.bindLong(1, entity.getTagId());
        if (entity.getText() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getText());
        }
      }
    }, new EntityDeletionOrUpdateAdapter<Tag>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE `tags` SET `tagId` = ?,`text` = ? WHERE `tagId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Tag entity) {
        statement.bindLong(1, entity.getTagId());
        if (entity.getText() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getText());
        }
        statement.bindLong(3, entity.getTagId());
      }
    });
  }

  @Override
  public Object deleteBox(final Box box, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfBox.handle(box);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteCard(final Card card, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfCard.handle(card);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteTag(final Tag tag, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfTag.handle(tag);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertBox(final Box box, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfBox.upsert(box);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertCard(final Card card, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfCard.upsert(card);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertTag(final Tag tag, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfTag.upsert(tag);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Box>> getAllBoxes() {
    final String _sql = "SELECT * FROM box ORDER BY dateAdded DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"box"}, new Callable<List<Box>>() {
      @Override
      @NonNull
      public List<Box> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfBoxId = CursorUtil.getColumnIndexOrThrow(_cursor, "boxId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfTopic = CursorUtil.getColumnIndexOrThrow(_cursor, "topic");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfDateAdded = CursorUtil.getColumnIndexOrThrow(_cursor, "dateAdded");
          final List<Box> _result = new ArrayList<Box>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Box _item;
            final long _tmpBoxId;
            _tmpBoxId = _cursor.getLong(_cursorIndexOfBoxId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpTopic;
            if (_cursor.isNull(_cursorIndexOfTopic)) {
              _tmpTopic = null;
            } else {
              _tmpTopic = _cursor.getString(_cursorIndexOfTopic);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final long _tmpDateAdded;
            _tmpDateAdded = _cursor.getLong(_cursorIndexOfDateAdded);
            _item = new Box(_tmpBoxId,_tmpName,_tmpTopic,_tmpDescription,_tmpDateAdded);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Box> getBox(final long id) {
    final String _sql = "SELECT * FROM box WHERE boxId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"box"}, new Callable<Box>() {
      @Override
      @NonNull
      public Box call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfBoxId = CursorUtil.getColumnIndexOrThrow(_cursor, "boxId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfTopic = CursorUtil.getColumnIndexOrThrow(_cursor, "topic");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfDateAdded = CursorUtil.getColumnIndexOrThrow(_cursor, "dateAdded");
          final Box _result;
          if (_cursor.moveToFirst()) {
            final long _tmpBoxId;
            _tmpBoxId = _cursor.getLong(_cursorIndexOfBoxId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpTopic;
            if (_cursor.isNull(_cursorIndexOfTopic)) {
              _tmpTopic = null;
            } else {
              _tmpTopic = _cursor.getString(_cursorIndexOfTopic);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final long _tmpDateAdded;
            _tmpDateAdded = _cursor.getLong(_cursorIndexOfDateAdded);
            _result = new Box(_tmpBoxId,_tmpName,_tmpTopic,_tmpDescription,_tmpDateAdded);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<BoxWithCards> getBoxWithCards(final long boxId) {
    final String _sql = "SELECT * FROM box WHERE boxId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, boxId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"cards",
        "box"}, new Callable<BoxWithCards>() {
      @Override
      @NonNull
      public BoxWithCards call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
        try {
          final int _cursorIndexOfBoxId = CursorUtil.getColumnIndexOrThrow(_cursor, "boxId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfTopic = CursorUtil.getColumnIndexOrThrow(_cursor, "topic");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfDateAdded = CursorUtil.getColumnIndexOrThrow(_cursor, "dateAdded");
          final LongSparseArray<ArrayList<Card>> _collectionCards = new LongSparseArray<ArrayList<Card>>();
          while (_cursor.moveToNext()) {
            final long _tmpKey;
            _tmpKey = _cursor.getLong(_cursorIndexOfBoxId);
            if (!_collectionCards.containsKey(_tmpKey)) {
              _collectionCards.put(_tmpKey, new ArrayList<Card>());
            }
          }
          _cursor.moveToPosition(-1);
          __fetchRelationshipcardsAscomExampleIndexcardsDataCard(_collectionCards);
          final BoxWithCards _result;
          if (_cursor.moveToFirst()) {
            final Box _tmpBox;
            final long _tmpBoxId;
            _tmpBoxId = _cursor.getLong(_cursorIndexOfBoxId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpTopic;
            if (_cursor.isNull(_cursorIndexOfTopic)) {
              _tmpTopic = null;
            } else {
              _tmpTopic = _cursor.getString(_cursorIndexOfTopic);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final long _tmpDateAdded;
            _tmpDateAdded = _cursor.getLong(_cursorIndexOfDateAdded);
            _tmpBox = new Box(_tmpBoxId,_tmpName,_tmpTopic,_tmpDescription,_tmpDateAdded);
            final ArrayList<Card> _tmpCardsCollection;
            final long _tmpKey_1;
            _tmpKey_1 = _cursor.getLong(_cursorIndexOfBoxId);
            _tmpCardsCollection = _collectionCards.get(_tmpKey_1);
            _result = new BoxWithCards(_tmpBox,_tmpCardsCollection);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public int getNumberOfCards(final long boxId) {
    final String _sql = "SELECT COUNT(*) from box WHERE boxId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, boxId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private void __fetchRelationshipcardsAscomExampleIndexcardsDataCard(
      @NonNull final LongSparseArray<ArrayList<Card>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshipcardsAscomExampleIndexcardsDataCard(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `cardId`,`word`,`meaning`,`dateAdded`,`level`,`boxId` FROM `cards` WHERE `cardId` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "cardId");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfCardId = 0;
      final int _cursorIndexOfWord = 1;
      final int _cursorIndexOfMeaning = 2;
      final int _cursorIndexOfDateAdded = 3;
      final int _cursorIndexOfLevel = 4;
      final int _cursorIndexOfBoxId = 5;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        final ArrayList<Card> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final Card _item_1;
          final long _tmpCardId;
          _tmpCardId = _cursor.getLong(_cursorIndexOfCardId);
          final String _tmpWord;
          if (_cursor.isNull(_cursorIndexOfWord)) {
            _tmpWord = null;
          } else {
            _tmpWord = _cursor.getString(_cursorIndexOfWord);
          }
          final String _tmpMeaning;
          if (_cursor.isNull(_cursorIndexOfMeaning)) {
            _tmpMeaning = null;
          } else {
            _tmpMeaning = _cursor.getString(_cursorIndexOfMeaning);
          }
          final int _tmpDateAdded;
          _tmpDateAdded = _cursor.getInt(_cursorIndexOfDateAdded);
          final int _tmpLevel;
          _tmpLevel = _cursor.getInt(_cursorIndexOfLevel);
          final long _tmpBoxId;
          _tmpBoxId = _cursor.getLong(_cursorIndexOfBoxId);
          _item_1 = new Card(_tmpCardId,_tmpWord,_tmpMeaning,_tmpDateAdded,_tmpLevel,_tmpBoxId);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
