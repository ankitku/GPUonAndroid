__kernel void matAddKernel(__global const int *A, __global int *C)
{
	int i = get_global_id(0);
	int n = get_global_size(0);
	int b = n - A[i];
	C[i] = (int) sqrt(sqrt ((float) (A[i]*A[i] + b*b)) + sqrt ((float) A[i])/sqrt ((float) b));
}

