import com.google.gson.Gson;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
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
    router.get("/database").handler(this::databaseHandler);
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
    Employee employee = new Employee(
        context.request().getParam("employeeid"),
        context.request().getParam("name"),
        context.request().getParam("age"),
        context.request().getParam("salary"),
        context.request().getParam("address"));
    JsonObject object = new JsonObject(new Gson().toJson(employee));
    
    System.out.println("Details received: " + object.toString());
    vertx.eventBus().request("PUSH", object, messageAsyncResult -> {
      JsonObject reply = (JsonObject)messageAsyncResult.result().body();
      if(messageAsyncResult.succeeded()){
        System.out.println("Push to db: " + reply.toString());
        context.put("title", "Thanks for submitting");
        context.put("employeeid", employee.employeeID);
        context.put("name", employee.name);
        context.put("age", employee.age);
        context.put("salary", employee.salary);
        context.put("address", employee.address);
  
        templateEngine.render(context.data(), "templates/post_register.ftl", bufferAsyncResult -> {
          if (bufferAsyncResult.succeeded()) {
            context.response().putHeader("Content-Type", "text/html");
            context.response().end(bufferAsyncResult.result()); //Parameter in end method is the response of the request. It can be a string, object, asyncResult.
          } else {
            context.fail(bufferAsyncResult.cause());
          }
        });
      }
      else {
        System.out.println("Publish event failed");
        context.fail(messageAsyncResult.cause());
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
  
  private void databaseHandler(RoutingContext context) {
    vertx.eventBus().request("DATABASE", new JsonObject(), messageAsyncResult -> {
      JsonObject reply = (JsonObject)messageAsyncResult.result().body();
      System.out.println(reply.toString());
      if(messageAsyncResult.succeeded()) {
        context.response().putHeader("Content-Type", "application/json");
        context.response().end(reply.toString());
      }
      else {
        context.fail(messageAsyncResult.cause());
      }
    });
  }
}
