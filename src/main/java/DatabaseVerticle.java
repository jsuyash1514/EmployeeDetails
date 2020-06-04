import com.google.gson.Gson;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class DatabaseVerticle extends AbstractVerticle {
  Map<String, Employee> database;
  
  @Override
  public void start(Future<Void> startFuture) throws Exception {
    database = new HashMap<>();
    vertx.eventBus().consumer("PUSH", this::pushObject);
    vertx.eventBus().consumer("DATABASE", this::getDatabase);
    startFuture.complete();
  }
  
  private void pushObject(Message<JsonObject> message) {
    JsonObject jsonObject = message.body();
    String id = jsonObject.getString("employeeID");
    Employee employee = new Employee(
        jsonObject.getString("employeeID"),
        jsonObject.getString("name"),
        jsonObject.getString("age"),
        jsonObject.getString("salary"),
        jsonObject.getString("address")
    );
    database.put(id, employee);
    System.out.println("New entry pushed to db : "+ jsonObject.toString());
    System.out.println("New entry test : " + employee.employeeID + ","+ employee.name + ","+ employee.age + ","+ employee.salary + ","+ employee.address);
    message.reply(new JsonObject().put("result", "OK"));
  }
  
  private void getDatabase(Message<JsonObject> message) {
    JsonArray array = new JsonArray();
    for (String key: database.keySet()) {
      array.add(new JsonObject().put(key,new JsonObject(new Gson().toJson(database.get(key)))));
    }
    message.reply(new JsonObject().put("Employees",array));
  }
}
