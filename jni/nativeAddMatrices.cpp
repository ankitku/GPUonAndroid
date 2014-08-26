#include "nativeAddMatrices.h"
#include <math.h>


void nativeAddMatrices (int *a, int *c, int *info)
{
	LOGI("\n\nStart nativeAddMatrices (i.e., CPU native plain C code)");

    int length;
	clock_t startTimer, stopTimer;

	length = info[0];

	startTimer=clock();

	//run each element 300 times
	for (int k = 0; k < 300; k++)
		for (int i = 0; i < length; i++) {
			int b = length - a[i];
			c[i] = (int) sqrt(
					sqrt(a[i] * a[i] + b*b) + sqrt(a[i]) / sqrt(b));
		}

	stopTimer = clock();
	double elapse = 1000.0* (double)(stopTimer - startTimer)/(double)CLOCKS_PER_SEC;
	info[1] = (int)elapse;

	LOGI("C++ code on the CPU took %g ms\n\n", 1000.0* (double)(stopTimer - startTimer)/(double)CLOCKS_PER_SEC) ;
	return;	
}
