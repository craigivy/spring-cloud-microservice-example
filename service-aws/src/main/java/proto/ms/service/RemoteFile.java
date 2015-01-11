package proto.ms.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.UUID;

/**
 * Created by civerson on 12/21/14.
 */
@DynamoDBTable(tableName = "remotefile")
public class RemoteFile {

    @DynamoDBHashKey(attributeName = "id")
    @Id
    private String id;
    private String name;
    private Date createDate;
    private String fileRef;
    private String contentType;


    public RemoteFile(String name, String fileRef, String contentType, Date createDate) {

        // todo, is this what I want?
        this.id = UUID.randomUUID().toString();;

        this.name = name;
        this.createDate = createDate;
        this.fileRef = fileRef;
        this.contentType = contentType;
    }

    public RemoteFile() {

        this.id = null;
        this.name = null;
        this.fileRef = null;
        this.contentType = null;
        this.createDate = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getFileRef() {
        return fileRef;
    }

    public void setFileRef(String fileRef) {
        this.fileRef = fileRef;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
