#ifndef OPENCLADDMATRICES_H
#define OPENCLADDMATRICES_H

#include <string>
#include <iostream>
#include <fstream>
#include <sstream>
#include <cstdio>
#include <cstdlib>


#define CL_USE_DEPRECATED_OPENCL_1_1_APIS

#if defined(__APPLE__) || defined(__MACOSX)
    #include <OpenCL/cl.hpp>
#else
    #include <CL/cl.hpp>
#endif

#include "compute.h"

void openCLAddMatrices (int *a, int *b, int *c, int *i);

#endif
