package proto.ms.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.util.UUID;

/**
 * Created by civerson on 1/5/15.
 */
public class S3FileService {

    @Value("${aws.s3.bucket}")
    String bucket;

    @Autowired
    AmazonS3Client amazonS3Client;

    public String create(InputStream inputStream, String contentType) {
        // todo, is this what I want?
        String id = UUID.randomUUID().toString();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);

        amazonS3Client.putObject(new PutObjectRequest(bucket, id, inputStream, null));
        return id;
    }
    public InputStream get() {
        amazonS3Client.getObject(bucket, "key");
        S3Object object = amazonS3Client.getObject(new GetObjectRequest(bucket, "key"));
        System.out.println("Content-Type: "  + object.getObjectMetadata().getContentType());
        return object.getObjectContent();

    }
    public void delete() {
        amazonS3Client.deleteObject(bucket, "key");
    }

    @Configuration
    static class AppConfig {
        @Value("${aws.iam.accessKey}")
        String accessKey;
        @Value("${aws.iam.secretKey}")
        String secretKey;

        @Bean
        AWSCredentials awsCredentials() {
            return new BasicAWSCredentials(accessKey, secretKey);
        }

        @Bean
        AmazonS3Client amazonS3Client() {
            AmazonS3Client s3Client = new AmazonS3Client(awsCredentials());
            Region usWest2 = Region.getRegion(Regions.US_WEST_2);
            s3Client.setRegion(usWest2);
            return s3Client;
        }
    }
}
