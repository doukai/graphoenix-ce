import { GraphiQL } from 'graphiql';
import type { Fetcher } from '@graphiql/toolkit';
import { ToolbarButton, MagnifyingGlassIcon } from '@graphiql/react';
import { useNavigate } from 'react-router-dom';
import 'graphiql/graphiql.min.css';

const fetcher: Fetcher = async (graphQLParams, options) => {
  const data = await fetch(
    '/graphql',
    {
      method: 'POST',
      headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json',
        ...options?.headers
      },
      body: JSON.stringify(graphQLParams)
    },
  );
  return data.json().catch(() => data.text());
};

const GraphiQLPage = () => {
	const navigate = useNavigate();
    return (
        <GraphiQL
            fetcher={fetcher}
            toolbar={{
                additionalContent: (
                    <ToolbarButton
                        onClick={() => navigate('/voyager')}
                        label="GraphQL Voyager"
                        title="voyager"
                    >
                        <MagnifyingGlassIcon className="graphiql-toolbar-icon" aria-hidden="true" />
                    </ToolbarButton>
                )
            }}
        />
    );
};

export default GraphiQLPage;