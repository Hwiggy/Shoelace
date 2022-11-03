package me.hwiggy.shoelace.api.messages

import java.util.*
import kotlin.collections.ArrayList

object Placeholder {
    @JvmStatic private val registry = IdentityHashMap<Class<*>, MutableList<Expander<*>>>()
    @JvmStatic fun <T : Any> getValidExpanders(obj: T): List<Expander<T>> {
        val collection = ArrayList<Expander<T>>()
        for ((type, list) in registry) {
            if (type.isAssignableFrom(obj.javaClass)) {
                collection.addAll(list as List<Expander<T>>)
            }
        }
        return collection
    }

    @JvmStatic fun <T : Any> register(type: Class<T>, expander: Expander<T>) {
        registry.computeIfAbsent(type) { ArrayList() }.add(expander)
    }

    @JvmStatic inline fun <reified T : Any> register(expander: Expander<T>) {
        register(T::class.java, expander)
    }

    @JvmStatic private val PLACEHOLDER_FORMAT = Regex("<(.+?)>")
    @JvmStatic fun applyPlaceholders(
        input: String, vararg elements: Any
    ) = input.replace(PLACEHOLDER_FORMAT) {
        val (slug) = it.destructured
        elements.firstNotNullOfOrNull { obj ->
            val expanders = getValidExpanders(obj)
            expanders.firstNotNullOfOrNull { it.expand(obj, slug) }
        }?.toString() ?: it.value
    }

    fun interface Expander<T : Any> {
        fun expand(forWho: T, slug: String): Any?
    }
}