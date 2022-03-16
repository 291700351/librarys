package io.github.lee.core.dao

import androidx.room.*

@Dao
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(data: T): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertList(list: List<T>): List<Long>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertData(vararg data: T): LongArray

    @Delete
    suspend fun delete(data: T)

    @Update
    suspend fun update(data: T)

    @Update
    suspend fun updateData(vararg data: T)

    @Update
    suspend fun updateList(list: List<T>)
}