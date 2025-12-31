package dev.nordix.irbridge.core.utils

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlin.uuid.Uuid

/**
 * Common interface to create identifiable instances,
 * provides a field [id][ID] representing an unique id af an instance.
 * @property id Unique [id][ID]
 */
interface Identifiable<T> {
    val id: ID<T>
}

@Serializable
@JvmInline
value class ID<out T>(val value: String) {
    constructor(value: Uuid) : this(value.toString())

    fun asUuid() : Uuid? = try {
        Uuid.parse(value)
    } catch (_: Throwable) {
        null
    }

    companion object {
        fun <T> new() = ID<T>(Uuid.random().toString())
    }

}
