# Tarea 1: Minimizador de Autómatas Finitos Deterministas

Implementación del algoritmo de minimización de DFA para encontrar estados equivalentes.

## Información del Estudiante

* **Nombre Completo:** [Tu Nombre Completo Aquí]
* **Número de Clase:** [Tu Número de Clase Aquí]

## Versiones y Entorno de Desarrollo

* **Sistema Operativo:** [Ej: Windows 11, macOS Sonoma, Ubuntu 22.04]
* **Lenguaje de Programación:** Java
* **Kit de Desarrollo de Java (JDK):** [Ej: Oracle JDK 17.0.5, OpenJDK 21]
* **Herramientas:** [Ej: Visual Studio Code con Extension Pack for Java, Eclipse IDE, IntelliJ IDEA]

## Instrucciones para Ejecución

El programa está diseñado para ser compilado y ejecutado desde la línea de comandos, leyendo la entrada desde un archivo o directamente desde la terminal.

### 1. Compilación

Navega hasta el directorio que contiene el archivo `DFAMinimizer.java` y compílalo con el siguiente comando:

```bash
javac DFAMinimizer.java
```

### 2. Ejecución

Una vez compilado, puedes ejecutar el programa. Él esperará la entrada desde la consola.

```bash
java DFAMinimizer
```

Puedes copiar y pegar el contenido de un archivo de prueba (como el `input.txt` del ejemplo) directamente en la terminal y presionar Enter.

#### Ejecución con Redirección de Archivos (Recomendado)

Es más conveniente usar la redirección de entrada para alimentar el programa con un archivo de prueba.

1.  Crea un archivo de texto, por ejemplo, `input.txt`.
2.  Pega el contenido del caso de prueba en `input.txt`.
3.  Ejecuta el siguiente comando:

```bash
java DFAMinimizer < input.txt
```

El programa procesará el archivo y mostrará la salida directamente en la consola. Para guardar la salida en un archivo `output.txt`, usa:

```bash
java DFAMinimizer < input.txt > output.txt
```

## Explicación del Algoritmo

Este programa implementa el algoritmo de minimización de DFA presentado en **Kozen 1997, Lectura 14**. El objetivo es encontrar todos los pares de **estados equivalentes** en un DFA sin estados inaccesibles.

El algoritmo funciona por un principio de "marcado": en lugar de buscar directamente los estados equivalentes, identifica y marca todos los pares de estados que son **no equivalentes** (o distinguibles). Aquellos pares que permanezcan sin marcar al final del proceso son, por definición, equivalentes.

Los pasos son los siguientes:

1.  **Inicialización:** Se crea una tabla para todos los pares de estados distintos `{p, q}`. Inicialmente, ningún par está marcado.

2.  **Paso Base:** Se marcan todos los pares `{p, q}` en los que un estado es de aceptación (final) y el otro no. Estos son claramente distinguibles, ya que la cadena vacía (`ε`) es aceptada por uno pero no por el otro.

3.  **Paso Iterativo:** Se repite el siguiente proceso hasta que no se puedan hacer más marcas en una pasada completa:
    * Para cada par no marcado `{p, q}` y para cada símbolo `a` del alfabeto, se observan los estados a los que transitan: `p' = δ(p, a)` y `q' = δ(q, a)`.
    * Si el par `{p', q'}` ya ha sido marcado como no equivalente, entonces el par original `{p, q}` también debe ser marcado. Esto se debe a que hemos encontrado una cadena (el símbolo `a` seguido de la cadena que distingue a `p'` y `q'`) que distingue a `p` y `q`.

4.  **Resultado Final:** Una vez que el proceso se estabiliza, la lista de pares de estados equivalentes está formada por todos los pares que **no fueron marcados**. El programa recolecta estos pares y los imprime en orden lexicográfico, como se especifica en los requisitos de la tarea.
