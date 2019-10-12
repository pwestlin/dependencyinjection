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

    inline fun <reified T> get(): T? = beans.firstOrNull { it::class == T::class } as T?

    inline fun <reified T> bean() {
        beans.add(createBean<T>() as Any)
    }
}

private inline fun <reified T>  createBean(): T {
    val c = T::class.constructors.first { it.parameters.isEmpty() }
    return c.call()
}

fun beans(block: DIContext.() -> Unit) = DIContext().apply(block)
