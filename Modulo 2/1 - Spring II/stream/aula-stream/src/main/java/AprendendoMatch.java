import java.util.stream.Stream;

public class AprendendoMatch {

    public static void main(String[] args) {
        Double[] notas = {5.5, 5.5, 7.3, 7.2};

        boolean aprovado = Stream.of(notas).noneMatch(nota -> nota >= 7);
        System.out.println("noneMatch - Teve aprovação: " + (aprovado ? "Sim" : "Não"));

        boolean aprovado2 = Stream.of(notas).allMatch(nota -> nota >= 7);
        System.out.println("allMatch - Teve aprovação: " + (aprovado2 ? "Sim" : "Não"));

        boolean aprovado3 = Stream.of(notas).anyMatch(nota -> nota >= 7);
        System.out.println("anyMatch - Teve aprovação: " + (aprovado3 ? "Sim" : "Não"));
    }
}
