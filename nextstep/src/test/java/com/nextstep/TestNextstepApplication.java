package com.nextstep;

import org.springframework.boot.SpringApplication;

public class TestNextstepApplication {

	public static void main(String[] args) {
		SpringApplication.from(NextstepApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
