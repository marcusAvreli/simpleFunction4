package simpleFunction4;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import org.json.JSONObject;

/**
 * Hello world!
 *
 */
public class App 
{
	AtomicReference<String> ar= new AtomicReference<String>("ref");
	 private ScheduledExecutorService reinitializer = null;
	 public synchronized Supplier<String> start() {
		 System.out.println("hello");
		 reinitializer = Executors.newSingleThreadScheduledExecutor();
		 initialize().join();
		 return ar::get;
	 }
	 private CompletableFuture<Void> initialize() {
    	 System.out.println("Executing init");
    	 return doUnauthorizedUrlEncodedPostForJsonObject().thenAccept(response ->{
    	 String initialAccessToken = response.getString("test");
    	 System.out.println(initialAccessToken);
    	 reinitializer.schedule(() -> initialize().join(), 1, TimeUnit.SECONDS);
    	 ar.set(initialAccessToken);
    	 });
    
    }
    public  CompletableFuture<JSONObject> doUnauthorizedUrlEncodedPostForJsonObject() {
        JSONObject jsonObject = new JSONObject();
       jsonObject.put("test", "testValue");
       System.out.println("doUnauthorizedUrlEncodedPostForJsonObject");
       CompletableFuture<JSONObject> future = new CompletableFuture<JSONObject>();
       future = CompletableFuture.supplyAsync(new Supplier<JSONObject>() {
    	   public JSONObject get() {
    		   JSONObject jsonObject = new JSONObject();
    		   jsonObject.put("test", "testValue");
    		   return jsonObject;
    	   }
       });
       return future;
    }
    public static void main(String... args){
	        
	     Supplier<String> ff=  new App().start();
	     
	     System.out.println("ff:"+ff.get());
    }
}
