package io.graphoenix.showcase.user;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.spi.annotation.Application;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.sql.translator.QueryTranslator;
import io.nozdormu.spi.context.BeanContext;

import java.io.IOException;

import static io.graphoenix.spi.utils.DocumentUtil.graphqlToOperation;

@Application
public class UserApplication {

    public static void main(String[] args) {
        try {
            DocumentManager documentManager = BeanContext.get(DocumentManager.class);
            QueryTranslator queryTranslator = BeanContext.get(QueryTranslator.class);
            documentManager.getDocument().addDefinitions(UserApplication.class.getClassLoader().getResourceAsStream("META-INF/graphql/main.gql"));
            GraphqlParser.OperationDefinitionContext operationDefinitionContext = graphqlToOperation("{ user(name: {val: \"test\"} ) {mobileNumbers(sort: DESC) userProfile(id: {arr: [\"1\",\"2\",\"3\"]}) {  email } } }");
            Operation operation = new Operation(operationDefinitionContext);
            System.out.println(queryTranslator.operationToSelectSQL(operation));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
