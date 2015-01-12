package proto.ms.service.fileService;

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
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.UUID;

/**
 * Created by civerson on 1/5/15.
 */
@Component
public class S3FileService implements FileService {

    @Value("${amazon.s3.bucket}")
    String bucket;

    @Autowired
    AmazonS3Client amazonS3Client;


    @Override
    public String create(InputStream inputStream, String contentType) {
        // todo, is this what I want?
        String id = UUID.randomUUID().toString();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);

        amazonS3Client.putObject(new PutObjectRequest(bucket, id, inputStream, null));
        return id;
    }

    @Override
    public InputStream get(String id) {
        S3Object object = amazonS3Client.getObject(new GetObjectRequest(bucket, id));
        return object.getObjectContent();

    }

    @Override
    public void delete(String id) {
        amazonS3Client.deleteObject(bucket, id);
    }

}
