package com.example.indexcards.data

import kotlinx.coroutines.flow.Flow

interface AppRepository {
    suspend fun upsertBox(box: Box)

    suspend fun upsertCard(card: Card)

    suspend fun insertTag(tag: Tag)

    suspend fun updateTag(tag: Tag)

    suspend fun upsertTagCardCrossRef(tagCrossRef: TagCardCrossRef)

    suspend fun deleteBox(boxId: Long)

    suspend fun deleteCard(cardId: Long)

    suspend fun deleteTag(tagId: Long)

    suspend fun deleteTagCardCrossRef(tagId: Long, cardId: Long)

    fun getAllBoxesStream(): Flow<List<Box>>

    fun getBox(boxId: Long): Flow<Box>

    fun getCard(cardId: Long): Flow<Card>

    fun getTag(tagId: Long): Flow<Tag>

    fun getBoxWithCardsStream(boxId: Long): Flow<BoxWithCards>

    fun getAllCardsWithTagsOfBoxStream(boxId: Long): Flow<List<CardWithTags>>

    fun getBoxWithTagsStream(boxId: Long): Flow<BoxWithTags>

    fun getCardWithTagsStream(cardId: Long): Flow<CardWithTags>

    fun getTagWithCardsStream(tagId: Long): Flow<TagWithCards>

    suspend fun getBiggestBoxId(): Long

    suspend fun getBiggestCardId(): Long

    suspend fun getBiggestTagId(): Long

    suspend fun upgradeLevelOnCard(cardId: Long)

    suspend fun downgradeLevelOnCard(cardId: Long)

    suspend fun enableNotificationsForBox(boxId: Long)

    suspend fun disableNotificationsForBox(boxId: Long)

    suspend fun getNumberOfCardsOfLevelInBox(boxId: Long, level: Int): Int
}
