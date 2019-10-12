package nu.westlin.dependencyinjection

import org.junit.jupiter.api.Test
import java.time.Instant


private val advancedTestContext = beans {
    bean<Logger>()
    bean<Repository>()
    bean<Service>()
}

class Logger {
    fun log(message: String) {
        println("${Instant.now()} - $message")
    }
}

class Repository {
    fun doStuff() {
        advancedTestContext.get<Logger>()?.log("Doing stuff in repository")
    }
}

class Service(private val repository: Repository) {
    fun doStuff() {
        advancedTestContext.get<Logger>()?.log("Doing stuff in service")
        repository.doStuff()
    }
}

class AdvancedTest {

    @Test
    fun `do stuff`() {
        advancedTestContext.get<Service>()?.doStuff()
    }
}