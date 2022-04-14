package org.laolittle.plugin.gif

import java.io.File

public class Writer(_ptr: RawPointer) : GifNative(_ptr) {
    public var canWrite: Boolean = true
        private set

    public suspend fun writeToFile(file: File) {
        if (!canWrite) error("Cannot write because already written")
        canWrite = false
        nWriteToFile(file.absolutePath, ptr)
    }

    public suspend fun writeToFile(path: String) {
        if (!canWrite) error("Cannot write because already written")
        canWrite = false
        nWriteToFile(path, ptr)
    }

    public suspend fun writeToBytes(): ByteArray {
        if (!canWrite) error("Cannot write because already written")
        canWrite = false
        return nWriteToBytes(ptr)
    }

    override fun close() {
        canWrite = false
        if (dropped) error("Already closed")
        nCloseWriter(ptr)
    }
}

private external suspend fun nWriteToFile(path: String, writer: RawPointer)

private external suspend fun nWriteToBytes(writer: RawPointer): ByteArray

private external fun nCloseWriter(writer: RawPointer)