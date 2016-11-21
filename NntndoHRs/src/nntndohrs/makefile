
JFLAGS = -g 
JC = javac
MAIN = NntndoHrs
.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSAS = \
	NntndoHRs.java \
	lexer.java \
	lexeme.java \
	parser.java \
	evaluator.java

all: classes run

classes: 
	$(CLASSES:.java=.class)


run: $(MAIN).class
	$(JVM) $(MAIN) 


clean:
	$(RM) *.class





