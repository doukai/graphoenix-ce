import { Voyager, voyagerIntrospectionQuery } from 'graphql-voyager';
import 'graphql-voyager/dist/voyager.css'

const response = await fetch(
    '/graphql',
    {
        method: 'post',
        headers: {
            Accept: 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ query: voyagerIntrospectionQuery }),
    },
);

const introspection = await response.json();

const VoyagerPage = () => <Voyager
    introspection={introspection}
    displayOptions={{ skipRelay: false, showLeafFields: true }}
/>;

export default VoyagerPage;