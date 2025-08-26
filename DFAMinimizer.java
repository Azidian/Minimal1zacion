import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.StringJoiner;

/**
 * Implementa el algoritmo de minimización de DFA de Kozen (Lectura 14)
 * para encontrar pares de estados equivalentes.
 * * El programa lee la descripción de uno o más DFAs desde la entrada estándar
 * y para cada uno, imprime los pares de estados equivalentes en orden lexicográfico.
 */
public class DFAMinimizer {

    /**
     * Clase interna para representar un par de estados.
     * Se implementa Comparable para facilitar el ordenamiento lexicográfico.
     */
    static class StatePair implements Comparable<StatePair> {
        final int state1;
        final int state2;

        public StatePair(int s1, int s2) {
            // Aseguramos que state1 siempre sea el menor para consistencia.
            this.state1 = Math.min(s1, s2);
            this.state2 = Math.max(s1, s2);
        }

        @Override
        public int compareTo(StatePair other) {
            if (this.state1 != other.state1) {
                return Integer.compare(this.state1, other.state1);
            }
            return Integer.compare(this.state2, other.state2);
        }

        @Override
        public String toString() {
            return "(" + state1 + ", " + state2 + ")";
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            StatePair other = (StatePair) obj;
            return this.state1 == other.state1 && this.state2 == other.state2;
        }

        @Override
        public int hashCode() {
            return 31 * state1 + state2;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            int numCases = Integer.parseInt(scanner.nextLine());

            for (int i = 0; i < numCases; i++) {
                solveCase(scanner);
            }
        } catch (Exception e) {
            System.err.println("Error procesando la entrada: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    /**
     * Resuelve un único caso de prueba de minimización de DFA.
     * @param scanner El objeto Scanner para leer la entrada.
     */
    private static void solveCase(Scanner scanner) {
        // 1. Leer la definición del DFA
        int n = Integer.parseInt(scanner.nextLine()); // Número de estados
        String[] alphabet = scanner.nextLine().split("\\s+");
        
        Set<Integer> finalStates = new HashSet<>();
        String finalStatesLine = scanner.nextLine();
        if (!finalStatesLine.trim().isEmpty()) {
            String[] finalStatesStr = finalStatesLine.split("\\s+");
            for (String s : finalStatesStr) {
                finalStates.add(Integer.parseInt(s));
            }
        }

        int[][] transitions = new int[n][alphabet.length];
        for (int j = 0; j < n; j++) {
            String[] row = scanner.nextLine().split("\\s+");
            for (int k = 0; k < alphabet.length; k++) {
                transitions[j][k] = Integer.parseInt(row[k]);
            }
        }

        // 2. Implementación del Algoritmo
        
        // La tabla 'marked' almacenará los pares de estados no equivalentes.
        // Usamos una matriz triangular (marked[p][q] con p > q).
        boolean[][] marked = new boolean[n][n];

        // Paso 2: Marcar pares donde un estado es final y el otro no.
        for (int p = 0; p < n; p++) {
            for (int q = 0; q < p; q++) {
                boolean pIsFinal = finalStates.contains(p);
                boolean qIsFinal = finalStates.contains(q);
                if (pIsFinal != qIsFinal) {
                    marked[p][q] = true;
                }
            }
        }

        // Paso 3: Marcar iterativamente hasta que no haya más cambios.
        boolean changedInPass;
        do {
            changedInPass = false;
            for (int p = 0; p < n; p++) {
                for (int q = 0; q < p; q++) {
                    // Si el par {p, q} aún no está marcado, verificar si debe marcarse.
                    if (!marked[p][q]) {
                        for (int k = 0; k < alphabet.length; k++) {
                            int pNext = transitions[p][k];
                            int qNext = transitions[q][k];

                            // Asegurar el orden para consultar la matriz triangular.
                            int r = Math.max(pNext, qNext);
                            int s = Math.min(pNext, qNext);

                            // Si los estados destino son diferentes y su par está marcado...
                            if (r != s && marked[r][s]) {
                                marked[p][q] = true; // ...entonces marcamos el par actual.
                                changedInPass = true;
                                break; // No es necesario seguir probando con otros símbolos.
                            }
                        }
                    }
                }
            }
        } while (changedInPass);

        // 3. Recolectar y ordenar los resultados
        List<StatePair> equivalentPairs = new ArrayList<>();
        for (int p = 0; p < n; p++) {
            for (int q = 0; q < p; q++) {
                // Los pares que no fueron marcados son equivalentes.
                if (!marked[p][q]) {
                    equivalentPairs.add(new StatePair(q, p));
                }
            }
        }
        Collections.sort(equivalentPairs);

        // 4. Imprimir la salida en el formato requerido
        StringJoiner sj = new StringJoiner(" ");
        for (StatePair pair : equivalentPairs) {
            sj.add(pair.toString());
        }
        System.out.println(sj.toString());
    }
}
