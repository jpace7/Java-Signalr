
#!/bin/bash

#echo "rf a5 off" | nc localhost 1099


if [ $# -gt 0 ]; then
    echo "rf" $1  $2 | nc localhost 1099
else
    echo "Your command line contains no arguments"
fi

