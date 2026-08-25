package numberlink.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI numberlinkOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("NumberLink API")
                        .version("0.0.1")
                        .description("""
                                Session cookies, not JWT. POST /api/login (or /api/login/2fa), \
                                then call authenticated routes from the same browser origin."""));
    }
}
