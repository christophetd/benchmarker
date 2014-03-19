#Java benchmarker

Java class for easy benchmarking.

## Usage

A complete example is provided in the [MyBenchmark.java](https://github.com/christophetd/benchmarker/blob/master/MyBenchmark.java) file, where two ways of building strings (concatenation and using StringBuilder class) are benchmarked.
You can also look for yourself in [Benchmark.java](https://github.com/christophetd/benchmarker/blob/master/Benchmarker.java), everything is fully documented.

## Example

```java
// Creates a benchmarker that runs each test 15 times
Benchmarker benchmarker = new Benchmarker(10);

// Register a method that takes no argument
benchmarker.register("path.to.my.class.method");

// Register a method that takes arguments
benchmarker.register("path.to.my.class.methodWithArguments", 42, "foo");

// Go !
benchmarker.execute();

// Get the results as a map of (method name -> average running time)
HashMap<String, Long> results = benchmarker.getResults();
```