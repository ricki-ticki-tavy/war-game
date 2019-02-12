package tests.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"core", "api", "tests"})
public class TestContextConfiguration {
}
