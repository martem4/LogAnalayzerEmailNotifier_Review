package com.century.logssender.configuration;

import com.century.logssender.template.YamlTemplateParser;
import com.century.logssender.template.contract.TemplateParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationContextConfiguration {

    @Bean
    public TemplateParser templateParser() {
        return new YamlTemplateParser();
    }

}
