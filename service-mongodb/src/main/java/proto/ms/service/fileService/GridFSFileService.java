package proto.ms.service.fileService;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * Created by civerson on 1/9/15.
 */
@Component
public class GridFSFileService implements FileService {

    @Autowired
    GridFsTemplate template;

    @Override
    public String create(InputStream inputStream, String contentType) {
        GridFSFile gridFSFile = template.store(inputStream, null, contentType);
        return gridFSFile.getId().toString();
    }

    @Override
    public InputStream get(String id) {
        GridFSDBFile gridFSDBFile = template.findOne(new Query(Criteria.where("_id").is(id)));
        return gridFSDBFile.getInputStream();
    }

    @Override
    public void delete(String id) {
        template.delete(new Query(Criteria.where("_id").is(id)));
    }
}
