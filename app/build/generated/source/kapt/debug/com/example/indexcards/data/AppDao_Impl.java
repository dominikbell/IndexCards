package com.example.indexcards.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.EntityUpsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
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

  private final EntityDeletionOrUpdateAdapter<TagCardCrossRef> __deletionAdapterOfTagCardCrossRef;

  private final SharedSQLiteStatement __preparedStmtOfUpdateTag;

  private final SharedSQLiteStatement __preparedStmtOfDeleteBox;

  private final SharedSQLiteStatement __preparedStmtOfDeleteCard;

  private final SharedSQLiteStatement __preparedStmtOfDeleteTag;

  private final SharedSQLiteStatement __preparedStmtOfDeleteCardsFromBox;

  private final SharedSQLiteStatement __preparedStmtOfDeleteTagsFromBox;

  private final SharedSQLiteStatement __preparedStmtOfDeleteTagsFromCard;

  private final SharedSQLiteStatement __preparedStmtOfDeleteCardFromTags;

  private final SharedSQLiteStatement __preparedStmtOfUpgradeLevelOnCard;

  private final SharedSQLiteStatement __preparedStmtOfDowngradeLevelOnCard;

  private final SharedSQLiteStatement __preparedStmtOfEnableNotificationsForBox;

  private final SharedSQLiteStatement __preparedStmtOfDisableNotificationsForBox;

  private final EntityUpsertionAdapter<Box> __upsertionAdapterOfBox;

  private final EntityUpsertionAdapter<Card> __upsertionAdapterOfCard;

  private final EntityUpsertionAdapter<Tag> __upsertionAdapterOfTag;

  private final EntityUpsertionAdapter<TagCardCrossRef> __upsertionAdapterOfTagCardCrossRef;

  public AppDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__deletionAdapterOfTagCardCrossRef = new EntityDeletionOrUpdateAdapter<TagCardCrossRef>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `TagCardCrossRef` WHERE `tagId` = ? AND `cardId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TagCardCrossRef entity) {
        statement.bindLong(1, entity.getTagId());
        statement.bindLong(2, entity.getCardId());
      }
    };
    this.__preparedStmtOfUpdateTag = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE tag SET text = ?, color = ? WHERE tagId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteBox = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM box WHERE boxId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteCard = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM card WHERE cardId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteTag = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM tag WHERE tagId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteCardsFromBox = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM card WHERE boxId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteTagsFromBox = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM tag WHERE boxId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteTagsFromCard = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM tagcardcrossref WHERE tagId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteCardFromTags = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM tagcardcrossref WHERE cardId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpgradeLevelOnCard = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE Card SET level = level + 1 WHERE cardId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDowngradeLevelOnCard = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE Card SET level = level - 1 WHERE cardId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfEnableNotificationsForBox = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE Box SET reminders = 1 WHERE boxId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDisableNotificationsForBox = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE Box SET reminders = 0 WHERE boxId = ?";
        return _query;
      }
    };
    this.__upsertionAdapterOfBox = new EntityUpsertionAdapter<Box>(new EntityInsertionAdapter<Box>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT INTO `Box` (`boxId`,`name`,`topic`,`reminders`,`description`,`dateAdded`) VALUES (nullif(?, 0),?,?,?,?,?)";
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
        final int _tmp = entity.getReminders() ? 1 : 0;
        statement.bindLong(4, _tmp);
        if (entity.getDescription() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getDescription());
        }
        statement.bindLong(6, entity.getDateAdded());
      }
    }, new EntityDeletionOrUpdateAdapter<Box>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE `Box` SET `boxId` = ?,`name` = ?,`topic` = ?,`reminders` = ?,`description` = ?,`dateAdded` = ? WHERE `boxId` = ?";
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
        final int _tmp = entity.getReminders() ? 1 : 0;
        statement.bindLong(4, _tmp);
        if (entity.getDescription() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getDescription());
        }
        statement.bindLong(6, entity.getDateAdded());
        statement.bindLong(7, entity.getBoxId());
      }
    });
    this.__upsertionAdapterOfCard = new EntityUpsertionAdapter<Card>(new EntityInsertionAdapter<Card>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT INTO `Card` (`cardId`,`word`,`meaning`,`notes`,`dateAdded`,`level`,`boxId`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
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
        if (entity.getNotes() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getNotes());
        }
        statement.bindLong(5, entity.getDateAdded());
        statement.bindLong(6, entity.getLevel());
        statement.bindLong(7, entity.getBoxId());
      }
    }, new EntityDeletionOrUpdateAdapter<Card>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE `Card` SET `cardId` = ?,`word` = ?,`meaning` = ?,`notes` = ?,`dateAdded` = ?,`level` = ?,`boxId` = ? WHERE `cardId` = ?";
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
        if (entity.getNotes() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getNotes());
        }
        statement.bindLong(5, entity.getDateAdded());
        statement.bindLong(6, entity.getLevel());
        statement.bindLong(7, entity.getBoxId());
        statement.bindLong(8, entity.getCardId());
      }
    });
    this.__upsertionAdapterOfTag = new EntityUpsertionAdapter<Tag>(new EntityInsertionAdapter<Tag>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT INTO `Tag` (`tagId`,`boxId`,`text`,`color`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Tag entity) {
        statement.bindLong(1, entity.getTagId());
        statement.bindLong(2, entity.getBoxId());
        if (entity.getText() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getText());
        }
        if (entity.getColor() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getColor());
        }
      }
    }, new EntityDeletionOrUpdateAdapter<Tag>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE `Tag` SET `tagId` = ?,`boxId` = ?,`text` = ?,`color` = ? WHERE `tagId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Tag entity) {
        statement.bindLong(1, entity.getTagId());
        statement.bindLong(2, entity.getBoxId());
        if (entity.getText() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getText());
        }
        if (entity.getColor() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getColor());
        }
        statement.bindLong(5, entity.getTagId());
      }
    });
    this.__upsertionAdapterOfTagCardCrossRef = new EntityUpsertionAdapter<TagCardCrossRef>(new EntityInsertionAdapter<TagCardCrossRef>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT INTO `TagCardCrossRef` (`tagId`,`cardId`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TagCardCrossRef entity) {
        statement.bindLong(1, entity.getTagId());
        statement.bindLong(2, entity.getCardId());
      }
    }, new EntityDeletionOrUpdateAdapter<TagCardCrossRef>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE `TagCardCrossRef` SET `tagId` = ?,`cardId` = ? WHERE `tagId` = ? AND `cardId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
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
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfTagCardCrossRef.handle(crossRef);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateTag(final long tagId, final String text, final String color,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateTag.acquire();
        int _argIndex = 1;
        if (text == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, text);
        }
        _argIndex = 2;
        if (color == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, color);
        }
        _argIndex = 3;
        _stmt.bindLong(_argIndex, tagId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateTag.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteBox(final long boxId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteBox.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, boxId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteBox.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteCard(final long cardId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteCard.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, cardId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteCard.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteTag(final long tagId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteTag.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, tagId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteTag.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteCardsFromBox(final long boxId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteCardsFromBox.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, boxId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteCardsFromBox.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteTagsFromBox(final long boxId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteTagsFromBox.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, boxId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteTagsFromBox.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteTagsFromCard(final long tagId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteTagsFromCard.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, tagId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteTagsFromCard.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteCardFromTags(final long cardId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteCardFromTags.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, cardId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteCardFromTags.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object upgradeLevelOnCard(final long cardId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpgradeLevelOnCard.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, cardId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpgradeLevelOnCard.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object downgradeLevelOnCard(final long cardId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDowngradeLevelOnCard.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, cardId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDowngradeLevelOnCard.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object enableNotificationsForBox(final long boxId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfEnableNotificationsForBox.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, boxId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfEnableNotificationsForBox.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object disableNotificationsForBox(final long boxId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDisableNotificationsForBox.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, boxId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDisableNotificationsForBox.release(_stmt);
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
  public Object upsertTagCardCrossRef(final TagCardCrossRef crossRef,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfTagCardCrossRef.upsert(crossRef);
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
          final int _cursorIndexOfReminders = CursorUtil.getColumnIndexOrThrow(_cursor, "reminders");
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
            final boolean _tmpReminders;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfReminders);
            _tmpReminders = _tmp != 0;
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final long _tmpDateAdded;
            _tmpDateAdded = _cursor.getLong(_cursorIndexOfDateAdded);
            _item = new Box(_tmpBoxId,_tmpName,_tmpTopic,_tmpReminders,_tmpDescription,_tmpDateAdded);
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
  public Flow<Box> getBox(final long boxId) {
    final String _sql = "SELECT * FROM box WHERE boxId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, boxId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"box"}, new Callable<Box>() {
      @Override
      @NonNull
      public Box call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfBoxId = CursorUtil.getColumnIndexOrThrow(_cursor, "boxId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfTopic = CursorUtil.getColumnIndexOrThrow(_cursor, "topic");
          final int _cursorIndexOfReminders = CursorUtil.getColumnIndexOrThrow(_cursor, "reminders");
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
            final boolean _tmpReminders;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfReminders);
            _tmpReminders = _tmp != 0;
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final long _tmpDateAdded;
            _tmpDateAdded = _cursor.getLong(_cursorIndexOfDateAdded);
            _result = new Box(_tmpBoxId,_tmpName,_tmpTopic,_tmpReminders,_tmpDescription,_tmpDateAdded);
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
  public Flow<Card> getCard(final long cardId) {
    final String _sql = "SELECT * FROM card WHERE cardId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, cardId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"card"}, new Callable<Card>() {
      @Override
      @NonNull
      public Card call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfCardId = CursorUtil.getColumnIndexOrThrow(_cursor, "cardId");
          final int _cursorIndexOfWord = CursorUtil.getColumnIndexOrThrow(_cursor, "word");
          final int _cursorIndexOfMeaning = CursorUtil.getColumnIndexOrThrow(_cursor, "meaning");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfDateAdded = CursorUtil.getColumnIndexOrThrow(_cursor, "dateAdded");
          final int _cursorIndexOfLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "level");
          final int _cursorIndexOfBoxId = CursorUtil.getColumnIndexOrThrow(_cursor, "boxId");
          final Card _result;
          if (_cursor.moveToFirst()) {
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
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final long _tmpDateAdded;
            _tmpDateAdded = _cursor.getLong(_cursorIndexOfDateAdded);
            final int _tmpLevel;
            _tmpLevel = _cursor.getInt(_cursorIndexOfLevel);
            final long _tmpBoxId;
            _tmpBoxId = _cursor.getLong(_cursorIndexOfBoxId);
            _result = new Card(_tmpCardId,_tmpWord,_tmpMeaning,_tmpNotes,_tmpDateAdded,_tmpLevel,_tmpBoxId);
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
  public Flow<Tag> getTag(final long tagId) {
    final String _sql = "SELECT * FROM tag WHERE tagId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, tagId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tag"}, new Callable<Tag>() {
      @Override
      @NonNull
      public Tag call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfTagId = CursorUtil.getColumnIndexOrThrow(_cursor, "tagId");
          final int _cursorIndexOfBoxId = CursorUtil.getColumnIndexOrThrow(_cursor, "boxId");
          final int _cursorIndexOfText = CursorUtil.getColumnIndexOrThrow(_cursor, "text");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final Tag _result;
          if (_cursor.moveToFirst()) {
            final long _tmpTagId;
            _tmpTagId = _cursor.getLong(_cursorIndexOfTagId);
            final long _tmpBoxId;
            _tmpBoxId = _cursor.getLong(_cursorIndexOfBoxId);
            final String _tmpText;
            if (_cursor.isNull(_cursorIndexOfText)) {
              _tmpText = null;
            } else {
              _tmpText = _cursor.getString(_cursorIndexOfText);
            }
            final String _tmpColor;
            if (_cursor.isNull(_cursorIndexOfColor)) {
              _tmpColor = null;
            } else {
              _tmpColor = _cursor.getString(_cursorIndexOfColor);
            }
            _result = new Tag(_tmpTagId,_tmpBoxId,_tmpText,_tmpColor);
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
    return CoroutinesRoom.createFlow(__db, false, new String[] {"Card",
        "box"}, new Callable<BoxWithCards>() {
      @Override
      @NonNull
      public BoxWithCards call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
        try {
          final int _cursorIndexOfBoxId = CursorUtil.getColumnIndexOrThrow(_cursor, "boxId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfTopic = CursorUtil.getColumnIndexOrThrow(_cursor, "topic");
          final int _cursorIndexOfReminders = CursorUtil.getColumnIndexOrThrow(_cursor, "reminders");
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
          __fetchRelationshipCardAscomExampleIndexcardsDataCard(_collectionCards);
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
            final boolean _tmpReminders;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfReminders);
            _tmpReminders = _tmp != 0;
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final long _tmpDateAdded;
            _tmpDateAdded = _cursor.getLong(_cursorIndexOfDateAdded);
            _tmpBox = new Box(_tmpBoxId,_tmpName,_tmpTopic,_tmpReminders,_tmpDescription,_tmpDateAdded);
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
  public Flow<BoxWithTags> getBoxWithTags(final long boxId) {
    final String _sql = "SELECT * FROM box WHERE boxId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, boxId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"Tag",
        "box"}, new Callable<BoxWithTags>() {
      @Override
      @NonNull
      public BoxWithTags call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
        try {
          final int _cursorIndexOfBoxId = CursorUtil.getColumnIndexOrThrow(_cursor, "boxId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfTopic = CursorUtil.getColumnIndexOrThrow(_cursor, "topic");
          final int _cursorIndexOfReminders = CursorUtil.getColumnIndexOrThrow(_cursor, "reminders");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfDateAdded = CursorUtil.getColumnIndexOrThrow(_cursor, "dateAdded");
          final LongSparseArray<ArrayList<Tag>> _collectionTags = new LongSparseArray<ArrayList<Tag>>();
          while (_cursor.moveToNext()) {
            final long _tmpKey;
            _tmpKey = _cursor.getLong(_cursorIndexOfBoxId);
            if (!_collectionTags.containsKey(_tmpKey)) {
              _collectionTags.put(_tmpKey, new ArrayList<Tag>());
            }
          }
          _cursor.moveToPosition(-1);
          __fetchRelationshipTagAscomExampleIndexcardsDataTag(_collectionTags);
          final BoxWithTags _result;
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
            final boolean _tmpReminders;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfReminders);
            _tmpReminders = _tmp != 0;
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final long _tmpDateAdded;
            _tmpDateAdded = _cursor.getLong(_cursorIndexOfDateAdded);
            _tmpBox = new Box(_tmpBoxId,_tmpName,_tmpTopic,_tmpReminders,_tmpDescription,_tmpDateAdded);
            final ArrayList<Tag> _tmpTagsCollection;
            final long _tmpKey_1;
            _tmpKey_1 = _cursor.getLong(_cursorIndexOfBoxId);
            _tmpTagsCollection = _collectionTags.get(_tmpKey_1);
            _result = new BoxWithTags(_tmpBox,_tmpTagsCollection);
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
  public Flow<TagWithCards> getTagWithCards(final long tagId) {
    final String _sql = "SELECT * FROM tag WHERE tagId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, tagId);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"TagCardCrossRef", "Card",
        "tag"}, new Callable<TagWithCards>() {
      @Override
      @NonNull
      public TagWithCards call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfTagId = CursorUtil.getColumnIndexOrThrow(_cursor, "tagId");
            final int _cursorIndexOfBoxId = CursorUtil.getColumnIndexOrThrow(_cursor, "boxId");
            final int _cursorIndexOfText = CursorUtil.getColumnIndexOrThrow(_cursor, "text");
            final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
            final LongSparseArray<ArrayList<Card>> _collectionCards = new LongSparseArray<ArrayList<Card>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfTagId);
              if (!_collectionCards.containsKey(_tmpKey)) {
                _collectionCards.put(_tmpKey, new ArrayList<Card>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipCardAscomExampleIndexcardsDataCard_1(_collectionCards);
            final TagWithCards _result;
            if (_cursor.moveToFirst()) {
              final Tag _tmpTag;
              final long _tmpTagId;
              _tmpTagId = _cursor.getLong(_cursorIndexOfTagId);
              final long _tmpBoxId;
              _tmpBoxId = _cursor.getLong(_cursorIndexOfBoxId);
              final String _tmpText;
              if (_cursor.isNull(_cursorIndexOfText)) {
                _tmpText = null;
              } else {
                _tmpText = _cursor.getString(_cursorIndexOfText);
              }
              final String _tmpColor;
              if (_cursor.isNull(_cursorIndexOfColor)) {
                _tmpColor = null;
              } else {
                _tmpColor = _cursor.getString(_cursorIndexOfColor);
              }
              _tmpTag = new Tag(_tmpTagId,_tmpBoxId,_tmpText,_tmpColor);
              final ArrayList<Card> _tmpCardsCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfTagId);
              _tmpCardsCollection = _collectionCards.get(_tmpKey_1);
              _result = new TagWithCards(_tmpTag,_tmpCardsCollection);
            } else {
              _result = null;
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<CardWithTags> getCardWithTags(final long cardId) {
    final String _sql = "SELECT * FROM card WHERE cardId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, cardId);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"TagCardCrossRef", "Tag",
        "card"}, new Callable<CardWithTags>() {
      @Override
      @NonNull
      public CardWithTags call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfCardId = CursorUtil.getColumnIndexOrThrow(_cursor, "cardId");
            final int _cursorIndexOfWord = CursorUtil.getColumnIndexOrThrow(_cursor, "word");
            final int _cursorIndexOfMeaning = CursorUtil.getColumnIndexOrThrow(_cursor, "meaning");
            final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
            final int _cursorIndexOfDateAdded = CursorUtil.getColumnIndexOrThrow(_cursor, "dateAdded");
            final int _cursorIndexOfLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "level");
            final int _cursorIndexOfBoxId = CursorUtil.getColumnIndexOrThrow(_cursor, "boxId");
            final LongSparseArray<ArrayList<Tag>> _collectionTags = new LongSparseArray<ArrayList<Tag>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfCardId);
              if (!_collectionTags.containsKey(_tmpKey)) {
                _collectionTags.put(_tmpKey, new ArrayList<Tag>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipTagAscomExampleIndexcardsDataTag_1(_collectionTags);
            final CardWithTags _result;
            if (_cursor.moveToFirst()) {
              final Card _tmpCard;
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
              final String _tmpNotes;
              if (_cursor.isNull(_cursorIndexOfNotes)) {
                _tmpNotes = null;
              } else {
                _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
              }
              final long _tmpDateAdded;
              _tmpDateAdded = _cursor.getLong(_cursorIndexOfDateAdded);
              final int _tmpLevel;
              _tmpLevel = _cursor.getInt(_cursorIndexOfLevel);
              final long _tmpBoxId;
              _tmpBoxId = _cursor.getLong(_cursorIndexOfBoxId);
              _tmpCard = new Card(_tmpCardId,_tmpWord,_tmpMeaning,_tmpNotes,_tmpDateAdded,_tmpLevel,_tmpBoxId);
              final ArrayList<Tag> _tmpTagsCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfCardId);
              _tmpTagsCollection = _collectionTags.get(_tmpKey_1);
              _result = new CardWithTags(_tmpCard,_tmpTagsCollection);
            } else {
              _result = null;
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<CardWithTags>> getAllCardsWithTagsOfBox(final long boxId) {
    final String _sql = "SELECT * FROM card WHERE boxId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, boxId);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"TagCardCrossRef", "Tag",
        "card"}, new Callable<List<CardWithTags>>() {
      @Override
      @NonNull
      public List<CardWithTags> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfCardId = CursorUtil.getColumnIndexOrThrow(_cursor, "cardId");
            final int _cursorIndexOfWord = CursorUtil.getColumnIndexOrThrow(_cursor, "word");
            final int _cursorIndexOfMeaning = CursorUtil.getColumnIndexOrThrow(_cursor, "meaning");
            final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
            final int _cursorIndexOfDateAdded = CursorUtil.getColumnIndexOrThrow(_cursor, "dateAdded");
            final int _cursorIndexOfLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "level");
            final int _cursorIndexOfBoxId = CursorUtil.getColumnIndexOrThrow(_cursor, "boxId");
            final LongSparseArray<ArrayList<Tag>> _collectionTags = new LongSparseArray<ArrayList<Tag>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfCardId);
              if (!_collectionTags.containsKey(_tmpKey)) {
                _collectionTags.put(_tmpKey, new ArrayList<Tag>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipTagAscomExampleIndexcardsDataTag_1(_collectionTags);
            final List<CardWithTags> _result = new ArrayList<CardWithTags>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final CardWithTags _item;
              final Card _tmpCard;
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
              final String _tmpNotes;
              if (_cursor.isNull(_cursorIndexOfNotes)) {
                _tmpNotes = null;
              } else {
                _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
              }
              final long _tmpDateAdded;
              _tmpDateAdded = _cursor.getLong(_cursorIndexOfDateAdded);
              final int _tmpLevel;
              _tmpLevel = _cursor.getInt(_cursorIndexOfLevel);
              final long _tmpBoxId;
              _tmpBoxId = _cursor.getLong(_cursorIndexOfBoxId);
              _tmpCard = new Card(_tmpCardId,_tmpWord,_tmpMeaning,_tmpNotes,_tmpDateAdded,_tmpLevel,_tmpBoxId);
              final ArrayList<Tag> _tmpTagsCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfCardId);
              _tmpTagsCollection = _collectionTags.get(_tmpKey_1);
              _item = new CardWithTags(_tmpCard,_tmpTagsCollection);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getBiggestCardId(final Continuation<? super Long> $completion) {
    final String _sql = "SELECT MAX(cardId) FROM card";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Long>() {
      @Override
      @Nullable
      public Long call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Long _result;
          if (_cursor.moveToFirst()) {
            final Long _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getBiggestTagId(final Continuation<? super Long> $completion) {
    final String _sql = "SELECT MAX(tagId) FROM tag";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Long>() {
      @Override
      @Nullable
      public Long call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Long _result;
          if (_cursor.moveToFirst()) {
            final Long _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getNumberOfCardsOfLevelInBox(final long boxId, final int level,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM card WHERE boxId = ? AND level = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, boxId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, level);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private void __fetchRelationshipCardAscomExampleIndexcardsDataCard(
      @NonNull final LongSparseArray<ArrayList<Card>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshipCardAscomExampleIndexcardsDataCard(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `cardId`,`word`,`meaning`,`notes`,`dateAdded`,`level`,`boxId` FROM `Card` WHERE `boxId` IN (");
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
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "boxId");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfCardId = 0;
      final int _cursorIndexOfWord = 1;
      final int _cursorIndexOfMeaning = 2;
      final int _cursorIndexOfNotes = 3;
      final int _cursorIndexOfDateAdded = 4;
      final int _cursorIndexOfLevel = 5;
      final int _cursorIndexOfBoxId = 6;
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
          final String _tmpNotes;
          if (_cursor.isNull(_cursorIndexOfNotes)) {
            _tmpNotes = null;
          } else {
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
          }
          final long _tmpDateAdded;
          _tmpDateAdded = _cursor.getLong(_cursorIndexOfDateAdded);
          final int _tmpLevel;
          _tmpLevel = _cursor.getInt(_cursorIndexOfLevel);
          final long _tmpBoxId;
          _tmpBoxId = _cursor.getLong(_cursorIndexOfBoxId);
          _item_1 = new Card(_tmpCardId,_tmpWord,_tmpMeaning,_tmpNotes,_tmpDateAdded,_tmpLevel,_tmpBoxId);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }

  private void __fetchRelationshipTagAscomExampleIndexcardsDataTag(
      @NonNull final LongSparseArray<ArrayList<Tag>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshipTagAscomExampleIndexcardsDataTag(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `tagId`,`boxId`,`text`,`color` FROM `Tag` WHERE `boxId` IN (");
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
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "boxId");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfTagId = 0;
      final int _cursorIndexOfBoxId = 1;
      final int _cursorIndexOfText = 2;
      final int _cursorIndexOfColor = 3;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        final ArrayList<Tag> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final Tag _item_1;
          final long _tmpTagId;
          _tmpTagId = _cursor.getLong(_cursorIndexOfTagId);
          final long _tmpBoxId;
          _tmpBoxId = _cursor.getLong(_cursorIndexOfBoxId);
          final String _tmpText;
          if (_cursor.isNull(_cursorIndexOfText)) {
            _tmpText = null;
          } else {
            _tmpText = _cursor.getString(_cursorIndexOfText);
          }
          final String _tmpColor;
          if (_cursor.isNull(_cursorIndexOfColor)) {
            _tmpColor = null;
          } else {
            _tmpColor = _cursor.getString(_cursorIndexOfColor);
          }
          _item_1 = new Tag(_tmpTagId,_tmpBoxId,_tmpText,_tmpColor);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }

  private void __fetchRelationshipCardAscomExampleIndexcardsDataCard_1(
      @NonNull final LongSparseArray<ArrayList<Card>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshipCardAscomExampleIndexcardsDataCard_1(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `Card`.`cardId` AS `cardId`,`Card`.`word` AS `word`,`Card`.`meaning` AS `meaning`,`Card`.`notes` AS `notes`,`Card`.`dateAdded` AS `dateAdded`,`Card`.`level` AS `level`,`Card`.`boxId` AS `boxId`,_junction.`tagId` FROM `TagCardCrossRef` AS _junction INNER JOIN `Card` ON (_junction.`cardId` = `Card`.`cardId`) WHERE _junction.`tagId` IN (");
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
      // _junction.tagId;
      final int _itemKeyIndex = 7;
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfCardId = 0;
      final int _cursorIndexOfWord = 1;
      final int _cursorIndexOfMeaning = 2;
      final int _cursorIndexOfNotes = 3;
      final int _cursorIndexOfDateAdded = 4;
      final int _cursorIndexOfLevel = 5;
      final int _cursorIndexOfBoxId = 6;
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
          final String _tmpNotes;
          if (_cursor.isNull(_cursorIndexOfNotes)) {
            _tmpNotes = null;
          } else {
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
          }
          final long _tmpDateAdded;
          _tmpDateAdded = _cursor.getLong(_cursorIndexOfDateAdded);
          final int _tmpLevel;
          _tmpLevel = _cursor.getInt(_cursorIndexOfLevel);
          final long _tmpBoxId;
          _tmpBoxId = _cursor.getLong(_cursorIndexOfBoxId);
          _item_1 = new Card(_tmpCardId,_tmpWord,_tmpMeaning,_tmpNotes,_tmpDateAdded,_tmpLevel,_tmpBoxId);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }

  private void __fetchRelationshipTagAscomExampleIndexcardsDataTag_1(
      @NonNull final LongSparseArray<ArrayList<Tag>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshipTagAscomExampleIndexcardsDataTag_1(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `Tag`.`tagId` AS `tagId`,`Tag`.`boxId` AS `boxId`,`Tag`.`text` AS `text`,`Tag`.`color` AS `color`,_junction.`cardId` FROM `TagCardCrossRef` AS _junction INNER JOIN `Tag` ON (_junction.`tagId` = `Tag`.`tagId`) WHERE _junction.`cardId` IN (");
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
      // _junction.cardId;
      final int _itemKeyIndex = 4;
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfTagId = 0;
      final int _cursorIndexOfBoxId = 1;
      final int _cursorIndexOfText = 2;
      final int _cursorIndexOfColor = 3;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        final ArrayList<Tag> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final Tag _item_1;
          final long _tmpTagId;
          _tmpTagId = _cursor.getLong(_cursorIndexOfTagId);
          final long _tmpBoxId;
          _tmpBoxId = _cursor.getLong(_cursorIndexOfBoxId);
          final String _tmpText;
          if (_cursor.isNull(_cursorIndexOfText)) {
            _tmpText = null;
          } else {
            _tmpText = _cursor.getString(_cursorIndexOfText);
          }
          final String _tmpColor;
          if (_cursor.isNull(_cursorIndexOfColor)) {
            _tmpColor = null;
          } else {
            _tmpColor = _cursor.getString(_cursorIndexOfColor);
          }
          _item_1 = new Tag(_tmpTagId,_tmpBoxId,_tmpText,_tmpColor);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
