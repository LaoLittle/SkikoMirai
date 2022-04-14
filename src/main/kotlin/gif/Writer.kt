package org.laolittle.plugin.gif

import java.io.File

public class Writer(_ptr: RawPointer) : GifNative(_ptr) {
    public val canWrite: Boolean get() = !dropped

    public suspend fun writeToFile(file: File) {
        if (!canWrite) error("Cannot write because already written")
        nWriteToFile(file.absolutePath, ptr)
        dropped = true
    }

    public suspend fun writeToFile(path: String) {
        if (!canWrite) error("Cannot write because already written")
        nWriteToFile(path, ptr)
        dropped = true
    }

    public suspend fun writeToBytes(): ByteArray {
        if (!canWrite) error("Cannot write because already written")
        return nWriteToBytes(ptr).also {
            dropped = true
        }
    }
}

private external suspend fun nWriteToFile(path: String, writer: RawPointer)

private external suspend fun nWriteToBytes(writer: RawPointer): ByteArray