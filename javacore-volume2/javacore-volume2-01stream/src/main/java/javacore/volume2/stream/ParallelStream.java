package javacore.volume2.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ParallelStream {
    public static void main(String[] args) {

        // parallel 将任意流转为并行流
        Stream<Integer> parallelStream = Stream.of(1, 2, 3).parallel();

        // 从任何集合中获取流
        List list = Arrays.asList(1, 2, 3);
        Stream<Integer> parallelStream1 = list.parallelStream();
    }
}
