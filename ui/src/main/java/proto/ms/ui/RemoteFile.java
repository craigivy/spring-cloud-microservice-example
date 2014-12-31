package proto.ms.ui;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * Created by civerson on 12/26/14.
 */
@Data
public class RemoteFile {

    private final @Id String id = null;
    private final String name = null;
    private final Date createDate = null;

}
