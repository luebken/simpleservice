# SimpleSevice

This is a very `simpleservice` that can be configured with environment variables to behave differently.

See [Makefile] to get it up and running.

The following call tree is currently in place:

curl 
  -> service-a.local/endpoint-a 
    -> service-b.local/endpoint-b (error 30%)
      -> service-c.local/last-endpoint
    -> service-b.local/last-endpoint
    -> service-b.local/last-endpoint