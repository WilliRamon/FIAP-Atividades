import java.util.stream.Stream;

public class AprendendoFilter {

    public static void main(String[] args) {
        Double[] notas = {5.5 , 5.5, 7.3, 7.2};

        var aprovado = Stream.of(notas)
                .reduce((acumulador, nota) -> acumulador + nota)
                .map(somaDasNotas -> somaDasNotas/4)
                .filter(nota -> nota > 5)
                .isPresent();

        System.out.println("Você foi aprovado: " + (aprovado ? "Sim" : "Não"));
    }
}
