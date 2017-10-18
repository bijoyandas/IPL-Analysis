import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

public class BowlerExtras {

	public static void main(String[] args) {
		JavaSparkContext jsc = new JavaSparkContext(new SparkConf().setAppName("Bowler Extras"));
		JavaRDD<Map<String,String>> mainRdd = jsc.objectFile("deliveries.obj");
		JavaPairRDD<String,Integer> extrasRdd = mainRdd.mapToPair(s -> 
				new Tuple2<>(s.get("bowler"),Integer.parseInt(s.get("wide_runs"))+Integer.parseInt(s.get("noball_runs"))+
							Integer.parseInt(s.get("bye_runs"))+Integer.parseInt(s.get("legbye_runs"))));
		extrasRdd = extrasRdd.reduceByKey((a,b) -> {return a+b;}).sortByKey();
		for(Tuple2<String,Integer> result : extrasRdd.collect()){
			System.out.println(result._1+":"+result._2);
		}
	}

}
