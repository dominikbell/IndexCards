package com.example.indexcards.data

import kotlinx.coroutines.flow.Flow

class OfflineAppRepository(
    private val appDao: AppDao
): AppRepository {
    override fun getAllBoxesStream(): Flow<List<Box>> =
        appDao.getAllBoxes()

    override fun getBoxStream(id: Long): Flow<Box> =
        appDao.getBox(id)

    override suspend fun insertBox(box: Box) =
        appDao.upsertBox(box)

    override suspend fun deleteBox(box: Box) =
        appDao.deleteBox(box)

    override suspend fun updateBox(box: Box) =
        appDao.upsertBox(box)
}