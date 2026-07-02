TIMEOUT=5m
TIMELOG = "time"

SKIP=0SkipMe/ # Folders to skip by default; MAKE SURE TO ADD "/"

export # This makes all variables defined here global for sub-makes

TOPTARGETS := all clean superclean zip archive
SUBDIRS1 = $(wildcard */)
SUBDIRS  := $(filter-out $(SKIP), $(SUBDIRS1))

$(TOPTARGETS): $(SUBDIRS)

JAVA_PATH := ../../../
MAKE_FILE_PATH := ../../../

$(SUBDIRS):
	@echo "Top dir: Now working on problems in " $@
	# cd $@ && make -f ../Makefile.subdir $(MAKECMDGOALS)
	$(MAKE) -f ../Makefile.subdir -C $@ $(MAKECMDGOALS) || true

.PHONY: $(TOPTARGETS) $(SUBDIRS)

help:
	@echo "A common commandline is: nohup make -j 20 &"
@echo $(SUBDIRS):

# see also: https://stackoverflow.com/questions/9249757/recursive-make-in-subdirectories



