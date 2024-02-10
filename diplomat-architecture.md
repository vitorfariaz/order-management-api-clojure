## Diplomat architecture 
Here you'll find how each layer of the diplomat architecture interacts with each other

### Diplomat folder
Where all the "tech" stuff lives.
Expect to have DB connections, http-client, http-server, consumers/producers and so on...

### Wire
This is the "wires" that connects the tech layer with the more internal layers like business

wire.out represents everything that goes out from our API, like
- The internal Api responses
- Request to external API's
- Message requests
- DB models requests

wire.in represents everything that comes in to the API, like
- The requests that the API receive
- The response from external API's
- Consumed messaged
- DB models response

### Logic
Where all the business logic of the service resides. All the "domain" of the service

### Controller
Where all application flow is handled.
This layer control the flow of the application, it calls http-client, logic, db, messaging whenever is needed.

### Models
Represents the domain models of the business.

### Adapters
Where is translated the wires to models.

### Summarizing
The flow of a request should be something like this:
- http-server/consumer calls adapter
- http-server/consumer calls controller
- controller calls logic/http-client/db/messaging then return the response to http-server
