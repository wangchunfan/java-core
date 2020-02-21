package javacore.volume2.stream;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class BaseTypeStream {
    public static void show(String title, IntStream stream) {
        final int SIZE = 10;
        int[] firstElements = stream.limit(SIZE + 1).toArray();
        System.out.println(title + ":");
        for (int i = 0; i < firstElements.length; i++) {
            if (i > 0) System.out.print(", ");
            if (i < SIZE) System.out.print(firstElements[i]);
            else System.out.print("...");
        }
        System.out.println("\n");
    }

    public static void main(String[] args) {
        /**
         * 创建 基础数据类型流
         */
        // IntStream.of
        IntStream intStream = IntStream.of(1, 2, 3);
        // Arrays.stream
        IntStream intStream1 = Arrays.stream(new int[]{1, 2, 3}, 1, 2);

        // 生成步长为 1 的整数范围，区别在于是否包含最有一个值
        IntStream intStream2 = IntStream.range(1, 5);
        IntStream intStream3 = IntStream.rangeClosed(1, 5);
        show("intStream2", intStream2); // 1, 2, 3, 4
        show("intStream3", intStream3); // 1, 2, 3, 4, 5

        // mapToInt 其它流转为 IntStream
        IntStream intStream4 = Stream.of(1, 2, 3).mapToInt(e -> e);

        // 生成由字符的 Unicode 码或由 UTF-16 编码机制的码元构造成的 IntStream
        String sentence = "\uD835\uD046abc.";
        IntStream codes = sentence.codePoints();
        show("codePoints", codes);  // 55349, 53318, 97, 98, 99, 46
        IntStream chars = sentence.chars();
        show("chars", chars);   // 55349, 53318, 97, 98, 99, 46

        IntStream intStream5 = new Random().ints();
        show("random.ints", intStream5);    // 1956771018, -736010820, -238800119, -852834990, 362126282, 968027442, -1268558055, 843840184, 732314106, -830666418, ...
    }
}
