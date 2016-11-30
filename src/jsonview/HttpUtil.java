package jsonview;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpUtil {

	public static String[] request(String url,Map<String, String> headers){
		DefaultHttpClient httpClient = new DefaultHttpClient();
		
		return executeGet(httpClient,url,headers);
	}
	
	public static String[] executeGet(DefaultHttpClient httpClient,String url,Map<String, String> headers){
		StringBuffer sb = new StringBuffer("");
		StringBuffer sb2 = new StringBuffer("");
		StringBuffer sb3 = new StringBuffer("");
		try {
			String NL = System.getProperty("line.separator");
			sb2.append(String.format("GET %s"+NL, url));
			headers.forEach((k,v)->sb2.append(String.format("%s: %s"+NL, k,v)));
			HttpGet get = new HttpGet(url);
			headers.forEach((k,v)->get.addHeader(k,v));
			HttpResponse response = httpClient.execute(get);
			BufferedReader in = null;
			Stream.of(response.getAllHeaders()).forEach(h->sb.append( String.format("%s: %s"+NL, h.getName(),h.getValue())));
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			sb.append(NL);
			while ((line = in.readLine()) != null) {
				sb.append(line + NL);
				sb3.append(line);
			}
			in.close();
		}catch (Exception e) {
			sb.append(e.getMessage());
		}
		return new String[]{sb2.toString(),sb.toString(),sb3.toString()};
	}
}
