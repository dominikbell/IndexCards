package com.example.indexcards.data

import kotlinx.coroutines.flow.Flow

class OfflineAppRepository(
    private val appDao: AppDao
): AppRepository {
    override fun getAllBoxesStream(): Flow<List<Box>> =
        appDao.getAllBoxes()

    override fun getBox(id: Long): Flow<Box> =
        appDao.getBox(id)

    override fun getNumberOfCards(boxId: Long): Int =
        appDao.getNumberOfCards(boxId)

    override fun getBoxWithCardsStream(boxId: Long): Flow<BoxWithCards> =
        appDao.getBoxWithCards(boxId)

    override suspend fun insertBox(box: Box) =
        appDao.upsertBox(box)

    override suspend fun deleteBox(box: Box) =
        appDao.deleteBox(box)

    override suspend fun updateBox(box: Box) =
        appDao.upsertBox(box)
}