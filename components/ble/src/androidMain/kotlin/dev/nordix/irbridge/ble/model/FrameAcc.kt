package dev.nordix.irbridge.ble.model

data class FrameAcc(
    val fid: Int,
    val expectedCount: Int,
    val unit: Int,
    // seq -> payload bytes (u16le array)
    val chunks: Array<ByteArray?> = arrayOfNulls(ARR_SIZE),
    var receivedChunkCount: Int = 0,
) {
    companion object {
        private const val ARR_SIZE = 256
    }
}
