package proto.ms.ui;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by civerson on 1/1/15.
 */
@Slf4j
@Service
public class RemoteFileService {

    @Autowired
    private RemoteFileClient remoteFileClient;
    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "getDefaultFiles")
    public Collection<RemoteFile> getFiles() {
        return this.getPagedFiles().getContent();
    }

    public PagedResources<RemoteFile> getPagedFiles() {
        return remoteFileClient.getRemoteFiles();
    }

    // todo, figure out a way to return a PagedResource
    public Collection<RemoteFile> getDefaultFiles() {
        log.warn("Circuit breaker tripped for getting files");
        return new ArrayList<RemoteFile>();
    }

    public void delete(String id) {
        remoteFileClient.delete(id);
    }

    public void downloadFile(String id, HttpServletResponse response) {

        long start = System.currentTimeMillis();
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<byte[]> responseEntity = restTemplate.exchange("http://remotefilesvc/remoteFiles/export/{id}" +
                "", HttpMethod.GET, new HttpEntity<byte[]>(headers), byte[].class, id);

        response.setContentType(responseEntity.getHeaders().getContentType().toString());
        response.setHeader("Content-Disposition", responseEntity.getHeaders().getFirst("Content-Disposition"));
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(responseEntity.getBody())) {
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            log.info("Error writing file to output stream. id was '{}'", id, ex);
            throw new RuntimeException("IOError writing file to output stream");
        }
        log.info("download took " + (System.currentTimeMillis() - start));
    }

    public void uploadFile(MultipartFile file) throws IOException {

        String name = file.getOriginalFilename();
        String contentType = file.getContentType();
        byte[] bytes = file.getBytes();
        HttpHeaders clientHeaders = new HttpHeaders();
        clientHeaders.setContentType(MediaType.parseMediaType(contentType));
        clientHeaders.set("Content-Disposition", "attachment; filename=" + name);

        HttpEntity<byte[]> entity = new HttpEntity<>(bytes, clientHeaders);
        ResponseEntity<String> sentData = restTemplate.exchange("http://remotefilesvc/remoteFiles/import", HttpMethod.POST, entity, String.class);

    }

}
