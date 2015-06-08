# spring-rest-client
spring rest client

## a rest api can be written using spring mvc like this:

```java
@RestController
@RequestMapping("/another")
public class AnotherController {
    @RequestMapping("/add")
    public int add(@RequestParam("offset") int offset) {
        // do something
        return offset ;
    }
}
```

## a rest client can be declared like this:

```java

@RequestMapping("/another")
@SpringRestClientEnabled(baseUrl = "http://localhost:8080")
public interface AnotherApi {
    @RequestMapping("/add")
    int add(@RequestParam("offset") int offset);
}
```

And then import spring-rest-client config like this:


```java
@Configuration
@ComponentScan
@SpringRestClientEnabledScan
public class SpringRestClientConfig {
}
```

And then you can call the api like this:

```java
@Autowired AnotherApi anotherApi;

int a = 123;
int account = anotherApi.add(a);
```