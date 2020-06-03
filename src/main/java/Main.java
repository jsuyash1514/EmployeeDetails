import io.vertx.core.Vertx;

public class Main {
  
  private static Vertx vertx;
  
  public static void main(String[] args) {
    vertx = Vertx.vertx();
    vertx.deployVerticle(new ServerVerticle(), stringAsyncResult -> {
      if (stringAsyncResult.succeeded()) {
        System.out.println("Verticle successfully deployed at id: " + stringAsyncResult.result());
      } else {
        System.out.println("Deployment Failed!");
      }
    });
  }
  
  
}
