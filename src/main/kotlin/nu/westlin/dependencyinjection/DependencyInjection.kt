package nu.westlin.dependencyinjection

import kotlin.reflect.KClass

@Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")
class DIContext {
    private val beans: HashMap<String, Any> = HashMap()

    fun add(bean: Any) {
        // TODO petves: Stop bean from being Kotlin API types (String etc)
        // TODO petves: Better name
        add(bean::class.simpleName!!, bean)
    }

    fun add(name: String, bean: Any) {
        beans.keys.firstOrNull { it == name }?.let { throw RuntimeException("Bean $name already exist")}
        beans[name] = bean
    }

    fun get(name: String): Any? = beans[name]

    inline fun <reified T> get(): Any? = beans.values.firstOrNull { it::class == T::class }
}