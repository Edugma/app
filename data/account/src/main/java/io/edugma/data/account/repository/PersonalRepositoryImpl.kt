package io.edugma.data.account.repository

import android.util.Log
import io.edugma.data.account.api.AccountService
import io.edugma.data.base.local.PreferencesDS
import io.edugma.data.base.local.getJsonLazy
import io.edugma.data.base.local.setJsonLazy
import io.edugma.domain.account.model.Order
import io.edugma.domain.account.model.Personal
import io.edugma.domain.account.repository.PersonalRepository
import io.edugma.domain.base.utils.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PersonalRepositoryImpl(
    private val api: AccountService,
    private val localStore: PreferencesDS
): PersonalRepository {
    override fun getPersonalInfo() =
        api.getPersonalInfo()
            .onSuccess { setLocalPersonalInfo(it) }
            .flowOn(Dispatchers.IO)

    override fun getOrders() =
        api.getOrders()
            .onSuccess { setLocalOrders(it) }
            .flowOn(Dispatchers.IO)

    override suspend fun setLocalPersonalInfo(personal: Personal) {
        localStore.setJsonLazy(personal)
    }

    override suspend fun getLocalPersonalInfo(): Personal? = localStore.getJsonLazy()

    override suspend fun setLocalOrders(orders: List<Order>) {
        localStore.setJsonLazy(orders, Order.TAG)
    }

    override suspend fun getLocalOrders(): List<Order>? {
        return localStore.getJsonLazy(Order.TAG)
    }
}