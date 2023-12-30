runBDTests:
	javac Backend.java IterableMultiKeySortedCollectionInterface.java KeyListInterface.java SortedCollectionInterface.java Movie.java MovieInterface.java TextUITester.java BackendInterface.java
	javac -cp .:../junit5.jar BackendDeveloperTests.java
	java -cp .:../junit5.jar BackendDeveloperTests

runFDTests:
	javac Frontend.java FrontendInterface.java TextUITester.java
	javac -cp .:../junit5.jar FrontendDeveloperTests.java
	java -cp .:../junit5.jar FrontendDeveloperTests

clean:
	rm -f *.class
