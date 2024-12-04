import java.io.File
import kotlin.system.measureTimeMillis

fun part01(input: String) : Int {
    val lines = input.trim().lines()
    var amount = 0
    for ((lineIndex, line) in lines.withIndex()) {
        for ((charIndex, char) in line.withIndex()) {
            if (char == 'X') {
                // horizontal forwards
                if (charIndex + 3 < line.length) {
                    if(line[charIndex + 1] == 'M' &&
                       line[charIndex + 2] == 'A' &&
                       line[charIndex + 3] == 'S')
                    amount++
                }
                // horizontal backwards
                if(charIndex - 3 >= 0) {
                    if(line[charIndex - 1] == 'M' &&
                        line[charIndex - 2] == 'A' &&
                        line[charIndex - 3] == 'S')
                        amount++
                }
                // vertical downwards
                if(lineIndex + 3 < lines.size) {
                    if(lines[lineIndex + 1][charIndex] == 'M' &&
                       lines[lineIndex + 2][charIndex] == 'A' &&
                       lines[lineIndex + 3][charIndex] == 'S')
                        amount++
                }
                // vertical upwards
                if(lineIndex - 3 >= 0) {
                    if(lines[lineIndex - 1][charIndex] == 'M' &&
                        lines[lineIndex - 2][charIndex] == 'A' &&
                        lines[lineIndex - 3][charIndex] == 'S')
                        amount++
                }
                // diagonal downwards-backwards (bottom-left)
                if(lineIndex + 3 < lines.size && charIndex - 3 >= 0) {
                    if(lines[lineIndex + 1][charIndex - 1] == 'M' &&
                        lines[lineIndex + 2][charIndex - 2] == 'A' &&
                        lines[lineIndex + 3][charIndex - 3] == 'S')
                        amount++
                }
                // diagonal downwards-forwards (bottom-right)
                if(lineIndex + 3 < lines.size && charIndex + 3 < line.length) {
                    if(lines[lineIndex + 1][charIndex + 1] == 'M' &&
                        lines[lineIndex + 2][charIndex + 2] == 'A' &&
                        lines[lineIndex + 3][charIndex + 3] == 'S')
                        amount++
                }
                // diagonal upwards-forwards (top-right)
                if(lineIndex - 3 >= 0 && charIndex + 3 < line.length) {
                    if(lines[lineIndex - 1][charIndex + 1] == 'M' &&
                        lines[lineIndex - 2][charIndex + 2] == 'A' &&
                        lines[lineIndex - 3][charIndex + 3] == 'S')
                        amount++
                }
                // diagonal upwards-backwards (top-left)
                if(lineIndex - 3 >= 0 && charIndex - 3 >= 0) {
                    if(lines[lineIndex - 1][charIndex - 1] == 'M' &&
                        lines[lineIndex - 2][charIndex - 2] == 'A' &&
                        lines[lineIndex - 3][charIndex - 3] == 'S')
                        amount++
                }
            }
        }
    }
    return amount
}

fun part02(input: String) : Int {
    val lines = input.trim().lines()
    var amount = 0
    for ((lineIndex, line) in lines.withIndex()) {
        for ((charIndex, char) in line.withIndex()) {
            if (char == 'A' &&
                lineIndex - 1 >= 0 && lineIndex + 1 < lines.size &&
                charIndex - 1 >= 0 && charIndex + 1 < line.length
                ) {
                if(
                    ((lines[lineIndex-1][charIndex-1] == 'M') && (lines[lineIndex+1][charIndex-1] == 'M') && (lines[lineIndex-1][charIndex+1] == 'S') && (lines[lineIndex+1][charIndex+1] == 'S')) ||
                    ((lines[lineIndex-1][charIndex-1] == 'M') && (lines[lineIndex+1][charIndex-1] == 'S') && (lines[lineIndex-1][charIndex+1] == 'M') && (lines[lineIndex+1][charIndex+1] == 'S')) ||
                    ((lines[lineIndex-1][charIndex-1] == 'S') && (lines[lineIndex+1][charIndex-1] == 'S') && (lines[lineIndex-1][charIndex+1] == 'M') && (lines[lineIndex+1][charIndex+1] == 'M')) ||
                    ((lines[lineIndex-1][charIndex-1] == 'S') && (lines[lineIndex+1][charIndex-1] == 'M') && (lines[lineIndex-1][charIndex+1] == 'S') && (lines[lineIndex+1][charIndex+1] == 'M'))
                    ) {
                    amount++
                }
            }
        }
    }
    return amount
}

fun main() {
    var input = ""
    var part01 = 0
    var part02 = 0
    val elapsedLoadFile = measureTimeMillis {
        input = File("./src/04.input.txt")
            .readText(Charsets.UTF_8)
    }
    val elapsedPart01 = measureTimeMillis {
        part01 = part01(input)
    }
    val elapsedPart02 = measureTimeMillis {
        part02 = part02(input)
    }

    val title = "Advent Of Code 2024 - Day 04"
    println(ANSI.GREEN + title + ANSI.RESET + "\n" +
            (ANSI.RED + "-" + ANSI.RESET).repeat(title.length))
    println("Loaded input  in " + ANSI.YELLOW + elapsedLoadFile + ANSI.RESET + " ms\n" +
            "First Part:  " + ANSI.BLUE + part01 + ANSI.RESET + " (" + ANSI.YELLOW + elapsedPart01 + ANSI.RESET + " ms)\n" +
            "Second Part: " + ANSI.BLUE + part02 + ANSI.RESET + " (" + ANSI.YELLOW + elapsedPart02 + ANSI.RESET + " ms)\n" +
            "Finished in " + ANSI.YELLOW + (elapsedLoadFile + elapsedPart01 + elapsedPart02) + ANSI.RESET + " ms")
}