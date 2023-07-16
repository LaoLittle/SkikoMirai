package org.laolittle.plugin.gif

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runInterruptible
import java.io.File

public class Writer internal constructor(ptr: RawPointer) : GifNative(ptr) {
    public val canWrite: Boolean get() = !dropped

    /**
     * 本函数仅在[Collector.close]函数执行后返回
     */
    public suspend fun writeToFile(file: File) {
        checkWrite()
        runInterruptible(Dispatchers.IO) {
            nWriteToFile(file.absolutePath, ptr)
            dropped = true
        }
    }

    /**
     * 本函数仅在[Collector.close]函数执行后返回
     */
    public suspend fun writeToFile(path: String) {
        checkWrite()
        runInterruptible(Dispatchers.IO) {
            nWriteToFile(path, ptr)
            dropped = true
        }
    }

    /**
     * 本函数仅在[Collector.close]函数执行后返回
     */
    public suspend fun writeToBytes(): ByteArray {
        checkWrite()
        return runInterruptible(Dispatchers.IO) {
            nWriteToBytes(ptr).also {
                dropped = true
            }
        }
    }

    private fun checkWrite() {
        if (!canWrite) error("Cannot write because already written")
    }
}

private external fun nWriteToFile(path: String, writer: RawPointer)

private external fun nWriteToBytes(writer: RawPointer): ByteArray