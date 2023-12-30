runFDTests:
	javac Frontend.java FrontendInterface.java TextUITester.java BackendInterface.java Backend.java FlightPath.java FlightPathInterface.java DijkstraGraph.java
	javac -cp .:../junit5.jar FrontendDeveloperTests.java
	java -jar ../junit5.jar --classpath . --select-class FrontendDeveloperTests

runBDTests:
	javac Frontend.java FrontendInterface.java TextUITester.java BackendInterface.java Backend.java FlightPath.java FlightPathInterface.java DijkstraGraph.java
	javac -cp .:../junit5.jar BackendDeveloperTests.java
	java -jar ../junit5.jar --classpath . --select-class BackendDeveloperTests
	
clean:
	rm -f *.class
