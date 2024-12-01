$input = Get-Content -Path ".\01.input.txt" 

$list = $input -split "   "
$rows = $list.Count / 2
$list2d = New-Object system.Array[][] 2,$rows
$rowCounter = 0
for ($i = 0; $i -lt $list.Count; $i++) {
    if ($i % 2 -eq 0) {
        $list2d[0][$rowCounter] = $list[$i] -as [int]
    } else {
        $list2d[1][$rowCounter] = $list[$i] -as [int]
        $rowCounter++
    }
}

$sortedlist2d = New-Object system.Array[][] 2,$rows
$sortedlist2d[0] = $list2d[0] | Sort-Object 
$sortedlist2d[1] = $list2d[1] | Sort-Object

$distances = 0
for ($i = 0; $i -lt $sortedlist2d[0].Count; $i++) {
    $left  = $sortedlist2d[0][$i][0]
    $right = $sortedlist2d[1][$i][0]
    if ($left -lt $right) {
        $distances += $right - $left
    } elseif ($left -gt $right) {
        $distances += $left - $right
    } else {
        $distances += 0
    }
}
Write-Host "First part:  " -NoNewline
Write-Host "$distances" -f Green

$map = @{};

for ($i = 0; $i -lt $list2d[0].Count; $i++) {
    $left  = [int]$list2d[0][$i][0]
    for ($j = 0; $j -lt $list2d[1].Count; $j++) {
        $right = [int]$list2d[1][$j][0]
        if ($left -eq $right) {
            $map[$left] += 1
        }
    }
}

$similarityScore = 0

foreach ($key in $map.Keys) {
    $similarityScore += $key * $map[$key]
}

Write-Host "Second part: " -NoNewline
Write-Host "$similarityScore" -f Green