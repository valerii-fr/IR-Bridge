package dev.nordix.irbridge.core.encoders

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.Base64

object IntArrayBase64Codec {

    fun encode(values: IntArray): String {
        val buffer = ByteBuffer
            .allocate(values.size * Int.SIZE_BYTES)
            .order(ByteOrder.BIG_ENDIAN)

        for (v in values) {
            buffer.putInt(v)
        }

        return Base64.getEncoder().encodeToString(buffer.array())
    }

    fun decode(base64: String): IntArray {
        val bytes = Base64.getDecoder().decode(base64)

        require(bytes.size % Int.SIZE_BYTES == 0) {
            "Invalid Base64 IntArray payload size=${bytes.size}"
        }

        val buffer = ByteBuffer
            .wrap(bytes)
            .order(ByteOrder.BIG_ENDIAN)

        val result = IntArray(bytes.size / Int.SIZE_BYTES)
        for (i in result.indices) {
            result[i] = buffer.getInt()
        }

        return result
    }
}
