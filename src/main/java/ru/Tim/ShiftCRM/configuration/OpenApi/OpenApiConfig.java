package ru.Tim.ShiftCRM.configuration.OpenApi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Evgeny",
                        email = "evgentimochkin@gmail.com"
                ),
                description = "OpenApi documentation for ShiftLab test project",
                title = "ShiftCRM API",
                version = "1.0"
        )
)
public class OpenApiConfig {

}

