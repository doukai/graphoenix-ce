package io.graphoenix.file.dto.objectType;

import com.dslplatform.json.CompiledJson;
import jakarta.annotation.Generated;
import java.util.Collection;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Type;

/**
 * Query
 */
@Type
@CompiledJson
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Query")
public class Query {
  /**
   * Query Field for 文件
   */
  @Description("Query Field for 文件")
  private File file;

  /**
   * Query Field for 文件 List
   */
  @Description("Query Field for 文件 List")
  private Collection<File> fileList;

  /**
   * Query Field for 文件 Connection
   */
  @Description("Query Field for 文件 Connection")
  private FileConnection fileConnection;

  public File getFile() {
    return this.file;
  }

  public void setFile(File file) {
    this.file = file;
  }

  public Collection<File> getFileList() {
    return this.fileList;
  }

  public void setFileList(Collection<File> fileList) {
    this.fileList = fileList;
  }

  public FileConnection getFileConnection() {
    return this.fileConnection;
  }

  public void setFileConnection(FileConnection fileConnection) {
    this.fileConnection = fileConnection;
  }
}
