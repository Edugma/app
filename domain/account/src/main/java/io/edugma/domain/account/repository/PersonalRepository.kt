package io.edugma.domain.account.repository

import io.edugma.domain.account.model.Order
import io.edugma.domain.account.model.Personal
import kotlinx.coroutines.flow.Flow

interface PersonalRepository {
    fun getPersonalInfo(): Flow<Result<Personal>>
    fun getOrders(): Flow<Result<List<Order>>>
    suspend fun setLocalPersonalInfo(personal: Personal)
    suspend fun setLocalOrders(orders: List<Order>)
    suspend fun getLocalPersonalInfo(): Personal?
    suspend fun getLocalOrders(): List<Order>?
}