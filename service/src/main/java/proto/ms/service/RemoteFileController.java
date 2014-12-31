package proto.ms.service;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * Created by civerson on 12/21/14.
 */
@Slf4j
@Controller
@RequestMapping("/remoteFiles")
public class RemoteFileController {

    @Autowired
    RefactorFileRepository repository;

    @Autowired
    GridFsTemplate template;

    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public void handleImport(HttpEntity<byte[]> requestEntity, HttpServletResponse response) throws IOException {

        MediaType mediaType = requestEntity.getHeaders().getContentType();
        String contentDisp = requestEntity.getHeaders().getFirst("Content-Disposition");
        String filename = StringUtils.substringAfter(contentDisp, "=");

        try (InputStream certStream = new ByteArrayInputStream(requestEntity.getBody())) {
            GridFSFile gridFSFile = template.store(certStream, filename, mediaType.getType() + "/" + mediaType.getSubtype());
            gridFSFile.getId().toString();
            RemoteFile remoteFile = new RemoteFile(filename, gridFSFile.getId().toString(), new Date());
            repository.save(remoteFile);
        }

        response.setStatus(HttpStatus.NO_CONTENT.value());
        log.debug("Imported file " + filename);

    }

    @RequestMapping(value = "/export/{id}", method = RequestMethod.GET)
    public void handleExport(@PathVariable("id") String id, HttpServletResponse response) throws IOException {
        RemoteFile remoteFile = repository.findOne(id);
        GridFSDBFile gridFSDBFile = template.findOne(new Query(Criteria.where("_id").is(remoteFile.getFileRef())));

        response.setContentType(gridFSDBFile.getContentType());
        response.setHeader("Content-Disposition", "attachment; filename=" + remoteFile.getName());

        // copy it to response's OutputStream
        IOUtils.copy(gridFSDBFile.getInputStream(), response.getOutputStream());
        response.flushBuffer();

        response.setStatus(HttpStatus.NO_CONTENT.value());
        log.debug("Exported file " + remoteFile.getName());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") String id, HttpServletResponse response) {
        RemoteFile remoteFile = repository.findOne(id);
        template.delete(new Query(Criteria.where("_id").is(remoteFile.getFileRef())));
        log.debug("Deleted file " + remoteFile.getName());
        repository.delete(remoteFile);
    }

}
