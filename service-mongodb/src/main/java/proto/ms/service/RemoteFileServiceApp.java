package proto.ms.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.FeignClientScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

/**
 * Created by civerson on 12/21/14.
 */
@EnableAutoConfiguration
@ComponentScan
@EnableDiscoveryClient
@FeignClientScan
@Import(RepositoryRestMvcConfiguration.class)
public class RemoteFileServiceApp extends RepositoryRestMvcConfiguration {

    @Override
    protected void configureRepositoryRestConfiguration( RepositoryRestConfiguration config) {
        config.exposeIdsFor(RemoteFile.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(RemoteFileServiceApp.class, args);
    }

}
