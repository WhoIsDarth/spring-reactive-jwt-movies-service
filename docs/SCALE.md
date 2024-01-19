# How to scale?

## Horizontal scaling

The application is stateless, and it uses unblocking-IO stack, so it can be scaled horizontally by
running multiple
instances
of the application behind a
load balancer. The best way to do this is to use a container orchestration tool like Kubernetes.

## Database

- **Cache**: The application uses a relational database, so we can optimize and use caching to
  reduce the load on the
  database for
  such api as `GET /movies/top` which can be unchanged for a long time. We can use a cache like
  Redis to store the
  result.
- **Sharding**: The application uses a relational database, so we can use sharding to scale the
  database. But it is
  crucial to understand it in the beginning of the project. It is not easy to implement sharding in
  the middle of the
  project.
