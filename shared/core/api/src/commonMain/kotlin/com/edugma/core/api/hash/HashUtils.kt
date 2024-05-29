package com.edugma.core.api.hash

object Hash {
    /**
     * Fast unsecure hash
     */
    fun hash(value: String): UInt {
        return MurmurHash3().hash32x86(value.encodeToByteArray())
    }

    /**
     * Fast unsecure hash
     */
    fun hash(value: ByteArray): UInt {
        return MurmurHash3().hash32x86(value)
    }
}
