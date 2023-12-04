package utils

import (
	"io/ioutil"
	"log"
	"strings"
)

func TxtFileReader(txtFile string) []string {
	content, error := ioutil.ReadFile(txtFile)
	if error != nil {
		log.Fatal(error)
	}
	contentArray := strings.Split(string(content[:]), "\n")
	return contentArray
}

// convert txt file to rune 2D matrix
func TxtFileToRuneMatrix(txtFile string) [][]rune {
	content, error := ioutil.ReadFile(txtFile)
	if error != nil {
		log.Fatal(error)
	}
	var res = [][]rune{}
	contentArray := strings.Split(string(content[:]), "\n")
	for _, line := range contentArray {
		runeArray := []rune(line)
		res = append(res, runeArray)
	}
	return res
}
