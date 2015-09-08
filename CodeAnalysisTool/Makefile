all:
	cd CodeReportTool; gradle build
	mkdir -p bin
	cp ./CodeReportTool/build/libs/CodeReportTool-1.0-SNAPSHOT.jar bin/CodeReportTool.jar

clean:
	rm -r bin
	cd CodeReportTool; gradle clean
