package benchmarks;

import java.util.HashMap;
import java.util.Random;

public class MyBenchmark {
	
	static public void main(String[] args) {
		
		Benchmarker benchmarker = new Benchmarker(10);
		

		benchmarker.register("benchmarks.MyBenchmark.calibrated"); // Will run in approx. 111ms
		benchmarker.register("benchmarks.MyBenchmark.concatenation", 10000, 5);
		benchmarker.register("benchmarks.MyBenchmark.stringbuilder", 10000, 5);
		benchmarker.execute();
		
		HashMap<String, Long> results = benchmarker.getResults();
		for(String method: results.keySet()) {
			System.out.println(method+" took in average "+results.get(method)+"ms to execute");
		}
		
		// Or just get the result of a particular method using for instance benchmarker.getResult("benchmarks.MyBenchmark.builder")
	}
	
	public static void calibrated() throws InterruptedException {
		Thread.sleep(111);
	}

	public static void concatenation(Integer nb, Integer len) {
		String result = "";
		for(int i = 0; i < nb; ++i){
			result += randomString(len);
		}
		
	}

	public static void stringbuilder(Integer nb, Integer len) {
		StringBuilder b = new StringBuilder("");
		for(int i = 0 ; i < nb; ++i) {
			b.append(randomString(len));
		}
		b.toString();
	}
	
	public static String randomString(int len) {
		StringBuilder b = new StringBuilder("");
		Random r = new Random();
		for(int i = 0; i < len; ++i) {
			b.append((char) (48 + r.nextInt(74)));
		}
		return b.toString();
	}
}