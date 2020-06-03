import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.templ.freemarker.FreeMarkerTemplateEngine;

public class ServerVerticle extends AbstractVerticle {
  private FreeMarkerTemplateEngine templateEngine;
  
  @Override
  public void start(Future<Void> startFuture) {
    HttpServer server = vertx.createHttpServer();
    Router router = Router.router(vertx);
    router.get("/form").handler(this::formRenderer); // Handles the getRequest on URL/form (Here URL: localhost:8080)
    router.post().handler(BodyHandler.create());
    router.post("/register").handler(this::registerHandler); // Handle the post request on URL/register
    templateEngine = FreeMarkerTemplateEngine.create(vertx);
    
    server.requestHandler(router)
        .listen(8080, httpServerAsyncResult -> {
          if (httpServerAsyncResult.succeeded()) {
            System.out.println("HTTP server running on port 8080");
            startFuture.complete();
          } else {
            System.out.println("Could not start a HTTP server\t" + httpServerAsyncResult.cause());
            startFuture.fail(httpServerAsyncResult.cause());
          }
        });
  }
  
  private void formRenderer(RoutingContext context) {
    context.put("title", "Employee Form"); // variables in html file are given value using context.put("variable_name","variable_value")
    templateEngine.render(context.data(), "templates/form.ftl", bufferAsyncResult -> {  //Renders the HTML file
      if (bufferAsyncResult.succeeded()) {
        context.response().putHeader("Content-Type", "text/html");
        context.response().end(bufferAsyncResult.result()); //Parameter in end method is the response of the request. It can be a string, object, asyncResult.
      } else {
        context.fail(bufferAsyncResult.cause());
      }
    });
  }
  
  private void registerHandler(RoutingContext context) {
    String employeeID = context.request().getParam("employeeid");
    String name = context.request().getParam("name");
    String age = context.request().getParam("age");
    String salary = context.request().getParam("salary");
    String address = context.request().getParam("address");
    System.out.println("Details received: " + employeeID + "\t" + name + "\t" + age + "\t" + salary + "\t" + address);
    
    context.put("title", "Thanks for submitting");
    context.put("employeeid", employeeID);
    context.put("name", name);
    context.put("age", age);
    context.put("salary", salary);
    context.put("address", address);
    
    templateEngine.render(context.data(), "templates/post_register.ftl", bufferAsyncResult -> {
      if (bufferAsyncResult.succeeded()) {
        context.response().putHeader("Content-Type", "text/html");
        context.response().end(bufferAsyncResult.result()); //Parameter in end method is the response of the request. It can be a string, object, asyncResult.
      } else {
        context.fail(bufferAsyncResult.cause());
      }
    });
    
    /**
     * If we have to redirect the page again to the form.
     *
     * context.response().setStatusCode(303);
     * context.response().putHeader("Location", "/form");
     * context.response().end();
     */
  }
}
