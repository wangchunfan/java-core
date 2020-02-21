package javacore.volume2.stream;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CreateStream {
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

        // 4
        // Files.lines() 返回包含文件所有行的流
        Path path = Paths.get("javacore-volume2/javacore-volume2-01stream/src/main/resources/test.text");
        try (Stream<String> FilesLines = Files.lines(path)) {
            show("FilesLines1", FilesLines); // hello, world, 我
        }

        try (Stream<String> FilesLines = Files.lines(path, StandardCharsets.ISO_8859_1)) {
            show("FilesLines2", FilesLines); // hello, world, æ
        }

        // 5
        // Collection.stream() Collection 接口的 stream 方法
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        show("Collection.stream", list.stream());  // a, b
    }

}
