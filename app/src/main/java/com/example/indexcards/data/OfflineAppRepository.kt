package com.example.indexcards.data

import kotlinx.coroutines.flow.Flow

class OfflineAppRepository(
    private val appDao: AppDao
): AppRepository {
    override fun getAllBoxesStream(): Flow<List<Box>> =
        appDao.getAllBoxes()

    override fun getBoxStream(id: Long): Flow<Box> =
        appDao.getBox(id)

    override fun getNumberOfCards(boxId: Long): Flow<Int> =
        appDao.getNumberOfCards(boxId)

    override fun getBoxWithCardsStream(boxId: Long): Flow<BoxWithCards> =
        appDao.getBoxWithCards(boxId)

    override fun getCardWithTagsStream(cardId: Long): Flow<CardWithTags> =
        appDao.getTagsOfCard(cardId)

    override fun getTagWithCardsStream(tagId: Long): Flow<TagWithCards> =
        appDao.getCardsOfTag(tagId)

    override fun getBoxWithTagsStream(boxId: Long): Flow<BoxWithTags> =
        appDao.getTagsOfBox(boxId)

    override suspend fun insertBox(box: Box) =
        appDao.upsertBox(box)

    override suspend fun deleteBox(boxId: Long) =
        appDao.deleteBox(boxId)

    override suspend fun updateBox(box: Box) =
        appDao.upsertBox(box)

    override suspend fun insertCard(card: Card) =
        appDao.upsertCard(card)

    override suspend fun deleteCard(cardId: Long) =
        appDao.deleteCard(cardId)

    override suspend fun updateCard(card: Card) =
        appDao.upsertCard(card)

    override suspend fun insertTag(tag: Tag) =
        appDao.upsertTag(tag)

    override suspend fun deleteTag(tagId: Long) =
        appDao.deleteTag(tagId)

    override suspend fun updateTag(tag: Tag) =
        appDao.upsertTag(tag)
}