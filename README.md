# order-management-api-clojure
A Clojure api, with connection in Mongo DB and a http-client.
This project exemplify the usage of [Diplomat architecture](diplomat-architecture.md) concept. 


## Start the application 
Download the project
Run `lein deps` on root 
Run `lein repl` 
Run the below command on repl window
```
(use 'server)
(start)
```
Then the API will be listening at http://localhost:3000 


## Tech stack 
- Clojure (programming language)
- Ring (framework to handle http server abstractions)
- munntaja (fast HTTP format negotiation, encoding and decoding)
