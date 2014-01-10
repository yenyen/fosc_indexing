1. To compile:
$ant compile

2. To create jar file (jar file is created in jar/ directory):
$ant jar

3. To run Hadoop (INPUT_PATH and OUTPUT_PATH are local or HDFS):

bin/hadoop jar PATH_TO_JAR/bda_labs_0.1.0.jar heigvd.bda.labs.relfreq.OrderInversion NUM_REDUCERS INPUT_PATH OUTPUT_PATH