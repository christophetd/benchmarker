package benchmarks;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

public class Benchmarker {

	/**
	 * Contains the methods to test. Keys correspond to the fully qualified
	 * names of the methods, values to the parameters that should be applied to
	 * them.
	 */
	private HashMap<String, Object[]> toTest = new HashMap<>();

	/**
	 * Contains the results of the tests. Each method name is mapped to its
	 * average execution time (ms). If a method was not benchmarked, the map
	 * will not contain any key for it.
	 */
	private HashMap<String, Long> results = new HashMap<>();

	/**
	 * The number of times to run the benchmarks (must be greater than zero) and
	 * its default value
	 */
	private int nbRuns;
	static private final int DEFAULT_NB_RUNS = 1;

	/**
	 * Constructs a new BenchmarkComparator
	 * 
	 * @param nbRuns
	 *            Number of times to run the benchmarks (must be greater than
	 *            zero)
	 * @throws IllegalArgumentException
	 *             if this number is negative or null
	 */
	public Benchmarker(int nbRuns) {
		checkPositive(nbRuns);
		this.nbRuns = nbRuns;
	}

	/**
	 * Constructs a new BenchmarkComparator with default value for the number of
	 * times to run the benchmarks
	 */
	public Benchmarker() {
		this(DEFAULT_NB_RUNS);
	}

	/**
	 * Registers a method to be benchmarked
	 * 
	 * @param methodPath
	 *            Fully qualified name of the method
	 * @param args
	 *            Arguments that will be applied to the method (possibly empty)
	 * 
	 * @throws IllegalArgumentException
	 *             if the method path appears not to be a fully qualified name
	 */
	public void register(String methodPath, Object... args) {
		if (methodPath.indexOf(".") == -1)
			throw new IllegalArgumentException();

		toTest.put(methodPath, args);
	}

	/**
	 * @param methodPath
	 *            The fully qualified name of the method to remove
	 * 
	 * @throws NoSuchElementException
	 *             if the method was not previously registered
	 */
	public void unregister(String methodPath) {
		if (!toTest.containsKey(methodPath))
			throw new NoSuchElementException();

		toTest.remove(methodPath);
	}

	/**
	 * Runs the benchmarks for all registered methods
	 */
	public void execute() {
		execute(nbRuns);
	}

	/**
	 * Runs the benchmarks for all registered methods
	 * 
	 * @param nbRuns
	 *            The number of times to run each method
	 * @throws IllegalArgumentException
	 *             if this number is negative or null
	 */
	public void execute(int nbRuns) {
		checkPositive(nbRuns);
		Iterator<Entry<String, Object[]>> it = toTest.entrySet().iterator();
		Entry<String, Object[]> current;
		while (it.hasNext()) {
			current = it.next();
			execute(current.getKey(), current.getValue(), nbRuns);
		}
	}

	/**
	 * Runs the benchmark for a specific method. To be used only internally.
	 * 
	 * @param methodPath
	 *            Fully qualified name of the method
	 * @param args
	 *            Arguments that will be applied to the method (possibly empty)
	 */
	private void execute(String methodPath, Object[] args, int nbRuns) {
		int index = methodPath.lastIndexOf(".");
		String classPath = methodPath.substring(0, index);
		String methodName = methodPath.substring(index + 1);

		try {
			Class<?> cl = Class.forName(classPath);
			Object instance = cl.newInstance();
			Class<?>[] argTypes = getArgsTypes(args);
			Method method = cl.getMethod(methodName, argTypes);

			long startTime = System.currentTimeMillis();
			for (int i = 0; i < nbRuns; ++i) {
				method.invoke(instance, args);
			}

			long avgDuration = (long) ((System.currentTimeMillis() - startTime) / Double.valueOf(nbRuns));
			results.put(methodPath, avgDuration);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * (To be used only internally)
	 * 
	 * @param args
	 *            Array of parameters
	 * @return Array of classtypes of parameters
	 */
	private Class<?>[] getArgsTypes(Object[] args) {
		Class<?>[] types = new Class<?>[args.length];
		for (int i = 0; i < args.length; ++i) {
			types[i] = args[i].getClass();
		}
		return types;
	}

	/**
	 * @return The results of the benchmark, mapping each fully qualified method
	 *         name to its average execution time (ms).
	 */
	public HashMap<String, Long> getResults() {
		return results;
	}

	/**
	 * Returns the result of the benchmark for a particular method
	 * 
	 * @param methodPath
	 *            The fully qualified name of the method
	 * @return The average execution time for this method (ms)
	 */
	public Long getResult(String methodPath) {
		return results.get(methodPath);
	}

	/**
	 * Ensures that a given integer is positive
	 * 
	 * @param n
	 *            The number
	 * @throws IllegalArgumentException
	 *             if it is not
	 */
	private void checkPositive(int n) {
		if (n <= 0)
			throw new IllegalArgumentException();
	}
}