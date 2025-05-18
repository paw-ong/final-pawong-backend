package kr.co.pawong.pwbe.global.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setSupportedMediaTypes(Arrays.asList(
                MediaType.APPLICATION_JSON,
                MediaType.valueOf("text/json;charset=UTF-8"),
                MediaType.valueOf("application/json;charset=UTF-8")

        ));

        MappingJackson2HttpMessageConverter xmlConverter = new MappingJackson2HttpMessageConverter();
        xmlConverter.setSupportedMediaTypes(Arrays.asList(
                MediaType.APPLICATION_XML,
                MediaType.TEXT_XML,
                MediaType.valueOf("text/xml;charset=UTF-8"),
                MediaType.valueOf("application/xml;charset=UTF-8")
        ));

        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(jsonConverter);
        converters.add(xmlConverter);

        restTemplate.setMessageConverters(converters);
        return restTemplate;
    }
}

