package proto.ms.ui;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by civerson on 12/22/14.
 */
@EnableWebMvc
@Configuration
public class ClientWebConfig extends WebMvcConfigurerAdapter {

//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        super.addViewControllers(registry);
//
//        registry.addViewController("/upload.html");
//    }

//    @Bean
//    MultipartConfigElement multipartConfigElement() {
//        MultiPartConfigFactory factory = new MultiPartConfigFactory();
//        factory.setMaxFileSize("512KB");
//        factory.setMaxRequestSize("512KB");
//        return factory.createMultipartConfig();
//    }
}
