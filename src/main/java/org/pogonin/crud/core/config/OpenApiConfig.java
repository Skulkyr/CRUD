package org.pogonin.crud.core.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Тестирование свагера",
                description = "Тестирование свагера", version = "1.0.0",
                contact = @Contact(
                        name = "Погонин Алексей",
                        email = "skulkyr20@gmail.com"
                )
        )
)
public class OpenApiConfig {
}
