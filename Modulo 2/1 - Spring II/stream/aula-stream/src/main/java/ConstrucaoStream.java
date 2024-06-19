import java.util.stream.Stream;

public class ConstrucaoStream {
    public static void main(String[] args) {
        Stream<Number> numeros = Stream.of(10, 10, 9.6, 8.2, 9.7, 10);
        numeros.forEach(System.out::println);

        System.out.println("******** <hr/> ******");
        Number[] maisNotas = {7, 6.5, 7.2, 9};
        Stream.of(maisNotas).forEach(System.out::println);

        System.out.println("******** <hr/> ******");
        Stream.of(maisNotas).parallel().forEach(System.out::println);
    }
}
