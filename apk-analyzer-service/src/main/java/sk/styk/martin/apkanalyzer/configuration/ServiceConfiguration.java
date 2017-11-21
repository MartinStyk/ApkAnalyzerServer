package sk.styk.martin.apkanalyzer.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import sk.styk.martin.apkanalyzer.PersistenceApplicationContext;

/**
 * @author Martin Styk
 * @version 21.11.2017
 */
@Configuration
@EnableAspectJAutoProxy
@Import(PersistenceApplicationContext.class)
@ComponentScan(basePackages = {"sk.styk.martin.apkanalyzer.facade", "sk.styk.martin.apkanalyzer.service",
        "sk.styk.martin.apkanalyzer.mapping"})
public class ServiceConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
