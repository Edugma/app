package com.mospolytech.mospolyhelper.data.addresses.repository

import com.mospolytech.mospolyhelper.data.addresses.local.AddressesLocalAssetsDataSource
import com.mospolytech.mospolyhelper.data.addresses.local.AddressesLocalStorageDataSource
import com.mospolytech.mospolyhelper.data.addresses.remote.AddressesRemoteDataSource
import com.mospolytech.mospolyhelper.domain.addresses.model.AddressMap
import com.mospolytech.mospolyhelper.domain.addresses.repository.AddressesRepository
import kotlinx.coroutines.flow.flow

class AddressesRepositoryImpl(
    private val remoteDataSource: AddressesRemoteDataSource,
    private val localStorageDataSource: AddressesLocalStorageDataSource,
    private val localAssetsDataSource: AddressesLocalAssetsDataSource
): AddressesRepository {

    override fun getAddresses(refresh: Boolean) = flow<AddressMap?> {
        emit(get(refresh) ?: get(!refresh))
    }

    private fun get(refresh: Boolean): AddressMap? {
        val addressMap: AddressMap?
        if (refresh) {
            addressMap = remoteDataSource.get()
            if (addressMap != null) {
                localStorageDataSource.set(addressMap)
            }
        } else {
            addressMap = localStorageDataSource.get() ?: localAssetsDataSource.get()
        }
        return addressMap
    }
}