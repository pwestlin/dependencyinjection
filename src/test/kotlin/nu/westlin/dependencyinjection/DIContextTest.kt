package nu.westlin.dependencyinjection

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DIContextTest {

    private lateinit var ctx: DIContext

    class Foo
    class Bar
    @Suppress("unused")
    class Foobar(private val foo: Foo)

    @BeforeEach
    private fun init() {
        ctx = DIContext()
    }

    @Test
    fun `register a bean with dependency that exist`() {
        ctx.register<Foo>()
        ctx.register<Foobar>()

        assertThat(ctx.get<Foobar>()).isInstanceOf(Foobar::class.java)
    }

    @Test
    fun `register a bean with dependency that does not exist`() {
        assertThatThrownBy {
            ctx.register<Foobar>()
        }
            .isInstanceOf(RuntimeException::class.java)
            .hasMessage("Dependency of type ${Foo::class.qualifiedName} is missing in context for ${Foobar::class}")
    }

    @Test
    fun `register a bean that already exist`() {
        ctx.register<Foo>()

        assertThatThrownBy { ctx.register<Foo>() }
            .isInstanceOf(RuntimeException::class.java)
            .hasMessage("Bean of type ${Foo::class} already exist")
    }

    @Test
    fun `get bean by reified type`() {
        ctx.register<Foo>()

        assertThat(ctx.get<Foo>()).isInstanceOf(Foo::class.java)
    }

    @Test
    fun `get bean by reified type - not found`() {
        assertThat(ctx.get<Bar>()).isNull()
    }

    @Test
    fun `create beans with DSL`() {
        beans {
            bean<Foo>()
            bean<Bar>()
        }.let { beans ->
            assertThat(beans.get<Foo>()).isInstanceOf(Foo::class.java)
            assertThat(beans.get<Bar>()).isInstanceOf(Bar::class.java)
            assertThat(beans.get<Foobar>()).isNull()
        }
    }

    @Test
    fun `DSL - create a bean that has a dependency to another bean`() {
        beans {
            bean<Foo>()
            bean<Foobar>()
        }
    }

    @Test
    fun `DSL - create a bean that has a dependency to another bean that is missing`() {
        assertThatThrownBy {
            beans {
                bean<Foobar>()
            }
        }
            .isInstanceOf(RuntimeException::class.java)
            .hasMessage("Dependency of type ${Foo::class.qualifiedName} is missing in context for ${Foobar::class}")
    }
}

