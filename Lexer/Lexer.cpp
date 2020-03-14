
#include <stdbool.h> 
#include <stdio.h> 
#include <string.h> 
#include <stdlib.h> 

// Returns 'true' if the character is a DELIMITER. 
bool isDelimiter(char ch)
{
    switch (ch) {
    case ' ':
    case ';':
    case ',':
    case '.':
    case '}':
    case '{':
        return (true);
    }
    return (false);
}

// Returns 'true' if the character is an OPERATOR. 
bool isOperator(char ch)
{

    switch (ch) {
    case '=':
    case '+':
    case '-':
    case '*':
    case '/':
        return (true);
    }
    return (false);
   
}

// Returns 'true' if the string is a VALID IDENTIFIER. 
bool isVariable(char str[1])
{
    switch (str[0]) {
    case 'a':
    case 'b':
    case 'c':
    case 'd':
    case 'e':
    case 'f':
    case 'g':
    case 'h':
    case 'i':
    case 'j':
    case 'k':
    case 'l':
    case 'm':
    case 'n':
    case 'o':
    case 'p':
    case 'q':
    case 'r':
    case 's':
    case 't':
    case 'u':
    case 'v':
    case 'w':
    case 'x':
    case 'y':
    case 'z':
    case 'A':
    case 'B':
    case 'C':
    case 'D':
    case 'E':
    case 'F':
    case 'G':
    case 'H':
    case 'I':
    case 'J':
    case 'K':
    case 'L':
    case 'M':
    case 'N':
    case 'O':
    case 'P':
    case 'Q':
    case 'R':
    case 'S':
    case 'T':
    case 'U':
    case 'V':
    case 'W':
    case 'X':
    case 'Y':
    case 'Z':
        return (true);
    }
   return (false);
}

// Returns 'true' if the string is a KEYWORD. 
    bool isKeyword(char str[50])
    {
        if (   !strcmp(str, "if")     || !strcmp(str, "else")
            || !strcmp(str, "while")  || !strcmp(str, "do")   
            || !strcmp(str, "break")  || !strcmp(str, "int")
            || !strcmp(str, "double") || !strcmp(str, "float")
            || !strcmp(str, "return") || !strcmp(str, "char")
            || !strcmp(str, "case")   || !strcmp(str, "long")
            || !strcmp(str, "switch") || !strcmp(str, "void")
            || !strcmp(str, "static") || !strcmp(str, "string"))
            return (true);
        return (false);
    }


// Returns 'true' if the string is an INTEGER. 
bool isInteger(char str[])
{
    int i, len = strlen(str);

    if (len == 0)
        return (false);
    for (i = 0; i < len; i++) {
        if (str[i] != '0' && str[i] != '1' && str[i] != '2'
            && str[i] != '3' && str[i] != '4' && str[i] != '5'
            && str[i] != '6' && str[i] != '7' && str[i] != '8'
            && str[i] != '9' || (str[i] == '-' && i > 0))
            return (false);
    }
    return (true);
}

// Returns 'true' if the string is a REAL NUMBER. 
bool isRealNumber(char str[])
{
    int i, len = strlen(str);
    bool hasDecimal = false;

    if (len == 0)
        return (false);
    for (i = 0; i < len; i++) {
        if (str[i] != '0' && str[i] != '1' && str[i] != '2'
            && str[i] != '3' && str[i] != '4' && str[i] != '5'
            && str[i] != '6' && str[i] != '7' && str[i] != '8'
            && str[i] != '9' && str[i] != '.' ||
            (str[i] == '-' && i > 0))
            return (false);
        if (str[i] == '.')
            hasDecimal = true;
    }
    return (hasDecimal);
}

bool isVariableValue(char str[])
{
  int i,len = strlen(str);
  if (len <= 1)
      return (false);
    for (i = 0; i < len; i++) {
        if (str[i] != '`' && str[len] != '`') {
            return (true);
        }
    }
    return(false);
}

// Extracts the SUBSTRING. 
char* subString(char* str, int left, int right)
{
    int i;
    char* subStr = (char*)malloc(
        sizeof(char) * (right - left + 2));

    for (i = left; i <= right; i++)
        subStr[i - left] = str[i];
    subStr[right - left + 1] = '\0';
    return (subStr);
}

// Parsing the input STRING. 
void parse(char* str)
{
    int left = 0, right = 0;
    int len = strlen(str);

    while (right <= len && left <= right) {
        if (isDelimiter(str[right]) == false)
            right++;

        if (isDelimiter(str[right]) == true && left == right) {
            if (isOperator(str[left]) == true)
                printf("'%c' IS AN OPERATOR\n", str[right]);

            right++;
            left = right;
        }
        else if (isDelimiter(str[right]) == true && left != right
            || (right == len && left != right)) {
            char* subStr = subString(str, left, right - 1);

            if (isKeyword(subStr) == true)
                printf("'%s' - keyword\n", subStr);

            else if (isOperator(str[left]) == true)
                printf("'%s' - operator\n", subStr);

            else if (isVariableValue(subStr) == true)
                printf("'%s' - string value\n", subStr);

            else if (isInteger(subStr) == true)
                printf("'%s' - integer\n", subStr);

            else if (isRealNumber(subStr) == true)
                printf("'%s' - real number\n", subStr);

            else if (isVariable(subStr) == true
                && isDelimiter(str[right - 1]) == false)
                printf("'%s' - valid variable\n", subStr);

            else if (isVariable(subStr) == false
                && isDelimiter(str[right - 1]) == false)
                printf("'%s' - not valid variable\n", subStr);
            left = right;
        }
    }
    return;
}

// DRIVER FUNCTION 
int main()
{
    // maximum legth of string is 100 here  
    char str[100] = "string a = `as`, b = `bs` ; int i = 0 ; while (i<2) { a = b ; i++ ; }";

    parse(str); // calling the parse function 

    return (0);
}