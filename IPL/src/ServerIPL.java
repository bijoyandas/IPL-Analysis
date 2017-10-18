

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SparkServer
 */
@WebServlet(urlPatterns="/ServerIPL", asyncSupported=true)
public class ServerIPL extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ServerIPL() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AsyncContext context = request.startAsync();
		
		//Queue<AsyncContext> jobQueue = new ConcurrentLinkedQueue<>();
		Executor executor = Executors.newFixedThreadPool(1);
		
		executor.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				exec("/home/bijoyan/workspace/SparkDeliveries/bin/spark-deliveries.jar", request,response);
			}
		});
		
//		exec("/home/bijoyan/workspace/SparkDeliveries/bin/spark-deliveries.jar", "BatsmanRuns",response);
			}

		
//		exec("/home/bijoyan/workspace/SparkDeliveries/bin/spark-deliveries.jar", "BatsmanRuns",response);
	
	
	public void exec(String jarPath, HttpServletRequest request,HttpServletResponse response) {
		String choice=request.getParameter("val");
		String classname="";
		if (choice.equals("Batsman Total Runs")){
			classname="BatsmanRuns";
		}
		else if (choice.equals("Batsman Highest Runs")){
			classname="BatsmanHighest";
		}
		else if (choice.equals("Bowler Wickets")){
			classname="BowlerWickets";
		}
		else if (choice.equals("Bowler Extras")) {
			classname="BowlerExtras";
		}
		else if (choice.equals("Best Bowling Figures")) {
			classname="BestBowlingFigures";
		}
		final String tableChoice = classname;
		ProcessBuilder pb = new ProcessBuilder("./spark-submit", "--class", classname, "/home/bijoyan/workspace/SparkDeliveries/bin/spark-deliveries.jar");
		pb.directory(new File("/home/bijoyan/Documents/Programs/spark-2.1.1-bin-hadoop2.7/bin"));
		Map<String, String> env = pb.environment();
		env.put("JAVA_HOME", "/home/bijoyan/Documents/Programs/jdk1.8.0_131");
		env.put("HADOOP_CONF_DIR", "/home/bijoyan/Documents/Hadoop/conf");
		try {
			Process p = pb.start();
			StringBuffer sb = new StringBuffer();
			new Thread() {
				public void run() {
					InputStream in = p.getInputStream();
					BufferedReader br = new BufferedReader(new InputStreamReader(in));
					String line;
					if (tableChoice.equals("BatsmanRuns")){
						sb.append("Batsman:Runs<br>");
					}
					else if (tableChoice.equals("BatsmanHighest")){
						sb.append("Batsman:Highest<br>");
					}
					else if (tableChoice.equals("BowlerExtras")){
						sb.append("Bowler:Extras<br>");
					}
					else if (tableChoice.equals("BowlerWickets")){
						sb.append("Bowler:Wickets<br>");
					}
					else if (tableChoice.equals("BestBowlingFigures")) {
						sb.append("Bowler:Figures<br>");
					}
					try {
						while ((line = br.readLine()) != null)
							sb.append(line+"<br>");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						response.getWriter().println(sb.toString());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						p.waitFor();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();
			
			new Thread() {
				public void run() {
					InputStream in = p.getErrorStream();
					int ch;
					try {
						while ((ch = in.read()) != -1)
					//		System.out.print((char)ch);
							System.out.print((char)ch);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						p.waitFor();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}