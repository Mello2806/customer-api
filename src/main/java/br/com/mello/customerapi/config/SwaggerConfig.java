package br.com.mello.customerapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Título e descrição que aparecem no topo do Swagger.
 * Os endpoints em si o springdoc descobre sozinho lendo os @GetMapping, @PostMapping...
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI documentacao() {
        return new OpenAPI().info(new Info()
                .title("Customer API")
                .version("1.0.0")
                .description("API REST de cadastro de clientes — projeto de estudo de ADS."));
    }
}
