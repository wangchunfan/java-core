# 第 1 章 Java SE 8 的流库

操作流的典型流程：

1. 创建流
2. 流转换
3. 终止操作

流在执行终止操作以后就不能再使用转换操作了。

以下代码使用的 show() 函数

```java
public static <T> void show(String title, Stream<T> stream) {
        final int SIZE = 10;
        List<T> firstElements = stream.limit(SIZE + 1).collect(Collectors.toList());
        System.out.println(title + ":");
        for (int i = 0; i < firstElements.size(); i++) {
            if (i > 0) System.out.print(", ");
            if (i < SIZE) System.out.print(firstElements.get(i));
            else System.out.print("...");
        }
        System.out.println("\n");
    }
```

## 创建流

1. `java.util.stream.Stream 8`
    - 1.1 `static <T> Stream<T> of(T... values)`：参数可以是多个元数据，或者是一个集合
    - 1.2 `static <T> empty()`
    - 1.3 `static <T> generate(Supplier<T> s)`
    - 1.4 `static <T> Stream<T> iterate(T seed, UnaryOperator<T> f)`
2. `java.util.Arrays 1.2`
    - 2.1 `static <T> Stream<T>(T[] array)`
    - 2.2 `static <T> Stream<T>(T[] array, int startInclusive, int endExclusive)`
3. `java.util.regex.Pattern 1.4`
    - 3.1 `Stream<String> splitAsStream(CharSequence input) 8`
4. `java.nio.file.Files 7`
    - 4.1 `static Stream<String> lines(Path path) 8`
    - 4.2 `static Stream<String> lines(Path path, Charset cs) 8`,指定字符集
5. `java.util.function.Supplier<T> 8`
    - 5.1 `T get()`
6. `java.util.Collection`
    - 6.1 `default Stream<E> stream()`

```java

    public static <T> void show(String title, Stream<T> stream) {
        final int SIZE = 10;
        List<T> firstElements = stream.limit(SIZE + 1).collect(Collectors.toList());
        System.out.println(title + ":");
        for (int i = 0; i < firstElements.size(); i++) {
            if (i > 0) System.out.print(", ");
            if (i < SIZE) System.out.print(firstElements.get(i));
            else System.out.print("...");
        }
        System.out.println("\n");
    }


    public static void main(String[] args) throws IOException {

        // 1.1
        // Stream.of() 可以转换任意 集合或任意数量引元字符串
        // Stream.of(new String[]{"1", "2"}, new String[]{"3", "4", "5"}); 不可以
        Stream<String> StreamOf1 = Stream.of(new String[]{"1", "2"});
        Stream<String> StreamOf2 = Stream.of("hello", "word");
        show("StreamOf1", StreamOf1);   // 1, 2
        show("StreamOf2", StreamOf2);   // hello, word

        // 1.2
        // Stream.empty():产生一个不包含任何元素的流
        Stream<String> StreamEmpty = Stream.empty();
        show("StreamEmpty", StreamEmpty); //

        // 1.3
        // Stream.generate() 产生无限流：通过反复调用函数
        Stream<String> StreamGenerate = Stream.generate(() -> "hello");
        show("StreamGenerate", StreamGenerate); // hello, hello, hello, hello, hello, hello, hello, hello, hello, hello, ...

        // 1.4
        // Stream.iterate() 产生无限流：在种子上调用 add 函数，此结果再调用 add 函数
        Stream<BigInteger> StreamIterate = Stream.iterate(BigInteger.ONE, n -> n.add(BigInteger.ONE));
        show("StreamIterate", StreamIterate);   // 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, ...

        // 2.1
        // Arrays.stream() 将数组转换为流
        Stream<String> ArraysStream = Arrays.stream(new String[]{"1", "2", "3"}, 2, 3);
        show("ArraysStream", ArraysStream); // 3

        // 3.1
        // Pattern.comple().splitAsStream(); 按照正则表达式分隔对象产生流
        Stream<String> PatternCompleSplitAsStream = Pattern.compile(",").splitAsStream("a,b,c,d");
        show("PatternCompleSplitAsStream", PatternCompleSplitAsStream); // a, b, c, d

        // 4.1
        // Files.lines() 返回包含文件所有行的流
        Path path = Paths.get("javacore-volume2/javacore-volume2-01stream/src/main/resources/test.text");
        try (Stream<String> FilesLines = Files.lines(path)) {
            show("FilesLines1", FilesLines); // hello, world, 我
        }
        // 4.2
        try (Stream<String> FilesLines = Files.lines(path, StandardCharsets.ISO_8859_1)) {
            show("FilesLines2", FilesLines); // hello, world, æ
        }

        // 6.1
        // Collection.stream() Collection 接口的 stream 方法
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        show("Collection.stream", list.stream());  // a, b
    }
```

test.txt
```
hello
world
我    
```

## 流的转换

### 1. filter/map/flatMap

`java.util.stream.Stream 8`
1. `Stream<T> filter(Predicate<? super T> predicate)`：返回满足断言条件的元素
2. `Stream<R> map(function<? super T, ? extends R> mapper)`：对流中每个元素一次处理
3. `<R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper)`:对流中每个元素生成一个流，然和把这些流合并成一个流



```java
public class Transform {

    public static void main(String[] args) {
        Stream<String> stream = Stream.of("hello", "world", "how", "are", "you");
        show("stream", stream); // hello, world, how, are, you

        Stream<String> streamFilter = Stream.of("hello", "world", "how", "are", "you");
        show("streamFilter", streamFilter.filter(s -> s.length() > 3)); // hello, world

        Stream<String> streamMap = Stream.of("hello", "world", "how", "are", "you");
        show("streamMap", streamMap.map(a -> a.substring(0, 1)));   // h, w, h, a, y

        Stream<String> flatMap = Stream.of("hello", "world", "how", "are", "you");
        show("streamMap", flatMap.flatMap(a -> letters(a)));    // h, e, l, l, o, w, o, r, l, d, ...
    }
```

用到的工具函数

```java
public static Stream<String> letters(String s) {
    List<String> result = new ArrayList<>();
    for (int i = 0; i < s.length(); i++) {
        result.add((s.substring(i, i + 1)));
    }
    return result.stream();
}

```

### 2. limit/skip/concat

获取子流和连接流

`java.util.stream.Stream 8`
1. `Stream<T> limit(long maxSize)`：获取流中前几个元素
2. `Stream<T> skip(long n)`：跳过流中前几个元素
3. `static <T> Stream<T> concat (Stream<? enxtends T> a, Stream<? enxtends T> b)`：合并两个流

```java
Stream<Double> limitStream = Stream.generate(Math::random).limit(3);
show("limitStream", limitStream);   // 0.3758453005326531, 0.21778149147931358, 0.2985891860376019

Stream<String> skipStream = Stream.of("a", "b", "c").skip(1);
show("skipStream", skipStream); // b, c

Stream<String> concatStream = Stream.concat(Stream.of("1", "2"), Stream.of("3", "4"));
show("concatStream", concatStream); // 1, 2, 3, 4
```

### 3. distinct/sorter/peek


`java.util.stream.Stream 8`
1. `Stream<T> distinct()`：对当前流去重
2. `Stream<T> sorted(Comparator<? super T> comparator)`：comparator 可以省略则按默认方式
3. `Stream<T> peek(Consumer<? super T> action`：调用该函数的流，流中的每个元素被访问都会执行 action，适合用来打印调试信息；当流没有终结时，是不会调用该函数的，说明流的操作是惰性的。

```java
        Stream<String> distinctStream = Stream.of("1", "2", "1", "3");
        show("distinctStream", distinctStream);

        Stream<String> sortedStream = Stream.of("3", "1", "4").sorted();
        show("sortedStream", sortedStream); // 1, 3, 4

        Stream.of(1, 2, 3).peek(e -> System.out.print(e + " "))
                .map(e -> e * 3).peek(e -> System.out.print(e + " ")).toArray();  // 1 3 2 6 3 9 
```

## 流的终止

将流转换为非流值供程序使用

### 简单约简

简单约简可以获取一个值

`java.util.stream.Stream 8`

1. `Long count()`：返回流中元素数量
2. `Optional<T> max(Comparator<? super T> comparator)`：通过比较器求流中最大值
3. `Optional<T> min(Comparator<? super T) comparator)`：通过比较器求流中最小值
4. `Optional<T> findFirst()`：获取流的第一个元素
5. `Optional<T> findAny()`：获取流中的任意一个元素



```java
// count
Long count = Stream.of("1", "2", "3").count();
System.out.println("count：" + count);   // 3

// max
Optional<Integer> max = Stream.of(3, 4, 2, 5).max(Integer::compareTo);
System.out.println("max：" + max.get()); //5

// findFirst 参数可选
Optional<String> findFirst = Stream.of("hello", "you", "need", "how")
        .findFirst();
System.out.println("findFirst：" + findFirst.get()   //hello
);

// findFirst 参数可选
Optional<String> findFirst = Stream.of("hello", "you", "need", "how")
        .findFirst();
System.out.println("findFirst：" + findFirst.get());   //hello

// findAny 查找任何一个元素,不能添加参数
Optional<String> findAny = Stream.of("1", "5", "3", "b")
        .findAny();
System.out.println("findAny：" + findAny.get());
```

判断是否符合条件

6. `boolean anyMatch(Predicate<? super T> predicate)`：有一个元素满足条件即返回 true
7. `boolean allMatch(Predicate<? super T> predicate)`：全部元素都满足条件即返回 true
8. `boolean noneMatch(Predicate<? super T> predicate)`：全部元素都不满足条件返回true

```java
// anyMatch
boolean anyMatch = Stream.of("a", "b", "a", "c").anyMatch(s -> s.equals("a"));
System.out.println("anyMatch：" + anyMatch);    // true

// allMatch
boolean allMatch = Stream.of("a", "b", "a", "c").allMatch(s -> s.equals("a"));
System.out.println("allMatch：" + allMatch);    // false

// noneMatch
boolean noneMatch = Stream.of("a", "b", "a", "c").noneMatch(s -> s.equals("a"));
System.out.println("noneMatch：" + noneMatch);  // false
```

### 转为集合

将流转为集合，或者对流进行遍历

forEach|iterator 遍历：

```java
// iterator()
Iterator iterator = Stream.of("a", "b", "c").iterator();
System.out.print("iterator：");
while (iterator.hasNext()) {
    System.out.print(iterator.next());  // abc
}

// forEach() 任意顺序，可以并行处理
Stream.of("3", "1", "2").forEach(e -> System.out.print(e)); // 312
// forEachOrdered() 按照流中的顺序处理，不能并行处理
Stream.of("3", "1", "2").forEachOrdered(e -> System.out.print(e));  //312
```

toArray 转为数组，可以不加参数转为 Object[] 类型

```java
String[] strArray = Stream.of("1", "2", "3").toArray(String[]::new);
Object[] intArray = Stream.of(1, 2, 3).toArray();
```

collect 转为集合

导入 `java.uti1.stream.collectors.*` 可以省略 `Collectors` 会使表达式更容易阅读。

```java
// Collectors.toSet/toGet，接收一个 Collector 接口的实例 还有 toSet 等。。
List<String> list = Stream.of("a", "b", "d").collect(Collectors.toList());
list.forEach(System.out::print);    // abd

// Collectors.toCollection() 指定集合类型
TreeSet<String> treeSet = Stream.of("h", "e", "l", "l", "o").collect(Collectors.toCollection(TreeSet::new));
treeSet.forEach(System.out::print); // ehlo

// Collectors.joining() 无参数直接连接；可以指定连接串；也可以给第一个元素前和最后一个元素后添加内容
String joining1 = Stream.of("h", "e", "l", "l", "o")
    .collect(Collectors.joining());// h*e*l*l*o
String joining2 = Stream.of("h", "e", "l", "l", "o")
    .collect(Collectors.joining("*"));// h*e*l*l*o
String joining3 = Stream.of("h", "e", "l", "l", "o")
    .collect(Collectors.joining("|", "&", "%"));// &h|e|l|l|o%

// collect 简约为 总和、平均值、最大值、最小值、总数量
IntSummaryStatistics summary = Stream.of("1", "22", "333")
    .collect(Collectors.summarizingInt(String::length));
System.out.println("Average：" + summary.getAverage()); // 2.0
System.out.println("Max：" + summary.getMax()); // 3
System.out.println("Min：" + summary.getMin()); // 1
```

### 收集到映射表

返回一个 Map 

```java
public static class Person {
        private int id;
        private String name;

        public Person(int id, String name) {
            this.id = id;
            this.name = name;
        }
        
        // getter/setter/toString...
```

```java
// 将两两个属性值做为 key,value
Map<Integer, String> map = getPersonStream().collect(Collectors
        .toMap(Person::getId, Person::getName)
);

// 将本身作为 value
Map<Integer, Person> mapIdentity = getPersonStream().collect(Collectors
        .toMap(Person::getId, Function.identity())
);

// key 值重复时会抛出异常，可以添加第 3 个参数处理,和分组有点类似
Map<Integer, String> map1 = personStream.collect(Collectors
        .toMap(Person::getId,
                Person::getName,
                (existValue, newValue) -> existValue + "," + newValue
        )
);

// 获取 TreeMap，添加第 4 个参数
TreeMap<Integer, String> treeMap = treeMapStream.collect(Collectors
        .toMap(Person::getId,
                Person::getName,
                (existValue, newValue) -> existValue + "," + newValue,
                TreeMap::new
        )
);
```

### 分组/分区

分组 `Collectors.groupBy`
```java
Map<String, List<Person>> groupByMap = Stream.of(
        new Person(1, "张三"),
        new Person(2, "李四"),
        new Person(3, "王五"),
        new Person(2, "老六"))
        .collect(Collectors
                .groupingBy(e -> e.id > 1 ? "2" : "1")
        );
//        key：1
//        id：1; name：张三
//        key：2
//        id：2; name：李四
//        id：3; name：王五
//        id：2; name：老六
```

`Collectors.partitioningBy()` 传入一个判断条件，将结果分为两组，key 分别是 true 和 false

```java
Map<Boolean, List<Person>> partitioningByMap = Stream.of(
                new Person(1, "张三"),
                new Person(2, "李四"),
                new Person(3, "王五"),
                new Person(4, "老六"))
                .collect(Collectors
                        .partitioningBy(e -> e.id > 2)
                );
//        key：false
//        id：1; name：张三
//        id：2; name：李四
//        key：true
//        id：3; name：王五
//        id：4; name：老六
```

### 下游收集器

groupingBy 的方法产生的结果为 `Map<String, List<T>>`,对 Map 的值进行处理就是下游收集器

```java
/**
 * 下游收集器
 */
//  Collectors.toSet 修改下游元素类型
Map<Integer, Set<Person>> setMap = getPersonStream().collect(
        Collectors.groupingBy(p -> p.id, Collectors.toSet())
);

// Collectors.counting 计算下游元素的数量
Map<Integer, Long> countingMap = getPersonStream().collect(
        Collectors.groupingBy(p -> p.id, Collectors.counting())
);

// Collectors.summingInt 对下游元素求和
Map<Integer, Integer> summingInt = getPersonStream().collect(
        Collectors.groupingBy(p -> p.id, Collectors.summingInt(Person::getId))
);

// Collectors.maxBy 只取下游元素的最大|小值
Map<Object, Optional<Person>> maxBy = getPersonStream().collect(
        Collectors.groupingBy(p -> p.id, Collectors.maxBy(Comparator.comparing(Person::getName)))
);

// Collector.mapping 将函数应用到下游元素，并将函数的返回值应用的下一个收集器
getPersonStream().collect(
        Collectors.groupingBy(p -> p.id,
                Collectors.mapping(Person::getName, Collectors.counting()))
);

// Collectors.summarizingInt 将下游元素收集到汇总统计对象中
Map<Integer, IntSummaryStatistics> summarizingInt = getPersonStream().collect(
        Collectors.groupingBy(p -> p.id, Collectors.summarizingInt(Person::getId))
);
```

### 约简操作

如果没有幺元，total 开始则为空

## Optional<T> 类型

包装器对象

### 创建 Optional 值

```java
// Optional.empty() 创建空值
Optional optionalEmpty = Optional.empty();
System.out.println(optionalEmpty == null);  // false

// Optional.of()   给定一个 Optional 值,如果参数为 null 则抛出空指针异常
Optional optionalOf = Optional.of("hello");
System.out.println(optionalOf.get());   // hello

// Optional.ofNullable，允许参数为 null 时返回 Optional.empty
Optional optionalOfNullable = Optional.ofNullable(null);
System.out.println(optionalOfNullable == optionalEmpty);    // true
```

### 正确的使用方式

值为空时，Optional 对象的 value 属性值为 null，该对象不为 null，可以通过以下 3 个方法使用这个 Optioanl 对象。

```java
// 当 Optional 值为空
Optional<String> optionalEmpty = Optional.empty();
System.out.println(optionalEmpty);  // Optional.empty

// orElse 使用默认值
System.out.println(optionalEmpty.orElse("123")); // 123
System.out.println(Optional.of("你好").orElse("123"));   // 你好

// orElseGet 使用代码计算默认值
System.out.println(optionalEmpty.orElseGet(() -> "fun")); //fun

// orElseThrow 抛出异常
try {
    optionalEmpty.orElseThrow(IllegalAccessError::new);
} catch (IllegalAccessError illegalAccessError) {
    System.out.println(true);   // true
}
```

当值不为空时，操作 Optional 对象

```java
// 当 Optional 值不为空时才消费该值
        Optional<String> optionalString = Optional.of("hello");

// 不为空时执行函数,但是不能获取函数的返回结果；为空时不执行该函数
optionalEmpty.ifPresent(s -> System.out.println(s + " word"));   // 未执行，没有输出
optionalString.ifPresent(s -> System.out.println(s + " word"));  // hello word

// 不为空时执行函数，并且可以获取函数的返回结果；如果为空时返回 Optional.empty
// map 只会对整个集合操作一次，而不是像在流中依次处理集合的每个元素
System.out.println(optionalString.map(s -> s)); // Optional[hello]
System.out.println(optionalEmpty.map(s -> {
        System.out.println(s);
        return s;
    }));    // Optional.empty

```

### 不推荐的使用方式

```java
Optional<String> optionalString = Optional.of("hello");

// get，获取值或当值为空时会抛出异常；应该使用 orElse/orElseGet/orElseThrow,考虑值为空时使用默认值
optionalString.get();

// isPresent 判断是否为空值；应该使用 ifPresent/map 方法
if (optionalString.isPresent()) {
    System.out.println("不推荐的使用方式");
}
```

### flatMap 管道操作

假设你有一个可以产生 `Optional<T>` 对象的方法 getOptionalT，并且目标类型 T 具有一个可以产生 `Optional<U>` 对象的方法 getOptionalU 。如果它们都是普通的方法，那么你可以通过调用 s.getOptionalT().getOptionalU() 来将它们组合起来。但是这种组合没法工作,因为 s.getOptionalT() 的类型为 `Optional<T>`，而不是 T。

很明显,如果有更多的可以产生 Optional 值的方法或 Lambda 表达式,那么就可以重复此过程。你可以直接将对 flatMap 的调用链接起来，从而构建由这些步骤构成的管道,只有所有步骤都成功时，该管道才会成功。

flatMap 的返回值是一个 Optional 对象，主要用处是形成管道操作

```
/**
 * 使用 flatMap 构建 Optional 值的函数
 */
public static void userFlatMap() {
    // getOptionalT().flatMap(T::getStringU); 错误
    Optional<String> U = getOptionalT().flatMap(T::getOptionalU);
    System.out.println(U);  // Optional[hello]
}

public static Optional<T> getOptionalT() {
    return Optional.of(new T());
}

public static class T {
    public Optional<String> getOptionalU() {
        return Optional.of("hello");
    }

    public String getStringU() {
        return "hello";
    }
}
```

## 基本类型流

用来存储基本数据类型的流

- IntStream：short/char/byte/boolean
- LongStream：long
- DoubleStream：float

```java
/**
 * 创建 基础数据类型流
 */
// IntStream.of
IntStream intStream = IntStream.of(1, 2, 3);
// Arrays.stream
IntStream intStream1 = Arrays.stream(new int[]{1, 2, 3}, 1, 2);

// 生成步长为 1 的整数范围，区别在于是否包含最有一个值
IntStream intStream2 = IntStream.range(1, 5); // 1, 2, 3, 4
IntStream intStream3 = IntStream.rangeClosed(1, 5); // 1, 2, 3, 4, 5

// mapToInt 其它流转为 IntStream
IntStream intStream4 = Stream.of(1, 2, 3).mapToInt(e -> e);

// 生成由字符的 Unicode 编码机制的码元构造成的 IntStream
String sentence = "\uD835\uD046abc.";
IntStream codes = sentence.codePoints();// 55349, 53318, 97, 98, 99, 46
// 生成由字符的 UTF-16 编码机制的码元构造成的 IntStream
IntStream chars = sentence.chars();     // 55349, 53318, 97, 98, 99, 46

// new Random().ints()|doubles|longs
IntStream intStream5 = new Random().ints();
show("random.ints", intStream5);    // 1956771018, -736010820, -238800119, -852834990, 362126282, 968027442, -1268558055, 843840184, 732314106, -830666418, ...
```

通常,基本类型流上的方法与对象流上的方法类似。下面是最主要的差异:

- toArray方法会返回基本类型数组。
- 产生可选结果的方法会返回一个Optional Int,OptionalLong或Optiona1Double.这些类与Optional类类似,但是具有getAsInt, getAsLong和getAsDouble方法,而不是get方法。
- 具有返回总和、平均值、最大值和最小值的sum, average, max和min方法。对象流没有定义这些方法。
- summarystatistics方法会产生一个类型为IntSummarystatistics, LongSummarystatistics或DoubleSummarystatistics的对象,它们可以同时报告流的总和、平均值、最大值和最小值。

## 并行流

只要在终结方法执行时,流处于并行模式,那么所有的中间流操作都将被并行化。

并行流是针对操作而言的，不是说有并行流的类。

```java
// parallel 将任意流转为并行流
Stream<Integer> parallelStream = Stream.of(1, 2, 3).parallel();

// 从任何集合中获取流
List list = Arrays.asList(1, 2, 3);
Stream<Integer> parallelStream1 = list.parallelStream();
```