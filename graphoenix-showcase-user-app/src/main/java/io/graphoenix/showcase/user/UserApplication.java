package io.graphoenix.showcase.user;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.before.ConnectionSplitter;
import io.graphoenix.spi.annotation.Application;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.sql.translator.MutationTranslator;
import io.graphoenix.sql.translator.QueryTranslator;
import io.graphoenix.sql.translator.TypeTranslator;
import io.nozdormu.spi.context.BeanContext;

import static io.graphoenix.spi.utils.DocumentUtil.graphqlToOperation;

@Application
public class UserApplication {

    public static void main(String[] args) {
        try {
            DocumentManager documentManager = BeanContext.get(DocumentManager.class);
//            QueryTranslator queryTranslator = BeanContext.get(QueryTranslator.class);
//            ConnectionSplitter connectionSplitter = BeanContext.get(ConnectionSplitter.class);
            documentManager.getDocument().addDefinitions(UserApplication.class.getClassLoader().getResourceAsStream("META-INF/graphql/main.gql"));
//            GraphqlParser.OperationDefinitionContext operationDefinitionContext = graphqlToOperation("{ userConnection(name: {val: \"test\"} ) { totalCount edges{ node{ mobileNumbers(sort: DESC) rolesConnection(id: {arr: [\"1\",\"2\",\"3\"]}) {totalCount edges{ node{name } } } } } } }");
//            Operation operation = new Operation(operationDefinitionContext);
//            connectionSplitter.handle(operation, null)
//                    .doOnNext(operation1 -> System.out.println(queryTranslator.operationToSelectSQL(operation1)))
//                    .subscribe();


//            MutationTranslator mutationTranslator = BeanContext.get(MutationTranslator.class);
//            GraphqlParser.OperationDefinitionContext operationDefinitionContext2 = graphqlToOperation("mutation{ user(name: \"test\" userProfile:{email: \"aaa@bbb.com\"} roles:[{name : \"role1\"},{name : \"role2\"}] mobileNumbers:[ \"123\", \"456\", \"789\"] ) { id } }");
//            Operation operation2 = new Operation(operationDefinitionContext2);
//            mutationTranslator.operationToStatementSQL(operation2);
//
//            System.out.println(mutationTranslator.operationToStatementSQL(operation2));

            TypeTranslator typeTranslator = BeanContext.get(TypeTranslator.class);
            System.out.println(typeTranslator.createTablesSQL());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
