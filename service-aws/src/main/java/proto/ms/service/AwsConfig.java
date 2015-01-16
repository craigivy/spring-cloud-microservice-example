package proto.ms.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.s3.AmazonS3Client;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.socialsignin.spring.data.dynamodb.core.DynamoDBOperations;
import org.socialsignin.spring.data.dynamodb.core.DynamoDBTemplate;
import org.socialsignin.spring.data.dynamodb.mapping.event.ValidatingDynamoDBEventListener;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Created by civerson on 1/10/15.
 */
@Slf4j
@Configuration
@EnableDynamoDBRepositories(basePackages = "proto.ms.service",dynamoDBOperationsRef="dynamoDBOperations")
public class AwsConfig {

    @Value("${aws.dynamodb.endpoint}")
    private String amazonDynamoDBEndpoint;

    @Value("${aws.access.key.id}")
    private String amazonAWSAccessKey;

    @Value("${aws.secret.key}")
    private String amazonAWSSecretKey;

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        AmazonDynamoDB amazonDynamoDB = new AmazonDynamoDBClient(
                awsCredentials());
        if (StringUtils.isNotEmpty(amazonDynamoDBEndpoint)) {
            amazonDynamoDB.setEndpoint(amazonDynamoDBEndpoint);
        }
        return amazonDynamoDB;
    }


    @Bean
    public DynamoDBOperations dynamoDBOperations()
    {
        return new DynamoDBTemplate(amazonDynamoDB());
    }

    @Bean
    public AWSCredentials awsCredentials()
    {
        log.debug("awsAccessKey is = " + amazonAWSAccessKey);
        return new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
    }

    @Bean
    AmazonS3Client amazonS3Client() {
        AmazonS3Client s3Client = new AmazonS3Client(awsCredentials());
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        s3Client.setRegion(usWest2);
        return s3Client;
    }


    /** The following validation-related beans are optional - only
     * required if JSR 303 validation is desired.  For validation to
     * work, the @EnableDynamoDBRepositories must be configured with
     * a reference to DynamoDBOperations bean, rather than with
     * reference to AmazonDynamoDB client
     * */

    @Bean
    public LocalValidatorFactoryBean validator()
    {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public ValidatingDynamoDBEventListener validatingDynamoDBEventListener()
    {
        return new ValidatingDynamoDBEventListener(validator());
    }
}
