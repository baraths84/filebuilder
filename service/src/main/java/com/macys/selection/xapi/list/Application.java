package com.macys.selection.xapi.list;

import com.macys.selection.xapi.list.mapping.MapperConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * 
 * @author m785440
 *
 */
@SpringBootApplication
@Import({MapperConfig.class})
@ImportResource("classpath:appContext.xml")
public class Application extends SpringBootServletInitializer {

  public static void main(String[] args) throws InterruptedException {
    SpringApplication.run(Application.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(Application.class);
  }

  @Bean
  public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }

}

