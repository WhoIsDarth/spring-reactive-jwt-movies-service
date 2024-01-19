# TODOs

## API

- As was mentioned in the code /movies/top endpoint should be paginated. It's not implemented yet.
  Use continuations for more optimal search
- Complete CRUD for all domains.
- Add more validations for based on business necessities

## Monitoring

- Add metrics for all endpoints.
- Add metrics for all domains.
- Tracing (can be implemented on the Service Mesh side).
- Logging.

## Security

- API encryption.

## Data structure

- Add `updated_at`, `created_at` columns for tables were necessary
- Add more details for `movies` table if business requires it.

## Testing

- Unit tests for DTOs, exceptions, and other small pieces of code.
- Integration tests for all endpoints.
- E2E tests for all endpoints.
- Contract tests for all endpoints.

## Migration

- Liquidbase should be disabled for production environment. Dry run job should be used instead.