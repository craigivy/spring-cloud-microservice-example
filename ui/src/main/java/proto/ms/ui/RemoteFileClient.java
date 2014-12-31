package proto.ms.ui;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.hateoas.PagedResources;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Created by civerson on 12/26/14.
 */
@FeignClient("remotefilesvc")
public interface RemoteFileClient {

    @RequestMapping(method = RequestMethod.GET, value = "/remoteFiles")
    public PagedResources<RemoteFile> getRemoteFiles();

    @RequestMapping(method = RequestMethod.DELETE, value = "/remoteFiles/{id}")
    public void delete(@PathVariable("id") String id);


}
