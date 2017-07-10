package gov.nih.nci.evs.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class Application extends SpringBootServletInitializer {
//public class Application {
	 @Override
	    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	        return application.sources(Application.class);
	    }

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


	@Bean
        public Docket api() { 
               return new Docket(DocumentationType.SWAGGER_2)  
                 .select()                                  
                 .apis(RequestHandlerSelectors.any())              
                 .paths(PathSelectors.ant("/api/v1/ctrp/concept/**"))                          
                 .build();                                           
        }

	
}
