# Example Spring Cloud Function

This is an example [spring cloud function](https://github.com/spring-cloud/spring-cloud-function) 
project running on Fn using the 
[`SpringCloudFunctionInvoker`](/runtime/src/main/java/com/fnproject/fn/runtime/spring/SpringCloudFunctionInvoker.java).

Firstly, if you have used `fn` before you'll want to make sure you have the latest runtime image which includes the Spring support:

```bash
$ docker pull fnproject/fdk-java:latest
```

Then you can build and deploy the app

Once the app is deployed you need to make the route through API Gateway
https://docs.oracle.com/en-us/iaas/developer-tutorials/tutorials/functions/func-api-gtw/01-summary.htm


```bash
fn build
fn deploy --app <app_name>
```

Try invoking the image through command 
```bash
fn invoke <app_name> <function_name>
```
Double-check that the VCN includes an internet gateway or service gateway. 
For Oracle Functions to be able to access Oracle Cloud Infrastructure Registry to pull an image, 
the VCN must include an internet gateway or a service gateway. Follow the link
https://docs.oracle.com/en-us/iaas/Content/Functions/Tasks/functionstroubleshooting_topic-Issues-invoking-functions.htm

Under the policy, check  for the policy for allowing all the compartment user to have access over the url
```bash
ALLOW any-user to use functions-family in <compartment> where ALL {request.principal.type= 'ApiGateway', request.resource.compartment.id = '<compartment_id>'}
```

Now you can call those functions using `fn invoke` or curl:

```bash
$ echo "Hi there" | fn invoke spring-cloud-fn 
Hello world

$ curl -d "Bob" http://<url-apigateway>/hello
Hello Bob
```


## Code walkthrough

```java
@Configuration
```
Defines that the class is a 
[Spring configuration class](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/annotation/Configuration.html) 
with `@Bean` definitions inside of it.

```java
@Import(ContextFunctionCatalogAutoConfiguration.class)
```
Specifies that this configuration uses a [`InMemoryFunctionCatalog`](https://github.com/spring-cloud/spring-cloud-function/blob/a973b678f1d4d6f703a530e2d9e071b6d650567f/spring-cloud-function-context/src/main/java/org/springframework/cloud/function/context/InMemoryFunctionCatalog.java)
that provides the beans necessary
for the `SpringCloudFunctionInvoker`.

```java
    ...
    @FnConfiguration
    public static void configure(RuntimeContext ctx) {
        ctx.setInvoker(new SpringCloudFunctionInvoker(SCFExample.class));
    }
```

Sets up the Fn Java FDK to use the SpringCloudFunctionInvoker which performs function discovery and invocation.

```java
    // Unused - see https://github.com/fnproject/fdk-java/issues/113
    public void handleRequest() { }
```

Currently the runtime expects a method to invoke, however this isn't used in the SpringCloudFunctionInvoker so we declare an empty method simply to keep the runtime happy. This will not be necessary for long - see the linked issue on GitHub.


```java
    @Bean
    public Function<String, String> function(){
        return value -> "Hello, " + ((value == null || value.isEmpty()) ? "world"  : value ) + "!";
        }
```

Finally the heart of the configuration; the bean definitions of the functions to invoke.

Note that these methods are not the functions themselves. They are factory methods which *return* the functions. As the Beans are constructed by Spring it is possible to use `@Autowired` dependency injection.
