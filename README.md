# vertx-absolute-uri
1. Run Main.java

Currently there is a divergence between the Request-URI and the absoluteURI:
    Wait a second...
    Client: GET Request-URI = http://myhostname:8080/path
    Server: absoluteURI = http://0.0.0.0:8080/path
    Received response with status code 200
