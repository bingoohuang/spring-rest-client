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


## Bypass spring boot request mapping
```java
// for spring boot 2.0
import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

@Configuration
public class SpringFrontConfig {
    @Bean
    public WebMvcRegistrations webMvcRegistrationsAdapter() {
        return new WebMvcRegistrations() {
            @Override
            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
                return new RequestMappingHandlerMapping() {
                    @Override protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
                        // by pass SpringRestClientEnabled interface
                        if (method.getDeclaringClass().isAnnotationPresent(SpringRestClientEnabled.class)) return;
                        super.registerHandlerMethod(handler, method, mapping);
                    }
                };
            }
        };
    }

}
```

```java
// for spring boot 1.x

import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import com.raiyee.hi.notify.WxNotifyEnabled;
import org.springframework.boot.autoconfigure.web.WebMvcRegistrationsAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

@Configuration
@WxNotifyEnabled
public class SpringConfig {
    @Bean
    public WebMvcRegistrationsAdapter webMvcRegistrationsAdapter() {
        return new WebMvcRegistrationsAdapter() {
            @Override
            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
                return new RequestMappingHandlerMapping() {
                    @Override
                    protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
                        // by pass SpringRestClientEnabled interface
                        if (method.getDeclaringClass().isAnnotationPresent(SpringRestClientEnabled.class)) return;
                        super.registerHandlerMethod(handler, method, mapping);
                    }
                };
            }
        };
    }
}


```