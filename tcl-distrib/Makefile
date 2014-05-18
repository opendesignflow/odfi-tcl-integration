## This makefile downloads and compiles TCl and dependent libraries for Extoll TCL interfaces
##

## Versions
###############
TCL_VERSION := 8.6.1
TK_VERSION := 8.6.1
NSF_VERSION := 2.0b5
ITCL_VERSION := 3.4.1
TCLLIB_VERSION := 1.14
TCLXML_VERSION := 3.2

## Distribution description
DIST_INSTALL_PACKAGES_DEPS_APT += libxslt-dev libxml2-dev
DIST_INSTALL_PACKAGES_DEPS_YUM += libxml2-devel libxslt-devel
include $(ODFI_MBUILD_HOME)/sw/makefile/Makefile.dist
DIST_VERSION := 8.6.1-r1
DIST_MODULE_NAME := tcl-distrib

## DIST: Download modules
#####################################
dist: tcl$(TCL_VERSION)-src.tar.gz tk$(TK_VERSION)-src.tar.gz  tclxml-$(TCLXML_VERSION).tar.gz itcl$(ITCL_VERSION).tar.gz tcllib-$(TCLLIB_VERSION).tar.gz nsf$(NSF_VERSION).tar.gz
	$(dist-module-prepare-work)
	@echo "-- Preparing TCL interpreter distribution"
	@echo "-- Done Downloading all the required packages . This makefile has been written to use downloaded packages and not redownload when installing"
	@echo "-- Packaging: $^"
	@$(call dist-module-add,$^)
	$(dist-module-package)


tcl$(TCL_VERSION)-src.tar.gz:
	@wget "http://prdownloads.sourceforge.net/tcl/tcl$(TCL_VERSION)-src.tar.gz"

tk$(TK_VERSION)-src.tar.gz:
	@wget "http://prdownloads.sourceforge.net/tcl/tk$(TK_VERSION)-src.tar.gz"

nsf$(NSF_VERSION).tar.gz:
	@wget "http://downloads.sourceforge.net/project/next-scripting/$(NSF_VERSION)/nsf$(NSF_VERSION).tar.gz"

tclxml-$(TCLXML_VERSION).tar.gz:
	@wget "http://prdownloads.sourceforge.net/tclxml/tclxml-3.2.tar.gz"

itcl$(ITCL_VERSION).tar.gz:
	@wget "http://downloads.sourceforge.net/project/incrtcl/%5BIncr%20Tcl_Tk%5D-source/3.4.1/itcl$(ITCL_VERSION).tar.gz"

tcllib-$(TCLLIB_VERSION).tar.gz:
	@wget "http://downloads.sourceforge.net/project/tcllib/tcllib/1.14/tcllib-$(TCLLIB_VERSION).tar.gz"

## Install does everything
#########################

PREFIX ?= $(shell pwd)

.NOTPARALLEL:
install : dist_install_prepare packages folders tcl tk nsf tclxml itcl tcllib

## Clean Deletes compilation results
#clean: clean_tcl clean_tclxml

## mrproper : Destroy everything (all folders apart from this Makefile)
mrproper:
	@echo "Cleaning folder completely (only makefile remains)"
	rm --preserve-root -Rf `ls --hide="Makefile"`


## Common
################

## Install required packages and download sources
packages:  tcl$(TCL_VERSION)-src.tar.gz tk$(TK_VERSION)-src.tar.gz  tclxml-$(TCLXML_VERSION).tar.gz itcl$(ITCL_VERSION).tar.gz tcllib-$(TCLLIB_VERSION).tar.gz nsf$(NSF_VERSION).tar.gz
	@echo "Install required packages: libxml2-dev libxslt-dev"


## Folder prepares folders
folders:
	@echo "Preparing base folders..."
	@if [[ ! -d $(PREFIX)/bin/ ]]; then mkdir -pv $(PREFIX)/bin/ ; fi;
	@if [[ ! -d $(PREFIX)/include/ ]]; then mkdir -pv $(PREFIX)/include/ ; fi;
	@if [[ ! -d $(PREFIX)/lib/ ]]; then mkdir -pv $(PREFIX)/lib/ ; fi;
	@if [[ ! -d src/ ]]; then mkdir -pv src/ ; fi;


## TCL
####################
tcl: $(PREFIX)/bin/tclsh

# Extract and make
$(PREFIX)/bin/tclsh: $(PREFIX)/bin/tclsh8.6
	@ln -f -s $(PREFIX)/bin/tclsh8.6 $(PREFIX)/bin/tclsh

$(PREFIX)/bin/tclsh8.6: src/tcl$(TCL_VERSION)/unix/Makefile
	@echo "Making TCL"
	@cd src/tcl$(TCL_VERSION)/unix && make install

# configure
src/tcl$(TCL_VERSION)/unix/Makefile: tcl$(TCL_VERSION)-src.tar.gz
	@echo "Configuring TCL"
	@cp tcl$(TCL_VERSION)-src.tar.gz src && cd src && tar xvzf tcl$(TCL_VERSION)-src.tar.gz && cd tcl$(TCL_VERSION)/unix && ./configure --prefix=$(PREFIX)


## TK
####################
tk: src/tk$(TK_VERSION)/unix/Makefile
	@cd src/tk$(TK_VERSION)/unix && make && make install

# configure
src/tk$(TK_VERSION)/unix/Makefile: tk$(TK_VERSION)-src.tar.gz
	@echo "Configuring TCL"
	@cp tk$(TK_VERSION)-src.tar.gz src && cd src && tar xvzf tk$(TK_VERSION)-src.tar.gz && cd tk$(TK_VERSION)/unix && ./configure --prefix=$(PREFIX)

## NSF
############################
nsf:  src/nsf$(NSF_VERSION)/Makefile
	@cd src/nsf$(NSF_VERSION)/ && make install

src/nsf$(NSF_VERSION)/Makefile: nsf$(NSF_VERSION).tar.gz
	@cp nsf$(NSF_VERSION).tar.gz src && cd src && tar xvzf nsf$(NSF_VERSION).tar.gz && cd nsf$(NSF_VERSION) && ./configure --prefix=$(PREFIX)

## TCl XML
####################
tclxml: $(PREFIX)/lib/Tclxml$(TCLXML_VERSION)/libTclxml$(TCLXML_VERSION).so


# Make
$(PREFIX)/lib/Tclxml3.2/libTclxml3.2.so: src/tclxml-$(TCLXML_VERSION)/Makefile
	@echo "Making TCL XML"
	@cd src/tclxml-$(TCLXML_VERSION) &&  make install

# Configure
src/tclxml-$(TCLXML_VERSION)/Makefile: tclxml-$(TCLXML_VERSION).tar.gz
	@echo "Configuring TCL XML"
	@cp tclxml-$(TCLXML_VERSION).tar.gz src && cd src && tar xvzf tclxml-$(TCLXML_VERSION).tar.gz && cd tclxml-$(TCLXML_VERSION)/ && ./configure --prefix=$(PREFIX)


## iTCL
##########################
itcl: $(PREFIX)/lib/itcl$(ITCL_VERSION)/libitcl$(ITCL_VERSION).so

$(PREFIX)/lib/itcl$(ITCL_VERSION)/libitcl$(ITCL_VERSION).so : src/itcl$(ITCL_VERSION)/Makefile
	@echo "Making iTCL"
	@cd src/itcl$(ITCL_VERSION)/ &&  make install

src/itcl$(ITCL_VERSION)/Makefile: itcl$(ITCL_VERSION).tar.gz
	@echo "Configuring iTCL"
	@cp itcl$(ITCL_VERSION).tar.gz src && cd src && tar xvzf itcl$(ITCL_VERSION).tar.gz && cd  itcl$(ITCL_VERSION)/ && ./configure --prefix=$(PREFIX)



## TCL lib
###############################
tcllib: src/tcllib-$(TCLLIB_VERSION)/Makefile
	@echo "Making TCL Lib"
	@cd src/tcllib-$(TCLLIB_VERSION) && make install

src/tcllib-$(TCLLIB_VERSION)/Makefile: tcllib-$(TCLLIB_VERSION).tar.gz
	@echo "Configuring TCL Lib"
	@cp tcllib-$(TCLLIB_VERSION).tar.gz src && cd src && tar xvzf tcllib-$(TCLLIB_VERSION).tar.gz && cd tcllib-$(TCLLIB_VERSION)  && ./configure --prefix=$(PREFIX)



