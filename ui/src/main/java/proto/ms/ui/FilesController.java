package proto.ms.ui;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by civerson on 12/26/14.
 */
@Slf4j
@Controller
@RequestMapping({"/files", "/"})
public class FilesController {

    @Autowired
    private RemoteFileClient remoteFileClient;
    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping
    public String getFiles(ModelMap model) {

        long start = System.currentTimeMillis();
        PagedResources<RemoteFile> remoteFileList = remoteFileClient.getRemoteFiles();
        model.addAttribute("remoteFiles", remoteFileList.getContent());

        log.info("get files took " + (System.currentTimeMillis() - start));
        return "files";
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable String id) {

        remoteFileClient.delete(id);
        return "redirect:/files";
    }

    @RequestMapping("/download/{id}")
    public void downloadFile(@PathVariable String id, HttpServletResponse response, ModelMap model) {

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

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String uploadFile(@RequestParam("theFile") MultipartFile file) {

        long start = System.currentTimeMillis();
        if (!file.isEmpty()) {
            String name = file.getOriginalFilename();
            String contentType = file.getContentType();
            try {
                byte[] bytes = file.getBytes();
                HttpHeaders clientHeaders = new HttpHeaders();
                clientHeaders.setContentType(MediaType.parseMediaType(contentType));
                clientHeaders.set("Content-Disposition", "attachment; filename=" + name);

                HttpEntity<byte[]> entity = new HttpEntity<>(bytes, clientHeaders);
                ResponseEntity<String> sentData = restTemplate.exchange("http://remotefilesvc/remoteFiles/import", HttpMethod.POST, entity, String.class);

                log.info("upload took " + (System.currentTimeMillis() - start));

                return "redirect:/files";
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "The selected file was empty and could not be uploaded.";
        }
    }


}
