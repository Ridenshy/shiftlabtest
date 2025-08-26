package ru.Tim.ShiftCRM;


import org.springframework.boot.SpringApplication;
import ru.Tim.ShiftCRM.config.TestcontainersConfiguration;

class ShiftCrmApplicationTests {

	public static void main(String[] args) {
		SpringApplication.from(ShiftCrmApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
