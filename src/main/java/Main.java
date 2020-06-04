import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

public class Main {
  
  private static Vertx vertx;
  
  public static void main(String[] args) {
    vertx = Vertx.vertx();
    DeploymentOptions deploymentOptions = new DeploymentOptions();
    deploymentOptions.setWorker(true);
    deploymentOptions.setWorkerPoolSize(20);
    vertx.deployVerticle(new DatabaseVerticle(), deploymentOptions, stringAsyncResult -> {
      if (stringAsyncResult.succeeded()) {
        System.out.println("Database Verticle successfully deployed at id: " + stringAsyncResult.result());
      } else {
        System.out.println("Database Verticle deployment Failed!");
      }
    });
    vertx.deployVerticle(new ServerVerticle(), stringAsyncResult -> {
      if (stringAsyncResult.succeeded()) {
        System.out.println("Server Verticle successfully deployed at id: " + stringAsyncResult.result());
      } else {
        System.out.println("Server Verticle deployment Failed!");
      }
    });
  }
  
  
}
