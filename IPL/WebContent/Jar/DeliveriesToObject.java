import java.util.HashMap;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;

public class DeliveriesToObject {

	public static void main(String[] args) {
		JavaSparkContext jsc = new JavaSparkContext(new SparkConf().setAppName("Deliveries to Object"));
		JavaRDD<String> mainRdd = jsc.textFile("deliveries.csv");
		Broadcast<String[]> titleRow = jsc.broadcast(mainRdd.first().split(","));
		JavaRDD<String> deliveriesRdd = mainRdd.filter(s -> !s.substring(0,8).equals("match_id"));
		JavaRDD<Map<String,String>> newDeliveriesRdd = deliveriesRdd.map(s -> {
			String[] l = s.split(",");
			Map<String,String> d = new HashMap<>();
			int i=0;
			for(String key : titleRow.value()){
				if (i<l.length)
					d.put(key, l[i++]);
				else
					d.put(key, null);
			}
			return d;
		});
		newDeliveriesRdd.saveAsObjectFile("deliveries.obj");
	}

}
