package io.graphoenix.introspection.dto.objectType;

import com.dslplatform.json.CompiledJson;
import io.graphoenix.core.dto.objectType.PageInfo;
import jakarta.annotation.Generated;
import java.lang.Integer;
import java.util.Collection;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Type;

/**
 * Connection Object for Relationship Object between __Directive and locations
 */
@Type
@CompiledJson
@Generated("io.graphoenix.java.builder.TypeSpecBuilder_Proxy")
@Description("Connection Object for Relationship Object between __Directive and locations")
public class __DirectiveLocationsRelationConnection {
  /**
   * Total
   */
  @Description("Total")
  private Integer totalCount;

  /**
   * Page Info
   */
  @Description("Page Info")
  private PageInfo pageInfo;

  /**
   * Edges
   */
  @Description("Edges")
  private Collection<__DirectiveLocationsRelationEdge> edges;

  public Integer getTotalCount() {
    return this.totalCount;
  }

  public void setTotalCount(Integer totalCount) {
    this.totalCount = totalCount;
  }

  public PageInfo getPageInfo() {
    return this.pageInfo;
  }

  public void setPageInfo(PageInfo pageInfo) {
    this.pageInfo = pageInfo;
  }

  public Collection<__DirectiveLocationsRelationEdge> getEdges() {
    return this.edges;
  }

  public void setEdges(Collection<__DirectiveLocationsRelationEdge> edges) {
    this.edges = edges;
  }
}
