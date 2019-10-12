package nu.westlin.dependencyinjection

@Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")
class DIContext {
    private val beans: ArrayList<Any> = ArrayList()

    inline fun <reified T> register() {
        // TODO petves: Stop bean from being Kotlin API types (String etc)

        beans.firstOrNull { it::class == T::class }
            ?.let { throw RuntimeException("Bean of type ${T::class} already exist") }
        beans.add(createBean<T>() as Any)
    }

    private inline fun <reified T>  createBean(): T {
        val c = T::class.constructors.first { it.parameters.isEmpty() }
        return c.call()
    }

    inline fun <reified T> get(): T? = beans.firstOrNull { it::class == T::class } as T?
}

class BeansDSL {
    private val beans = ArrayList<Any>()

    fun bean(bean: Any.() -> Unit) {
        bean()
    }
}

fun beans(block: BeansDSL.() -> Unit) = BeansDSL().apply(block)
