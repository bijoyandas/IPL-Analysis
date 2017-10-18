import java.util.HashMap;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

public class BatsmanHighest {

	public static void main(String[] args) {
		JavaSparkContext jsc = new JavaSparkContext(new SparkConf().setAppName("Highest Runs"));
		//jsc.setLogLevel("OFF");
		JavaRDD<Map<String,String>> mainRdd = jsc.objectFile("deliveries.obj");
		JavaPairRDD<Tuple2<String,String>,Integer> matchBatsmanRdd = mainRdd.mapToPair(s -> 
			new Tuple2<>(new Tuple2<String,String>(s.get("match_id"),s.get("batsman")),Integer.parseInt(s.get("batsman_runs"))));				
		//Right now we have runs scored by every batsman in every match 
		matchBatsmanRdd = matchBatsmanRdd.reduceByKey((a,b) -> {return a+b;});
		JavaPairRDD<String,Integer> batsmanHighest = matchBatsmanRdd.mapToPair(s -> 
				new Tuple2<>(s._1._2,s._2));
		batsmanHighest = batsmanHighest.reduceByKey((a,b) -> {return a>b?a:b;}).sortByKey();
		for(Tuple2<String,Integer> result: batsmanHighest.collect()){
			System.out.println(result._1+":"+result._2);
		}
	}
	
}
