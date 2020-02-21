package javacore.volume2.stream;

import java.sql.SQLOutput;
import java.util.Optional;

public class OptionalType {
    public static void main(String[] args) {
        createOptional();
        useOptionalCorrect();
        useOptionalError();
        userFlatMap();
    }

    /**
     * 创建 Optional 值
     */
    public static void createOptional() {
        // Optional.empty() 创建空值
        Optional optionalEmpty = Optional.empty();
        System.out.println(optionalEmpty == null);  // false

        // Optional.of()   给定一个 Optional 值,如果参数为 null 则抛出空指针异常
        Optional optionalOf = Optional.of("hello");
        System.out.println(optionalOf.get());   // hello

        // Optional.ofNullable，允许参数为 null 时返回 Optional.empty
        Optional optionalOfNullable = Optional.ofNullable(null);
        System.out.println(optionalOfNullable == optionalEmpty);    // true
    }

    /**
     * 正确使用 Optional 值
     */
    public static void useOptionalCorrect() {
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

        // 当 Optional 值不为空时才消费该值
        Optional<String> optionalString = Optional.of("hello");

        // ifPresent 不为空时执行函数,但是不能获取函数的返回结果
        optionalEmpty.ifPresent(s -> System.out.println(s + " word"));   // 未执行，没有输出
        optionalString.ifPresent(s -> System.out.println(s + " word"));  // hello word

        // map 不为空时执行函数，并且可以获取函数的返回结果；如果 Optional 值是一个集合，
        // map 只会对整个集合操作一次，而不是像在流中依次处理集合的每个元素
        System.out.println(optionalEmpty.map(s -> {
            System.out.println(s);
            return s;
        }));    // Optional.empty
        System.out.println(optionalString.map(s -> s)); // Optional[hello]
    }

    // 不推荐的方式
    public static void useOptionalError() {
        Optional<String> optionalString = Optional.of("hello");

        // get，获取值或当值为空时会抛出异常；应该使用 orElse/orElseGet/orElseThrow,考虑值为空时使用默认值
        optionalString.get();

        // isPresent 判断是否为空值；应该使用 ifPresent/map 方法
        if (optionalString.isPresent()) {
            System.out.println("不推荐的使用方式");
        }
    }

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

}