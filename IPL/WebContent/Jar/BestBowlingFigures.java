import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

public class BestBowlingFigures {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JavaSparkContext jsc = new JavaSparkContext(new SparkConf().setAppName("Best Bowling Figures"));
		JavaRDD<Map<String,String>> mainRdd = jsc.objectFile("deliveries.obj");
		JavaPairRDD<Tuple2<String, String>,Tuple2<Integer,Integer>> figuresRdd = mainRdd.mapToPair(s -> {
			String isDismissed = s.get("player_dismissed");
			int dismissed=0,runs=0;
			if (isDismissed!=null){
				dismissed=1;
			}
			runs=Integer.parseInt(s.get("legbye_runs"))+Integer.parseInt(s.get("wide_runs"))+
					Integer.parseInt(s.get("noball_runs"))+Integer.parseInt(s.get("batsman_runs"));
			Tuple2<Tuple2<String,String>,Tuple2<Integer,Integer>> t = new Tuple2<>(
					new Tuple2<String,String>(s.get("match_id"),s.get("bowler")), new Tuple2<Integer,Integer>(
							runs,dismissed));
			return t;
		});
		figuresRdd = figuresRdd.reduceByKey((a,b) -> {
			Tuple2<Integer,Integer> t = new Tuple2<>(a._1+b._1,a._2+b._2);
			return t;
		});
		//Data Type now: Match_ID,Bowler - Runs,Dismissal
		JavaPairRDD<String,Tuple2<Integer,Integer>> finalFiguresRdd = figuresRdd.mapToPair(s ->
			new Tuple2<String,Tuple2<Integer,Integer>>(s._1._2,new Tuple2<Integer,Integer>(s._2._1,s._2._2)));
		finalFiguresRdd = finalFiguresRdd.reduceByKey((a,b) -> {
			Tuple2<Integer,Integer> t;
			if (a._2>b._2){
				t=new Tuple2<>(a._1,a._2);
			}
			else if (a._2==b._2){
				if (a._1 < b._1){
					t=new Tuple2<>(a._1,a._2);
				}
				else {
					t=new Tuple2<>(b._1,b._2);
				}
			}
			else {
				t=new Tuple2<>(b._1,b._2);
			}
			return t;
		}).sortByKey();
		for(Tuple2<String,Tuple2<Integer,Integer>> t:finalFiguresRdd.collect()){
			System.out.println(t._1+":"+t._2._2+"/"+t._2._1);
		}
	}

}
