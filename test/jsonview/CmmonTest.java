package jsonview;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class CmmonTest {

	
	@Test
	public void test1(){
		String str = "{\"data1\":100,\"data2\":\"hello\",\"list\":[\"String 1\",\"String 2\",\"String 3\"]}";
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(str);
		String prettyJsonString = gson.toJson(je);
		System.out.println(prettyJsonString);
		
	}
}
