package com.example.indexcards.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first

class OfflineAppRepository(
    private val appDao: AppDao
) : AppRepository {
    override suspend fun upsertBox(box: Box) =
        appDao.upsertBox(box)

    override suspend fun upsertCategory(category: Category) =
        appDao.upsertCategory(category)

    override suspend fun upsertCard(card: Card) =
        appDao.upsertCard(card)

    override suspend fun insertTag(tag: Tag) =
        appDao.upsertTag(tag)

    override suspend fun updateTag(tag: Tag) =
        appDao.updateTag(tagId = tag.tagId, text = tag.text, color = tag.color)

    override suspend fun upsertTagCardCrossRef(tagCrossRef: TagCardCrossRef) =
        appDao.upsertTagCardCrossRef(tagCrossRef)

    override suspend fun deleteBox(boxId: Long) {
        // Delete all TagCardCrossReferences
        appDao.getBoxWithCards(boxId)
            .filterNotNull()
            .first()
            .cards
            .forEach {
                appDao.deleteCardFromTags(cardId = it.cardId)
            }
        // Delete all cards
        appDao.deleteCardsFromBox(boxId = boxId)
        // Delete all categories
        appDao.deleteCategoriesFromBox(boxId = boxId)
        // Delete all tags
        appDao.deleteTagsFromBox(boxId = boxId)
        // Delete the box
        appDao.deleteBox(boxId = boxId)
    }

    override suspend fun deleteCategory(category: Category) {
        appDao.getBoxWithCards(category.boxId)
            .filterNotNull()
            .first()
            .cards
            .filter { it.categoryId == category.categoryId }
            .forEach {
                appDao.upsertCard(card = it.copy(categoryId = -1))
            }
        appDao.deleteCategory(categoryId = category.categoryId)
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

    override fun getBoxWithCardsStream(boxId: Long): Flow<BoxWithCards?> =
        appDao.getBoxWithCards(boxId)

    override fun getAllCardsWithTagsOfBoxStream(boxId: Long): Flow<List<CardWithTags>> =
        appDao.getAllCardsWithTagsOfBox(boxId)

    override fun getBoxWithTagsStream(boxId: Long): Flow<BoxWithTags?> =
        appDao.getBoxWithTags(boxId)

    override fun getBoxWithCategoriesStream(boxId: Long): Flow<BoxWithCategories?> =
        appDao.getBoxWithCategories(boxId)

    override fun getCardWithTagsStream(cardId: Long): Flow<CardWithTags> =
        appDao.getCardWithTags(cardId)

    override fun getTagWithCardsStream(tagId: Long): Flow<TagWithCards> =
        appDao.getTagWithCards(tagId)

    override suspend fun getBiggestBoxId(): Long {
        return appDao.getBiggestBoxId() ?: -1
    }

    override suspend fun getBiggestCategoryId(): Long {
        return appDao.getBiggestCategoryId() ?: -1
    }

    override suspend fun getBiggestCardId(): Long {
        return appDao.getBiggestCardId() ?: -1
    }

    override suspend fun getBiggestTagId(): Long {
        return appDao.getBiggestTagId() ?: -1
    }

    override suspend fun upgradeLevelOnCard(cardId: Long) = appDao.upgradeLevelOnCard(cardId)

    override suspend fun downgradeLevelOnCard(cardId: Long) = appDao.downgradeLevelOnCard(cardId)

    override suspend fun enableNotificationsForBox(boxId: Long) =
        appDao.enableNotificationsForBox(boxId)

    override suspend fun disableNotificationsForBox(boxId: Long) =
        appDao.disableNotificationsForBox(boxId)

    override suspend fun getNumberOfCardsOfLevelInBox(boxId: Long, level: Int) =
        appDao.getNumberOfCardsOfLevelInBox(boxId, level)
}