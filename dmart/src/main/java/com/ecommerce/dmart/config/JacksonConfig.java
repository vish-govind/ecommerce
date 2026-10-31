
package com.ecommerce.dmart.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class JacksonConfig implements WebMvcConfigurer {

    @Override
    public void extendMessageConverters(
            List<HttpMessageConverter<?>> converters) {

        for (HttpMessageConverter<?> converter : converters) {

            if (converter instanceof MappingJackson2HttpMessageConverter jacksonConverter) {

                jacksonConverter.getObjectMapper()
                        .registerModule(new JavaTimeModule());

                jacksonConverter.getObjectMapper()
                        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            }
        }
    }
}
