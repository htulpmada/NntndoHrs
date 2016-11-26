


run: compile error1 error1x error2 error2x error3 error3x arrays arraysx conditionals condtionalsx recursion recursionx iteration iterationx functions functionsx dictionary dictionaryx problem problemx


arrays: 
	cat srcCodesNes/ArrayTest.nes

arraysx: compile
	java -classpath classfiles nntndohrs.NntndoHRs srcCodesNes/ArrayTest.nes

error1:
	cat srcCodesNes/Errors/ArrayError1.nes
	cat srcCodesNes/Errors/ArrayError2.nes
	cat srcCodesNes/Errors/UndefineError.nes
	cat srcCodesNes/Errors/ClosureError.nes
	cat srcCodesNes/Errors/ArgumentError.nes

error1x: compile
	java -classpath classfiles nntndohrs.NntndoHRs srcCodesNes/Errors/ArrayError1.nes
	java -classpath classfiles nntndohrs.NntndoHRs srcCodesNes/Errors/ArrayError2.nes
	java -classpath classfiles nntndohrs.NntndoHRs srcCodesNes/Errors/UndefineError.nes
	java -classpath classfiles nntndohrs.NntndoHRs srcCodesNes/Errors/ClosureError.nes
	java -classpath classfiles nntndohrs.NntndoHRs srcCodesNes/Errors/ArgumentError.nes

error2:

error2x: compile

error3:

error3x: compile

conditionals:
	cat srcCodesNes/ConditionalTest.nes

condtionalsx: compile
	java -classpath classfiles nntndohrs.NntndoHRs srcCodesNes/ConditionalTest.nes

recursion:
	cat srcCodesNes/BasicFuncRecursionTest.nes

recursionx: compile
	java -classpath classfiles nntndohrs.NntndoHRs srcCodesNes/BasicFuncRecursionTest.nes

iteration:
	cat srcCodesNes/IterationTest.nes

iterationx: compile
	java -classpath classfiles nntndohrs.NntndoHRs srcCodesNes/IterationTest.nes

functions:
	cat srcCodesNes/FirstClassFunctionTest.nes

functionsx: compile
	java -classpath classfiles nntndohrs.NntndoHRs srcCodesNes/FirstClassFunctionTest.nes

dictionary:

dictionaryx:compile

problem:

problemx: compile

compile: 
	javac -d classfiles -sourcepath src src/nntndohrs/NntndoHRs.java

clean:
	rm -f classfiles/nntndohrs/*.class



