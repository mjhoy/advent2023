package main

import (
	"david/src/day_001"
	"david/src/day_002"
	"david/src/day_003"
	"fmt"
	"os"
	"strconv"
)

func main() {

	day, _ := strconv.Atoi(os.Args[1:][0])
	if day == 1 {
		fmt.Println(day_001.SumFirstLastInts("src/day_001/sample.txt"))
		fmt.Println(day_001.SumFirstLastIntsWithStr("src/day_001/sample2.txt"))
	}
	if day == 2 {
		fmt.Println(day_002.ProblemOne("src/day_002/sample.txt"))
		fmt.Println(day_002.ProblemTwo("src/day_002/sample.txt"))
	}
	if day == 3 {
		fmt.Println(day_003.Solution("src/day_003/sample.txt", true))
		fmt.Println(day_003.Solution("src/day_003/sample.txt", false))
	}
}
