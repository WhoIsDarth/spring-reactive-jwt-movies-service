## Assumptions

### Assumption 1

While I was doing task I assumed that we should use our own API key, so it should be set as
environment variable.
If it is necessary to use user API key, we can easily store them in encrypted format in other
service and retrieve.

### Assumption 2

The OMBD API is unclear about the response format. So right now I am fining it's tricky to parse and
save to our database. But If necessary we can use some other API which is more clear about the
response format, or create flexible parser.

### Assumption 3

The service can have some enhancements you can find them in the `TO_DO.md` file.