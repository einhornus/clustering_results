package com.antra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * General and mid-tier Spring context configuration class
 * @author Lester
 *
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"com.antra.dao","com.antra.service","com.antra.util"}, excludeFilters = { @Filter(type = FilterType.ANNOTATION, value = EnableWebMvc.class) })
@Import({JpaConfig.class, SpringSecurityConfig.class, CachingConfig.class, JmsConfig.class})
@ImportResource({"/WEB-INF/spring/cxf.xml"})
public class RootConfig {
	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
		ClassPathResource persistence=new ClassPathResource("persistence.properties");
		ClassPathResource environment=new ClassPathResource("environment.properties");
		ppc.setLocations(new ClassPathResource[]{persistence, environment});
		return ppc;
	}
	
	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
}

--------------------

package com.tracebucket.crm.test.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Created by vishwa on 26-11-2014.
 */
@Configuration
@ComponentScan(basePackages = {"com.tracebucket.**.rest.controller"})
@EnableWebMvc
public class WebTestConfig extends WebMvcConfigurerAdapter{
    @Autowired
    @Qualifier("commandInterceptor")
    private HandlerInterceptorAdapter commandInterceptor;

    @Bean
    public Mapper mapper() {
        return new DozerBeanMapper();
    }

    @Bean
    public ObjectMapper objectMapper()
    {
        return new ObjectMapper();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(commandInterceptor);
    }

}

--------------------

package com.thoersch.seeds.init;

import com.thoersch.seeds.ApplicationConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class YamlConfigInitialization {
    @Inject
    private Environment springEnvironment;
    private static final Logger LOGGER = LoggerFactory.getLogger(YamlConfigInitialization.class);

    @Bean
    public ApplicationConfiguration getApplicationConfig() throws IOException {
        String environmentName = springEnvironment.getActiveProfiles().length > 0 ? springEnvironment.getActiveProfiles()[0] : "local";
        LOGGER.info("Loading environment: " + environmentName);

        try (InputStream inputStream = YamlConfigInitialization.class.getResourceAsStream("/config/" + environmentName + ".yml")) {
            if (inputStream == null) {
                throw new IOException("Profile not found: " + environmentName);
            }

            return new ApplicationConfigurationParser().parse(environmentName, inputStream);
        }
    }
}

--------------------

