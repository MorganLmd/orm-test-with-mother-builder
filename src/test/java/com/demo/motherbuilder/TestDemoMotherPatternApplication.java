package com.demo.motherbuilder;

import org.springframework.boot.SpringApplication;

public class TestDemoMotherPatternApplication {

    static void main(String[] args) {
        SpringApplication.from(DemoMotherBuilderApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
