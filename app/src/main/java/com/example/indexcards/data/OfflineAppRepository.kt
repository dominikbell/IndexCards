package com.example.indexcards.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class OfflineAppRepository(
    private val appDao: AppDao
) : AppRepository {
    override suspend fun upsertBox(box: Box) =
        appDao.upsertBox(box)

    override suspend fun upsertCard(card: Card) =
        appDao.upsertCard(card)

    override suspend fun insertTag(tag: Tag) =
        appDao.upsertTag(tag)

    override suspend fun updateTag(tag: Tag) =
        appDao.updateTag(tagId = tag.tagId, text = tag.text, color = tag.color)

    override suspend fun upsertTagCardCrossRef(tagCrossRef: TagCardCrossRef) =
        appDao.upsertTagCardCrossRef(tagCrossRef)

    override suspend fun deleteBox(boxId: Long) {
        appDao.getBoxWithCards(boxId).first().cards
            .forEach {
                appDao.deleteCardFromTags(cardId = it.cardId)
            }
        appDao.deleteTagsFromBox(boxId = boxId)
        appDao.deleteCardsFromBox(boxId = boxId)
        appDao.deleteBox(boxId = boxId)
    }

    override suspend fun deleteCard(cardId: Long) {
        appDao.deleteCardFromTags(cardId = cardId)
        appDao.deleteCard(cardId = cardId)
    }

    override suspend fun deleteTag(tagId: Long) {
        appDao.deleteTagsFromCard(tagId = tagId)
        appDao.deleteTag(tagId = tagId)
    }

    override suspend fun deleteTagCardCrossRef(tagId: Long, cardId: Long) =
        appDao.deleteTagCardCrossRef(TagCardCrossRef(tagId, cardId))

    override fun getAllBoxesStream(): Flow<List<Box>> =
        appDao.getAllBoxes()

    override fun getBox(boxId: Long): Flow<Box> =
        appDao.getBox(boxId)

    override fun getCard(cardId: Long): Flow<Card> =
        appDao.getCard(cardId)

    override fun getTag(tagId: Long): Flow<Tag> =
        appDao.getTag(tagId)

    override fun getBoxWithCardsStream(boxId: Long): Flow<BoxWithCards> =
        appDao.getBoxWithCards(boxId)

    override fun getBoxWithTagsStream(boxId: Long): Flow<BoxWithTags> =
        appDao.getBoxWithTags(boxId)

    override fun getCardWithTagsStream(cardId: Long): Flow<CardWithTags> =
        appDao.getCardWithTags(cardId)

    override fun getTagWithCardsStream(tagId: Long): Flow<TagWithCards> =
        appDao.getTagWithCards(tagId)

    override fun getBiggestCardId(): Flow<Long> =
        appDao.getBiggestCardId()
}