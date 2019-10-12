package nu.westlin.dependencyinjection

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class DIContextTest {

    class Foo
    class Bar

    private lateinit var ctx: DIContext

    @BeforeEach
    private fun init() {
        ctx = DIContext()
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
}