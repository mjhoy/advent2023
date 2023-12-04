// Creator: david
// Date: 2023-12-3
// Purpose: day 3 of advent of code 2023

package day_003

import (
	"david/src/utils"
	"fmt"
	"strconv"
	"unicode"
)

// combine all adjacent digits into a single number and return the number and the indices of the digits
func CombineAllAdjacentDigits(idx int, line []rune) (int, []int) {
	left, right := idx, idx
	for left >= 0 && unicode.IsDigit(line[left]) {
		left--
	}
	for right < len(line) && unicode.IsDigit(line[right]) {
		right++
	}
	digits := line[left+1 : right]
	num, err := strconv.Atoi(string(digits))
	if err != nil {
	}
	return num, []int{left + 1, right - 1}
}

func CheckAdjacentCells(i, j int, matrix [][]rune, visited map[Pair]bool, partOne bool) ([]int, []Pair) {
	// store map of visited indices in a map
	pairs := []Pair{}
	var res = []int{}
	directions := [][]int{
		{-1, 0},  // Up
		{1, 0},   // Down
		{0, -1},  // Left
		{0, 1},   // Right
		{-1, -1}, // Up-left diagonal
		{-1, 1},  // Up-right diagonal
		{1, -1},  // Down-left diagonal
		{1, 1},   // Down-right diagonal
	}
	for _, direction := range directions {
		ni, nj := i+direction[0], j+direction[1]
		if ni >= 0 && ni < len(matrix) && nj >= 0 && nj < len(matrix[0]) {
			if unicode.IsDigit(matrix[ni][nj]) {
				if !visited[Pair{ni, nj}] {
					dig, indices := CombineAllAdjacentDigits(nj, matrix[ni])
					// add all values between the indices to the visited map
					for idx := indices[0]; idx <= indices[1]; idx++ {
						visited[Pair{ni, idx}] = true
						pairs = append(pairs, Pair{ni, idx})
					}
					res = append(res, dig)
				}
			}
		}
	}
	return res, pairs
}

type Pair struct {
	First, Second int
}

func Solution(content string, partOne bool) int {
	matrix := utils.TxtFileToRuneMatrix(content)
	visited := make(map[Pair]bool)
	var result int = 0
	for i := 0; i < len(matrix); i++ {
		for idx, char := range matrix[i] {
			if partOne {
				if char != '.' && (unicode.IsSymbol(char) || unicode.IsPunct(char)) {
					digits, _ := CheckAdjacentCells(i, idx, matrix, visited, partOne)
					for _, digit := range digits {
						result += digit
					}
				}
			} else {
				if char == '*' {
					digits, pairs := CheckAdjacentCells(i, idx, matrix, visited, partOne)
					if len(digits) != 2 {
						for _, pair := range pairs {
							delete(visited, pair)
						}
					} else {
						result += digits[0] * digits[1]
					}
				}
			}
		}

	}
	return result
}

func main() {
	fmt.Println(Solution("sample.txt", true))
	fmt.Println(Solution("sample.txt", false))
}
