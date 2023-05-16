# Yandex.Lavka Courier Service

This is a RESTful API for the Yandex.Lavka courier service, implemented using Java Spring Boot, Docker, Gradle, and
OpenAPI. 

Designed for `School of Backend Development in Yandex 2023`.

The service provides basic CRUD operations for orders and couriers and allows for the automatic distribution of orders
between couriers for maximum efficiency.

Additionally, you can obtain information about the courier's earnings and rating through
the `GET /couriers/meta-info/{courier_id}` endpoint.

"The OpenAPI specification for this service is available at `http://localhost:8080/`, where you can explore the API
endpoints, make test requests, and view the generated documentation."

## Getting Started

To get started with the service, follow these instructions:

- Clone this repository:

`git clone https://github.com/your-username/your-repository.git`

`cd your-repository`

- Build the Docker image:

`docker build -t yandex-lavka-courier-service .`

- Start the container:

`docker run -p 8080:8080 yandex-lavka-courier-service`

## Rate Limiting

The service uses the Resilience4J library to limit the number of incoming requests. Each endpoint is limited to 10
requests per second. If the number of requests exceeds this limit, the service responds with a 429 status code.

## Order distribution among couriers

The following parameters are taken into account for the distribution of orders among couriers:

- Order weight
- Delivery region
- Delivery cost

### Order Weight

Each category of courier has a limit on the weight of the transported order and the number of orders.

- FOOT - Max 10 KG - Max 2 orders
- BIKE - Max 20 KG - Max 4 orders
- AUTO - Max 40 KG - Max 7 orders

### Delivery Region

The type of transport used affects the number of regions that a courier can visit when delivering orders

- FOOT - 1 Region
- BIKE - 2 Regions
- AUTO - 3 Regions

### Delivery Time

Delivery time is the time taken to visit all points for delivering an order in a region, plus the waiting time for
delivering the order.

Time to visit all points in one region:

- FOOT - 25 mins 1st order - 10 mins others
- BIKE - 12 mins 1st order - 8 mins others
- AUTO - 8 mins 1st order - 4 mins others

When delivering goods in another region, the time is also calculated as follows:

- BIKE - 12 mins 1st order - 8 mins others
- BIKE - 8 mins 1st order - 4 mins others

## Dependencies

The following dependencies are used in this project:

- Spring Boot
- Gradle
- Docker
- OpenAPI
- Resilience4J
