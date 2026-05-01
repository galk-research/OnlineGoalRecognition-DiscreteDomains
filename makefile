# Variables
empty :=
space := $(empty) $(empty)
#empty :=
SRC_DIR := src
BIN_DIR := bin
LIB_DIR := lib
JAR_FILES := $(wildcard $(LIB_DIR)/*.jar)
JAR_PATH := $(subst $(space),:,$(strip $(JAR_FILES)))
CLASSPATH := $(BIN_DIR):$(JAR_PATH)
SOURCES := $(shell find $(SRC_DIR) -name "*.java")
CLASSES := $(patsubst $(SRC_DIR)/%.java,$(BIN_DIR)/%.class,$(SOURCES))

# Default target
all: $(CLASSES)

# Rule to compile .java files to .class files
$(BIN_DIR)/%.class: $(SRC_DIR)/%.java
	@mkdir -p $(dir $@)
	javac  -cp $(CLASSPATH) $(SOURCES) -d  $(BIN_DIR) $<

# Run the program
#run: all
#	java -cp $(CLASSPATH) Main

# Clean class files
clean:
	rm -rf $(BIN_DIR)/*

# Print values
print:
	@echo "sources: $(SOURCES)"
	@echo "jar file: $(JAR_FILES)"
	@echo "jar path: $(JAR_PATH)"
	@echo "args: $(ARGS)"

run:
	java -cp "$(CLASSPATH)" experiments.MultipleGoals $(ARGS)
