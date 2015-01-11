package proto.ms.service.fileService;

import java.io.InputStream;

/**
 * Created by civerson on 1/9/15.
 */
public interface FileService {

    public String create(InputStream inputStream, String contentType);

    public InputStream get(String id);

    public void delete(String id);

}
