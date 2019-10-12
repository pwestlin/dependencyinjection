package nu.westlin.dependencyinjection

@Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")
class DIContext {
    private val beans: ArrayList<Any> = ArrayList()

    fun register(bean: Any) {
        // TODO petves: Stop bean from being Kotlin API types (String etc)

        beans.firstOrNull { it::class == bean::class }?.let { throw RuntimeException("Bean of type ${bean::class} already exist") }
        beans.add(bean)
    }

    inline fun <reified T> get(): Any? = beans.firstOrNull { it::class == T::class }
}