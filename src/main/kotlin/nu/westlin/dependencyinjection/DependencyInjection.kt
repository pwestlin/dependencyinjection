package nu.westlin.dependencyinjection

@Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE", "RemoveCurlyBracesFromTemplate")
class DIContext {
    private val beans: ArrayList<Any> = ArrayList()

    inline fun <reified T> register() {
        // TODO petves: Stop bean from being Kotlin API types (String etc)?

        beans.firstOrNull { it::class == T::class }
            ?.let { throw RuntimeException("Bean of type ${T::class} already exist") }
        beans.add(createBean<T>() as Any)
    }

    inline fun <reified T> get(): T? = beans.firstOrNull { it::class == T::class } as T?

    inline fun <reified T> bean() = register<T>()

    private inline fun <reified T> createBean(): T {
        return T::class.constructors.firstOrNull { it.parameters.isEmpty() }?.call() ?: createBeanWithDependecies()
    }

    private inline fun <reified T> createBeanWithDependecies(): T {
        println("T::class = ${T::class}")
        require(T::class.constructors.size == 1) { "Type ${T::class} has more than one constructor" }
        val ctor = T::class.constructors.first()
        println("ctor = ${ctor}")
        println("ctor.parameters = ${ctor.parameters}")
        val params = ctor.parameters.map { param ->
            println("param = ${param}")
            beans.firstOrNull { it::class.qualifiedName == param.type.toString() }
                ?: throw RuntimeException("Dependency of type ${param.type} is missing in context for ${T::class}")
        }

        return ctor.call(*params.toTypedArray())
    }
}

fun beans(block: DIContext.() -> Unit) = DIContext().apply(block)

