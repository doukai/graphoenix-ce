package io.graphoenix.introspection.dto.inputObjectType;

import com.dslplatform.json.CompiledJson;
import jakarta.annotation.Generated;
import java.lang.String;
import java.util.Collection;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Input;

/**
 * Group Input for __Schema
 */
@CompiledJson
@Input
@Generated("io.graphoenix.java.builder.TypeSpecBuilder")
@Description("Group Input for __Schema")
public class __SchemaGroupBy {
  /**
   * Group By Field Names
   */
  @Description("Group By Field Names")
  private Collection<String> by;

  /**
   * types
   */
  @Description("types")
  private __TypeGroupBy types;

  /**
   * queryType
   */
  @Description("queryType")
  private __TypeGroupBy queryType;

  /**
   * mutationType
   */
  @Description("mutationType")
  private __TypeGroupBy mutationType;

  /**
   * subscriptionType
   */
  @Description("subscriptionType")
  private __TypeGroupBy subscriptionType;

  /**
   * directives
   */
  @Description("directives")
  private __DirectiveGroupBy directives;

  /**
   * OrderByes
   */
  @Description("OrderByes")
  private Collection<__SchemaGroupBy> gbs;

  public Collection<String> getBy() {
    return this.by;
  }

  public void setBy(Collection<String> by) {
    this.by = by;
  }

  public __TypeGroupBy getTypes() {
    return this.types;
  }

  public void setTypes(__TypeGroupBy types) {
    this.types = types;
  }

  public __TypeGroupBy getQueryType() {
    return this.queryType;
  }

  public void setQueryType(__TypeGroupBy queryType) {
    this.queryType = queryType;
  }

  public __TypeGroupBy getMutationType() {
    return this.mutationType;
  }

  public void setMutationType(__TypeGroupBy mutationType) {
    this.mutationType = mutationType;
  }

  public __TypeGroupBy getSubscriptionType() {
    return this.subscriptionType;
  }

  public void setSubscriptionType(__TypeGroupBy subscriptionType) {
    this.subscriptionType = subscriptionType;
  }

  public __DirectiveGroupBy getDirectives() {
    return this.directives;
  }

  public void setDirectives(__DirectiveGroupBy directives) {
    this.directives = directives;
  }

  public Collection<__SchemaGroupBy> getGbs() {
    return this.gbs;
  }

  public void setGbs(Collection<__SchemaGroupBy> gbs) {
    this.gbs = gbs;
  }
}
