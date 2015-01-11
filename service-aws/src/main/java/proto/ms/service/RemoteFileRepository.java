package proto.ms.service;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.socialsignin.spring.data.dynamodb.repository.EnableScanCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by civerson on 12/21/14.
 */
public interface RemoteFileRepository extends PagingAndSortingRepository<RemoteFile, String> {

    @EnableScan
    @EnableScanCount
    public Page<RemoteFile> findAll(Pageable pageable);

}
