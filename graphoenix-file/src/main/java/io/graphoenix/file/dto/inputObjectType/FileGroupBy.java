package io.graphoenix.file.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import jakarta.annotation.Generated;
import java.lang.String;
import java.util.Collection;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Group Input for 文件
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder")
@Description("Group Input for 文件")
public class FileGroupBy {
  /**
   * Group By Field Names
   */
  @Description("Group By Field Names")
  private Collection<String> fieldNames;

  /**
   * OrderByes
   */
  @Description("OrderByes")
  private Collection<FileGroupBy> gbs;

  public Collection<String> getFieldNames() {
    return this.fieldNames;
  }

  public void setFieldNames(Collection<String> fieldNames) {
    this.fieldNames = fieldNames;
  }

  public Collection<FileGroupBy> getGbs() {
    return this.gbs;
  }

  public void setGbs(Collection<FileGroupBy> gbs) {
    this.gbs = gbs;
  }
}
