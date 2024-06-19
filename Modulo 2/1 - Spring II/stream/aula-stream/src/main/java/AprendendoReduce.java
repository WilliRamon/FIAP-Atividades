import java.util.stream.Stream;

public class AprendendoReduce {

    public static void main(String[] args) {
        Double[] notas = {5.5, 5.5, 7.3, 7.2};
        Stream.of(notas)
                .filter(nota -> nota >= 7)
                .map(nota -> "Você foi aprovado com nota: " + nota)
                .forEach(System.out::println);
    }
}
