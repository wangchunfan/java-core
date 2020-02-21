package javacore.volume2.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Transform {

    public static void main(String[] args) {
        Stream<String> stream = Stream.of("hello", "world", "how", "are", "you");
        show("stream", stream); // hello, world, how, are, you

        // 1
        Stream<String> streamFilter = Stream.of("hello", "world", "how", "are", "you");
        show("streamFilter", streamFilter.filter(s -> s.length() > 3)); // hello, world

        Stream<String> streamMap = Stream.of("hello", "world", "how", "are", "you");
        show("streamMap", streamMap.map(a -> a.substring(0, 1)));   // h, w, h, a, y

        Stream<String> flatMap = Stream.of("hello", "world", "how", "are", "you");
        show("flatMap", flatMap.flatMap(a -> letters(a)));    // h, e, l, l, o, w, o, r, l, d, ...

        // 2
        Stream<Double> limitStream = Stream.generate(Math::random).limit(3);
        show("limitStream", limitStream);   // 0.3758453005326531, 0.21778149147931358, 0.2985891860376019

        Stream<String> skipStream = Stream.of("a", "b", "c").skip(1);
        show("skipStream", skipStream); // b, c

        Stream<String> concatStream = Stream.concat(Stream.of("1", "2"), Stream.of("3", "4"));
        show("concatStream", concatStream); // 1, 2, 3, 4

        // 3
        Stream<String> distinctStream = Stream.of("1", "2", "1", "3");
        show("distinctStream", distinctStream);

        Stream<String> sortedStream = Stream.of("3", "1", "4").sorted();
        show("sortedStream", sortedStream); // 1, 3, 4

        System.out.println("peekStream:");
        Stream.of(1, 2, 3).peek(e -> System.out.print(e + " "))
                .map(e -> e * 3).peek(e -> System.out.print(e + " ")).toArray();  // 1 3 2 6 3 9


    }

    public static Stream<String> letters(String s) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < s.length(); i++) {
            result.add((s.substring(i, i + 1)));
        }
        return result.stream();
    }


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
}
