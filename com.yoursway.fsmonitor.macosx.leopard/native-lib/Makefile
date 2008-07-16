#*******************************************************************************
# Copyright (c) 2000, 2007 IBM Corporation and others.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     IBM Corporation - initial API and implementation
#*******************************************************************************

# Makefile for SWT libraries on Cocoa/Mac

# include make_common.mak

maj_ver=1
min_ver=0

OUTPUT_DIR=../
SWT_PREFIX=ys-fs-monitor
WS_PREFIX=macosx_leopard
SWT_VERSION=$(maj_ver)$(min_ver)
SWT_LIB=lib$(SWT_PREFIX)-$(WS_PREFIX).jnilib

# Uncomment for Native Stats tool
#NATIVE_STATS = -DNATIVE_STATS

#SWT_DEBUG = -g
ARCHS = -arch i386 -arch ppc
CFLAGS = -c -xobjective-c -Wall $(ARCHS) -DSWT_VERSION=$(SWT_VERSION) $(NATIVE_STATS) $(SWT_DEBUG) -DUSE_ASSEMBLER -DCOCOA \
	-I /System/Library/Frameworks/JavaVM.framework/Headers \
	-I /System/Library/Frameworks/Cocoa.framework/Headers
LFLAGS = -bundle $(ARCHS) -framework JavaVM -framework Cocoa -framework Carbon -framework CoreServices -framework WebKit
SWT_OBJECTS = os.o

all: $(SWT_LIB)

.c.o:
	cc $(CFLAGS) $*.c

$(SWT_LIB): $(SWT_OBJECTS)
	cc -o $(SWT_LIB) $(LFLAGS) $(SWT_OBJECTS)

install: all
	cp *.jnilib $(OUTPUT_DIR)

clean:
	rm -f *.jnilib *.o
