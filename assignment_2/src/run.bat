ECHO OFF
-d build || mkdir -p build

if "%1%" == "s" (
    echo "Compiling..." && \
    javac -cp src:$CLASSPATH -d build assignment2/Backend.java && \
    echo "Running..." && \
    java -cp build:$CLASSPATH \
        -Djava.rmi.server.codebase=file:build/ \
        -Djava.rmi.server.hostname=$2 \
        assignment2.Backend "$2" "$3")

else (
    echo "Compiling..." && \
    javac -cp src:$CLASSPATH -d build assignment2/Frontend.java && \
    echo "Running..." && \
    java -cp build:$CLASSPATH assignment2.Frontend "$2" "$3")
