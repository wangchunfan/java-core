package javacore.volume2.stream;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TerminalOperation {
    public static void main(String[] args) {

        /**
         * 简单简约
         */

        // count
        Long count = Stream.of("1", "2", "3").count();
        System.out.println("count：" + count);   // 3
        // max 最大的元素
        Optional<Integer> max = Stream.of(3, 4, 2, 5).max(Integer::compareTo);
        System.out.println("max：" + max.get()); //5
        Optional<Integer> min = Stream.of(3, 4, 2, 5).min(Integer::compareTo);
        System.out.println("min:" + min.get());
        // findFirst 参数可选
        Optional<String> findFirst = Stream.of("hello", "you", "need", "how")
                .findFirst();
        System.out.println("findFirst：" + findFirst.get());   //hello
        // findAny 查找任何一个元素,不能添加参数
        Optional<String> findAny = Stream.of("1", "5", "3", "b")
                .findAny();
        System.out.println("findAny：" + findAny.get());

        /**
         * 判断是否符合条件
         */
        // anyMatch
        boolean anyMatch = Stream.of("a", "b", "a", "c").anyMatch(s -> s.equals("a"));
        System.out.println("anyMatch：" + anyMatch); // true
        // allMatch
        boolean allMatch = Stream.of("a", "b", "a", "c").allMatch(s -> s.equals("a"));
        System.out.println("allMatch：" + allMatch); // false
        // noneMatch
        boolean noneMatch = Stream.of("a", "b", "a", "c").noneMatch(s -> s.equals("a"));
        System.out.println("noneMatch：" + noneMatch);   // false

        /**
         * 遍历
         */

        // iterator()
        Iterator iterator = Stream.of("a", "b", "c").iterator();
        System.out.print("iterator：");
        while (iterator.hasNext()) {
            System.out.print(iterator.next());  // abc
        }
        System.out.println();
        // forEach() 任意顺序，可以并行处理
        System.out.print("forEach：");
        Stream.of("3", "1", "2").forEach(e -> System.out.print(e)); // 312
        // forEachOrdered() 按照流中的顺序处理，不能并行处理
        System.out.print("\nforEachOrdered：");
        Stream.of("3", "1", "2").forEachOrdered(e -> System.out.print(e));  //312

        /**
         * toArray 转为数组，可以不加参数
         */
        System.out.println("\n转为数组：");
        String[] strArray = Stream.of("1", "2", "3").toArray(String[]::new);
        Object[] intArray = Stream.of(1, 2, 3).toArray();

        /**
         * collect 转为集合
         * stream.collect 生成 Collection
         * 取决于 collect() 中的参数，需要了解 Collectors 的静态方法
         */
        // Collectors.toSet/toGet，接收一个 Collector 接口的实例 还有 toSet 等。。
        System.out.print("Collectors.toList()：");
        List<String> list = Stream.of("a", "b", "d").collect(Collectors.toList());
        list.forEach(System.out::print);    // abd
        // Collectors.toCollection() 指定集合类型
        System.out.print("\nCollectors.toCollection()：");
        TreeSet<String> treeSet = Stream.of("h", "e", "l", "l", "o").collect(Collectors.toCollection(TreeSet::new));
        treeSet.forEach(System.out::print); // ehlo
        // Collectors.joining() 无参数直接连接；可以指定连接串，也给第一个元素前或最后一个元素后添加内容
        System.out.print("\nCollectors.joining()：");
        String joining1 = Stream.of("h", "e", "l", "l", "o").collect(Collectors.joining());
        String joining2 = Stream.of("h", "e", "l", "l", "o").collect(Collectors.joining("*"));
        String joining3 = Stream.of("h", "e", "l", "l", "o").collect(Collectors.joining("|", "&", "%"));
        System.out.print(joining1);   // h*e*l*l*o
        System.out.print(joining2);   // h*e*l*l*o
        System.out.print(joining3);   // &h|e|l|l|o%

        /**
         * collect 简约为 总和、平均值、最大值、最小值
         * Collectors.summarizingInt|Long|Double
         */
        System.out.println("\nCollectors.summarizingInt|Long|Double：");
        IntSummaryStatistics summary = Stream.of("1", "22", "333").collect(Collectors.summarizingInt(String::length));
        System.out.println("Average：" + summary.getAverage());  // 2.0
        System.out.println("Max：" + summary.getMax());  // 3
        System.out.println("Min：" + summary.getMin());  // 1
        System.out.println("Count：" + summary.getCount());


        System.out.println();
        /**
         * collect 收集到映射表
         * Collectors.toMap
         */
        // 将两两个属性值做为 key,value
        System.out.println("map：");
        Map<Integer, String> map = getPersonStream().collect(Collectors
                .toMap(Person::getId, Person::getName)
        );
        for (Integer i : map.keySet()) {
            System.out.println("key:" + i + ";name:" + map.get(i));
        }

        // 将本身作为 value
        System.out.println("mapIdentity：");
        Map<Integer, Person> mapIdentity = getPersonStream().collect(Collectors
                .toMap(Person::getId, Function.identity())
        );
        for (Integer i : mapIdentity.keySet()) {
            System.out.println(mapIdentity.get(i));
        }

        // key 值重复时会抛出异常，可以添加第 3个参数处理,相当于分组
        System.out.println("personStream：");
        Stream<Person> personStream = Stream.of(
                new Person(1, "张三"),
                new Person(2, "李四"),
                new Person(3, "王五"),
                new Person(2, "老六"));
        Map<Integer, String> map1 = personStream.collect(Collectors
                .toMap(Person::getId,
                        Person::getName,
                        (existValue, newValue) -> existValue + "," + newValue
                )
        );
        for (Integer i : map1.keySet()) {
            System.out.println("key:" + i + ";name:" + map1.get(i));
        }

        // 获取 TreeMap，添加第 4 个参数
        System.out.println("TreeMap：");
        Stream<Person> treeMapStream = Stream.of(
                new Person(1, "张三"),
                new Person(2, "李四"),
                new Person(3, "王五"),
                new Person(2, "老六"));
        TreeMap<Integer, String> treeMap = treeMapStream.collect(Collectors
                .toMap(Person::getId,
                        Person::getName,
                        (existValue, newValue) -> existValue + "," + newValue,
                        TreeMap::new
                )
        );
        for (Integer i : treeMap.keySet()) {
            System.out.println("key:" + i + ";name:" + treeMap.get(i));
        }

        /**
         * collect 分组分区
         */
        // Collectors.groupBy
        System.out.println("groupBy：");
        Map<String, List<Person>> groupByMap = Stream.of(
                new Person(1, "张三"),
                new Person(2, "李四"),
                new Person(3, "王五"),
                new Person(2, "老六"))
                .collect(Collectors
                        .groupingBy(e -> e.id > 1 ? "2" : "1")
                );
        for (String key : groupByMap.keySet()) {
            List li = groupByMap.get(key);
            System.out.println("key：" + key);
            li.forEach(System.out::println);
        }
//        key：1
//        id：1; name：张三
//        key：2
//        id：2; name：李四
//        id：3; name：王五
//        id：2; name：老六

        // Collectors.partitioningBy
        System.out.println("partitioningBy：");
        Map<Boolean, List<Person>> partitioningByMap = Stream.of(
                new Person(1, "张三"),
                new Person(2, "李四"),
                new Person(3, "王五"),
                new Person(4, "老六"))
                .collect(Collectors
                        .partitioningBy(e -> e.id > 2)
                );
        for (Boolean key : partitioningByMap.keySet()) {
            List li = partitioningByMap.get(key);
            System.out.println("key：" + key);
            li.forEach(System.out::println);
        }
//        key：false
//        id：1; name：张三
//        id：2; name：李四
//        key：true
//        id：3; name：王五
//        id：4; name：老六

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

        /**
         *  约简操作
         */
        // reduce 计算所有元素的和 v0 + v1 + v2 ..以下两种方式是一样的，流为空，返回一个空的 Optional
        Optional<Integer> reduce = Stream.of(1, 2, 3, 4).reduce((total, ele) -> total + ele);
        Optional<Integer> reduce1 = Stream.of(1, 2, 3, 4).reduce(Integer::sum);
        // 增加一个幺元 计算 100 + v0 + v1... 注意返回值不再是 Optional
        Integer reduce2 = Stream.of(1, 2, 3, 4).reduce(100, (x, y) -> x + y);
        // “累积器” 处理并行化
        Integer reduce3 = Stream.of("hello", "me").reduce(
                10,
                (total, ele) -> total + ele.length(),
                (total1, total2) -> total1 + total2
        );

        // collect 进行位操作
        BitSet result = Stream.of(1, 2, 3).collect(BitSet::new, BitSet::set, BitSet::or);
    }

    public static Stream<Person> getPersonStream() {
        return Stream.of(
                new Person(1, "张三"),
                new Person(2, "李四"),
                new Person(3, "王五"));

    }


    public static class Person {
        private int id;
        private String name;

        public Person(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "id：" + id + "; name：" + name;
        }

    }


}
