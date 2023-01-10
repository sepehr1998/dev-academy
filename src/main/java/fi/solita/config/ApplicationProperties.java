package fi.solita.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.servlet.MultipartConfigElement;

/**
 * Properties specific to Solita.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    //TODO add values to the properties later

    @Bean
    public MultipartConfigElement multipartConfigElement(){
        return new MultipartConfigElement("C:\\temp\\" ,
            1073741824L,1073741824L,65535);
    }
}
