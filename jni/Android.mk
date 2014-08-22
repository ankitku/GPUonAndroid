LOCAL_PATH		:= $(call my-dir)
LOCAL_PATH_EXT	:= $(call my-dir)/../extra_libs/
include $(CLEAR_VARS)

LOCAL_ARM_MODE  := arm

LOCAL_MODULE    := compute

LOCAL_CFLAGS 	+= -DANDROID_CL 
LOCAL_CFLAGS    += -O3 -ffast-math

LOCAL_C_INCLUDES := $(LOCAL_PATH)/../include

LOCAL_SRC_FILES := compute.cpp openCLNR.cpp refNR.cpp openCLAddMatrices.cpp nativeAddMatrices.cpp

LOCAL_LDLIBS 	:= -llog -ljnigraphics
LOCAL_LDLIBS 	+= $(LOCAL_PATH_EXT)libOpenCL.so  

include $(BUILD_SHARED_LIBRARY)
