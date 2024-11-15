package io.graphoenix.file.dto.objectType;

import com.dslplatform.json.CompiledJson;
import jakarta.annotation.Generated;
import java.lang.String;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Type;

/**
 * Edge Object for 文件
 */
@Type
@CompiledJson
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Edge Object for 文件")
public class FileEdge {
  /**
   * Node
   */
  @Description("Node")
  private File node;

  /**
   * Cursor
   */
  @Description("Cursor")
  private String cursor;

  public File getNode() {
    return this.node;
  }

  public void setNode(File node) {
    this.node = node;
  }

  public String getCursor() {
    return this.cursor;
  }

  public void setCursor(String cursor) {
    this.cursor = cursor;
  }
}
