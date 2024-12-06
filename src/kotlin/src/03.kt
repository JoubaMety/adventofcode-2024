import java.io.File
import kotlin.system.measureTimeMillis

object ANSI {
    const val RED = "\u001b[31m"
    const val GREEN = "\u001b[32m"
    const val YELLOW = "\u001b[33m"
    const val BLUE = "\u001B[34m"
    const val RESET = "\u001b[0m"
    const val GRAY = "\u001b[90m"
}

fun sumOfMulOperations(input: String): Int {
    return Regex("mul[(]\\d*,\\d*[)]")
        .findAll(input)
        .map { it.groupValues[0] }
        .sumOf { operation ->
            val (a, b) = operation
                .substring(4, operation.length - 1)
                .split(',')
                .map(String::toInt)
            a * b
        }
}

fun removeDisabledOperations(input: String): String {
    return input.replace(
        Regex("(?<=don't\\(\\)).*?(?=do\\(\\))|(?<=don't\\(\\)).*"),
        "")
}

fun main() {
    var input = ""
    var part01 = 0
    var part02 = 0
    val elapsedLoadFile = measureTimeMillis {
        input = File("./src/03.input.txt")
            .readText(Charsets.UTF_8)
    }
    val elapsedCleanedFile = measureTimeMillis {
        input = input.replace("\n", "")
    }
    val elapsedPart01 = measureTimeMillis {
        part01 = sumOfMulOperations(input)
    }
    val elapsedPart02 = measureTimeMillis {
        part02 = sumOfMulOperations(removeDisabledOperations(input))
    }

    val title = "Advent Of Code 2024 - Day 03"
    println(ANSI.GREEN + title + ANSI.RESET + "\n" +
            (ANSI.RED + "-" + ANSI.RESET).repeat(title.length))
    println("Loaded input  in " + ANSI.YELLOW + elapsedLoadFile + ANSI.RESET + " ms\n" +
            "Cleaned input in " + ANSI.YELLOW + elapsedCleanedFile + ANSI.RESET + " ms\n" +
            "First Part:  " + ANSI.BLUE + part01 + ANSI.RESET + " (" + ANSI.YELLOW + elapsedPart01 + ANSI.RESET + " ms\n" +
            "Second Part: " + ANSI.BLUE + part02 + ANSI.RESET + " (" + ANSI.YELLOW + elapsedPart02 + ANSI.RESET + " ms\n" +
            "Finished in " + ANSI.YELLOW + (elapsedLoadFile + elapsedPart01 + elapsedPart02 + elapsedCleanedFile) + ANSI.RESET + " ms")
}