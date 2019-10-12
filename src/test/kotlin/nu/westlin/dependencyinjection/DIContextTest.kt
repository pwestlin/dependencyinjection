package nu.westlin.dependencyinjection

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

internal class DIContextTest {

    private class Foo
    private class Bar

    @Test
    fun `register a bean that already exist - same bean`() {
        val foo = Foo()

        val ctx = DIContext()
        ctx.register(foo)

        assertThatThrownBy { ctx.register(foo) }
            .isInstanceOf(RuntimeException::class.java)
            .hasMessage("Bean of type ${foo::class} already exist")
    }

    @Test
    fun `register a bean that already exist - another bean with same type`() {
        val ctx = DIContext()
        ctx.register(Foo())

        assertThatThrownBy { ctx.register(Foo()) }
            .isInstanceOf(RuntimeException::class.java)
            .hasMessage("Bean of type ${Foo::class} already exist")
    }

    @Test
    fun `get bean by reified type`() {
        val foo = Foo()

        val ctx = DIContext()
        ctx.register(foo)

        assertThat(ctx.get<Foo>()).isEqualTo(foo)
    }

    @Test
    fun `get bean by reified type - not found`() {
        val foo = Foo()

        val ctx = DIContext()
        ctx.register(foo)

        assertThat(ctx.get<Bar>()).isNull()
    }
}