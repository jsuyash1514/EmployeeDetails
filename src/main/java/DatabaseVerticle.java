import com.google.gson.Gson;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class DatabaseVerticle extends AbstractVerticle {
  Map<String, Employee> database;
  
  @Override
  public void start(Future<Void> startFuture) throws Exception {
    database = new HashMap<>();
    vertx.eventBus().consumer("PUSH", this::pushObject);
    startFuture.complete();
  }
  
  private void pushObject(Message<JsonObject> message) {
    JsonObject jsonObject = message.body();
    String id = jsonObject.getString("employeeID");
    Employee employee = new Gson().fromJson(jsonObject.toString(), Employee.class);
    database.put(id, employee);
    System.out.println("New Entry pushed to db : " + jsonObject.toString());
    message.reply(new JsonObject().put("result", "OK"));
  }
}
