import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

public class BowlerWickets {

	public static void main(String[] args) {
		JavaSparkContext jsc = new JavaSparkContext(new SparkConf().setAppName("Bowler Wickets"));
		JavaRDD<Map<String,String>> mainRdd = jsc.objectFile("deliveries.obj");
		JavaPairRDD<String,Integer> wicketsRdd = mainRdd.mapToPair(s -> 
				{
					String dismissal = s.get("player_dismissed");
					Tuple2<String,Integer> t;
					if (dismissal==null){
						t=new Tuple2<>(s.get("bowler"),0);
					}
					else {
						t=new Tuple2<>(s.get("bowler"),1);
					}
					return t;
				});
		wicketsRdd = wicketsRdd.reduceByKey((a,b) -> {return a+b;}).sortByKey();
		for(Tuple2<String,Integer> result:wicketsRdd.collect()){
			System.out.println(result._1+":"+result._2);
		}
	}

}
