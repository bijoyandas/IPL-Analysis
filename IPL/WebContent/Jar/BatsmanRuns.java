import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

public class BatsmanRuns {

	public static void main(String[] args) {
		JavaSparkContext jsc = new JavaSparkContext(new SparkConf().setAppName("Batsman Runs"));
		JavaRDD<Map<String,String>> deliveriesRdd = jsc.objectFile("deliveries.obj");
		JavaPairRDD<String,Integer> batsmanRdd = deliveriesRdd.mapToPair(s -> 
				new Tuple2<>(s.get("batsman"),Integer.parseInt(s.get("batsman_runs"))));
		JavaPairRDD<String,Integer> batsmanRdd2 = batsmanRdd.reduceByKey((a,b) -> {return a+b;}).sortByKey();
		for(Tuple2<String,Integer> result : batsmanRdd2.collect()){
			System.out.println(result._1 + ":"+result._2.intValue());
		}
	}

}
