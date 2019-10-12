package nu.westlin.dependencyinjection

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

internal class DIContextTest {

    private class Foo
    private class Bar

    @Test
    fun `add a bean that already exist - same bean`() {
        val foo = Foo()

        val ctx = DIContext()
        ctx.add(foo)

        assertThatThrownBy { ctx.add(foo) }
            .isInstanceOf(RuntimeException::class.java)
            .hasMessage("Bean ${foo::class.simpleName} already exist")
    }

    @Test
    fun `add a bean that already exist - another bean with same type`() {
        val ctx = DIContext()
        ctx.add(Foo())

        assertThatThrownBy { ctx.add(Foo()) }
            .isInstanceOf(RuntimeException::class.java)
            .hasMessage("Bean ${Foo::class.simpleName} already exist")
    }

    @Test
    fun `get bean by name`() {
        val foo = Foo()

        val ctx = DIContext()
        ctx.add(foo)

        assertThat(ctx.get(foo::class.simpleName!!)).isEqualTo(foo)
    }

    @Test
    fun `get bean by reified type`() {
        val foo = Foo()

        val ctx = DIContext()
        ctx.add(foo)

        assertThat(ctx.get<Foo>()).isEqualTo(foo)
    }

    @Test
    fun `get bean by reified type - not found`() {
        val foo = Foo()

        val ctx = DIContext()
        ctx.add(foo)

        assertThat(ctx.get<Bar>()).isNull()
    }
}