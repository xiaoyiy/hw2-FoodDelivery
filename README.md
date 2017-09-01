HW2 - Food Delivery

ARCHITECTURE DESIGN:
![UML](UML.png)

DESIGN EXPLANATION:
1. Restaurant Info Service: It stores the restaurant info, including restuarnt name, restuarant menu, menu items. It supports the funtionality of inserting new restaurants, and letting user search restaurant by name. When restaurant is found, the restuarant object is returned, which include the all the menu items.
2. Order Management Servie: After user selects the items from a restaurant, user will place order. User sends an OrderInfo objects, with items, address, etc to the Order Management Service to create a new order, and some additional order info is returned to user after order is created, including Order ID, Total Price, etc. The User will next go to the payment step. After payment is done, user can send request to Order Mangement Service to check the status of the order. System will respond with latest status, such as whether payment succeed, and the estimated delivery status. Order Management Servie also picks up messages from a RabbitMQ to update the order status based on payment success/fail info passed from Payment Process Service.
3. Payment Process Service: After user creates order and received the order ID and total price, user sends payment info to Payment Process Service which handles the credit card payment. Payment Process Service responds user with Payment ID and Status. It then sends a Message to MQ with the payment status for Order Management Service to pick up and udpate order status based on payment status.


API DESIGN:
1. Restaurant Info Service: http://restaurant-info-service
  - HTTP POST /restaurant
    REQUEST: JSON of all restaurants
    RESPONSE: 201

  - HTTP GET /restaurant/{restaurantName}
    RESPONSE: 200 restaurant with given name

  - HTTP DELETE /restaurant
    RESPONSE: 200 delete all restaurants

  - HTTP DELETE /restaurant/{restaurantName}
    RESPONSE: 200 delete given restaurant

2. Order Management Service: http://order-management-service
  - HTTP POST /order
    REQUEST: orderInfo
    RESPONSE: 201 orderInfo containing orderId, totalPrice

  - HTTP POST /order/status/{orderId}
    REQUEST: payment processing status from Payment Process Service
    RESPONSE: 200 orderInfo with updated OrderStatus

  - HTTP GET /order/{orderId}
    RESPONSE: 200 orderInfo with latest status

  - HTTP DELETE /order
    RESPONSE: 200 delete all orders

  - HTTP DELETE /order/{orderId}
    RESPONSE: 200 delete given order

3. Payment Process Service: http://payment-process-service
  - HTTP POST /payment
    REQUEST: payment info with orderId
    RESPONSE: 200 Payment Info(including Payment ID, payment status)

  - HTTP GET /payment/{paymentId}
    RESPONSE: 200 Payment Info(including Payment ID, payment status)

  - HTTP DELETE /payment
    RESPONSE: 200 delete all payments

  - HTTP DELETE /payment/{paymentId}
    RESPONSE: 200 delete given payment

RUNNING INSTRUCTION:
1. Initialization:
  a. docker-compose up
  b. mvn clean install
  c. start eureka: java -jar platform/eureka/target/food-delivery-eureka-server-0.0.1.BUILD-SNAPSHOT.jar 
  d. monitor eureka: http://localhost:8761/
  e. monitor RabbitMQ: http://localhost:15672/

2. Restaurant Info Service
  a. start Restauarant Info Serivice: java -jar Restaurant-Info-Service/target/Restaurant-Info-Service-1.0.0.BUILD-SNAPSHOT.jar
  b. verify registration on eureka 
  c. create restuarants: POST http://localhost:9005/restaurant/ with restaurant.json under TestCases folder
  d. confirm restaurant exists: GET http://localhost:9005/restaurant/ABC Seafood?page=0

3. Order Management Service
  a. start Order Management Service: java -jar Order-Management-Service/target/Order-Management-Service-1.0.0.BUILD-SNAPSHOT.jar
  b. verify registration on eureka
  c. create order: POST http://localhost:9006/order, with neworder_valid.json under TestCase. The order will be returned in response body as NEW status, and including total price. (orderId 1)
  d. repeat c. Create another order. (orderId 2)
  d. get order status: GET http://localhost:9006/order/1, same response will be received in response body
  e. create invalid order: POST http://localhost:9006/order, with neworder_invalid.json under TestCase. The order will returned in response body as ERROR status

4. Payment Process Service
  a. leave Order Management Service up and running
  b. start Payment Process Service: java -jar Payment-Process-Service/target/Payment-Process-Service-1.0.0.BUILD-SNAPSHOT.jar
  c. verify registration on eureka
  d. we should see the queue payments on RabbitMQ now
  e. send payment: POST http://localhost:9007/payment with payment_valid.json under TestCase. The Payment Info will be saved to DB, as well as returned as response indication if the payment is successful or not. Meanwhile, a message is sent to RabbitMQ to notify Order Management Service that the payment for orderId 1 is received and processed successfully so Order Management Service will update the order to COMPLETED status and set an estimate delivery time.
  f. query the updated order status: GET http://localhost:9006/order/1 The order should be COMPLETED with an estimated time now.
  g. send failure payment: POST http://localhost:9007/payment with payment_invalid.json under TestCase. With the invalid card number, the payment will returned as FAIL. A message will be sent to Order Management Service to update the order status to CANCELLED.
  h. query the updated order status: GET http://localhost:9006/order/2 The order should be CANCELLED.


TESTS
1. Unit Test: Order-Management-Service/src/test/java/com/rest/OrderManagementRestControllerTest.java
2. Integration Test: Order-Management-Service/src/test/java/com/domain/OrderRepositoryTest.java
3. MVC test: Restaurant-Info-Service/src/test/java/com/rest/RestaurantInfoRestTest.java