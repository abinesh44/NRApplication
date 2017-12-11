**Application**

Application to process the words (valid numbers that are 9 digits long) and display the statistics.
Logs the distinct entries in the file path /tmp/numbers.log

**Build**

The executable can be built by maven.
mvn clean install

The jar file "NR-Application-1.0-jar-with-dependencies.jar"  is executable jar.


**Running the Application**

The application runs with default parameters by just executing the jar.
Run with an min heap space of greater 1G for optimal performance.

(eg) java -jar -Xms2g NR-Application-1.0-jar-with-dependencies.jar


**Application Overview**

Application has been designed with minimizing blocking time and in a multithreaded environment.

**Application Processing Flow.**

Overall application flow can be defined as a stream of processors/aggregators.

NumberListerService -> 
    n * LineClientHandler -> 
        DataProcessorManager -> 
        m * DataProcessorWorker -> 
            StatsAggregator ->
                StatsForwarder
                
-> Indicates a asycnhronous pipeline (A queue)            
                       
                       