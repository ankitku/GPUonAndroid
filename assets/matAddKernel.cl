__kernel void matAddKernel(__global const int *A,  __global const int *B, 
                                    						__global int *C)
{
	int i = get_global_id(0);
	C[i] = (int) sqrt(sqrt ((float) (A[i]*A[i] + B[i]*B[i])) + sqrt ((float) A[i])/sqrt ((float) B[i]));
}

