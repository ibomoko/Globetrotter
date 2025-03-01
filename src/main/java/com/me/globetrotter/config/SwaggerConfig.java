package com.me.globetrotter.config;

import com.me.globetrotter.constants.SecurityConstants;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    private final SecurityConstants securityConstants;

    @Bean
    public OpenAPI customOpenAPI() {
        final String bearerSchemeName = "bearerAuth";
        final String basicSchemeName = "basicAuth";
        return new OpenAPI()
                .servers(getServers())
                .components(
                        new Components()
                                .addSecuritySchemes(bearerSchemeName,
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer").bearerFormat("jti"))
                                .addSecuritySchemes(basicSchemeName,
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("basic"))
                )
                .security(List.of(new SecurityRequirement().addList(bearerSchemeName).addList(basicSchemeName)))
                .info(new Info().title("Globetrotter").version("V1"));
    }

    public List<Server> getServers() {
        List<Server> servers = new ArrayList<>();
        servers.add(new Server().url(securityConstants.getServer()));
        return servers;
    }
}
