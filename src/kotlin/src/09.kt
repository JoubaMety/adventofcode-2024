import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.system.measureNanoTime

fun day09_convertInput(input: Path): List<Int> {
    return input.readLines().first().map { it.digitToInt() }
}

fun day09_part01(input: List<Int>): ULong {
    val mutableList = mutableListOf<Int?>()
    var fileID = 0
    // populate mutableList
    for((i, num) in input.withIndex()) {
        if(i % 2 == 0) {
            if(num != 0) {
                for (j in 0..<num) {
                    mutableList.add(fileID)
                }
                fileID++
            }
        } else {
            for(j in 0..<num) {
                mutableList.add(null)
            }
        }

    }
    // move files
    a@for((pos, block) in mutableList.withIndex()) {
        if(block == null) {
            for(j in mutableList.size-1 downTo pos+1) {
                if(mutableList[j] != null) {
                    mutableList[pos] = mutableList[j]
                    mutableList[j] = null
                    continue@a
                }
            }
            break
        }
    }
    // checksum
    var checksum = 0uL
    for((pos, num) in mutableList.withIndex()) {
        if(num == null)
            continue
        checksum += pos.toULong() * num.toULong()
    }
    return checksum
}

fun day09_part02(input: List<Int>): ULong {
    val mutableList = mutableListOf<Int?>()
    var fileID = 0
    // populate mutableList
    for((i, num) in input.withIndex()) {
        if(i % 2 == 0) {
            if(num != 0) {
                for (j in 0..<num) {
                    mutableList.add(fileID)
                }
                fileID++
            }
        } else {
            for(j in 0..<num) {
                mutableList.add(null)
            }
        }

    }
    // move files
    val numberPairs = mutableListOf<Pair<Int, Int>>()
    b@for(fileEndPos in mutableList.size-1 downTo 0) {
        if(mutableList[fileEndPos] != null) {
            for(pair in numberPairs) {
                if(pair.first <= fileEndPos && fileEndPos <= pair.second)
                    continue@b
            }
            var fileStartPos = fileEndPos
            for(j in fileEndPos-1 downTo 0)
                if(mutableList[j] == mutableList[fileEndPos])
                    fileStartPos--
                else
                    break
            numberPairs.add(Pair(fileStartPos, fileEndPos))
            val fileSpace = (fileEndPos - fileStartPos)
            val nullPairs = mutableListOf<Pair<Int, Int>>()
            a@for(i in 0..<fileStartPos) {
                if(mutableList[i] == null) {
                    for(pair in nullPairs) {
                        if(pair.first <= i && i <= pair.second)
                            continue@a
                    }
                    var freeSpace = 0
                    for(j in i+1..<mutableList.size) {
                        if (mutableList[j] == null)
                            freeSpace++
                        else
                            break
                    }
                    nullPairs.add(Pair(i, i+freeSpace))
                    if(fileSpace <= freeSpace) {
                        for(j in i..i+fileSpace)
                            mutableList[j] = mutableList[fileStartPos]
                        for(j in fileStartPos..fileEndPos)
                            mutableList[j] = null
                        continue@b
                    }
                }
            }
        }
    }
    // checksum
    var checksum = 0uL
    for((pos, num) in mutableList.withIndex()) {
        if(num != null)
            checksum += pos.toULong() * num.toULong()
    }
    return checksum
}

fun main() {
    val path : Path
    val part01: ULong
    val part02: ULong
    val input: List<Int>
    val elapsedLoadFile = measureNanoTime {
        path = Path("./src/09.input.txt")
        input = day09_convertInput(path)
    }
    val elapsedPart01 = measureNanoTime {
        part01 = day09_part01(input)
    }
    val elapsedPart02 = measureNanoTime {
        part02 = day09_part02(input)
    }

    val title = "Advent Of Code 2024 - Day 09"
    println(ANSI.GREEN + title + ANSI.RESET + "\n" +
            (ANSI.RED + "-" + ANSI.RESET).repeat(title.length))
    println("Got the input in " + convertTime(elapsedLoadFile) + "\n" +
            "First Part:  " + ANSI.BLUE + part01 + ANSI.RESET + " (" + convertTime(elapsedPart01) + ")\n" +
            "Second Part: " + ANSI.BLUE + part02 + ANSI.RESET + " (" + convertTime(elapsedPart02) + ")\n" +
            "Finished in " + convertTime(elapsedLoadFile + elapsedPart01 + elapsedPart02)
    )
}