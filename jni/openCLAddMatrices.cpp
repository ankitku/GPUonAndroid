#define __CL_ENABLE_EXCEPTIONS

#include "openCLAddMatrices.h"

inline std::string loadProgram(std::string input)
{
	std::ifstream stream(input.c_str());
	if (!stream.is_open()) {
		LOGE("Cannot open input file\n");
		exit(1);
	}
	return std::string( std::istreambuf_iterator<char>(stream),
						(std::istreambuf_iterator<char>()));
}

void openCLAddMatrices (int *a, int *c, int *info)
{

	LOGI("\n\nStart openCLAddMatrices (i.e., OpenCL on the GPU)");

	int length = info[0];
	unsigned int matSize = length * sizeof(int);

	cl_int err = CL_SUCCESS;
	cl_int ret;
	try {

		std::vector<cl::Platform> platforms;
		cl::Platform::get(&platforms);
		if (platforms.size() == 0) {
			std::cout << "Platform size 0\n";
			return;
		}

		cl_context_properties properties[] =
		{ CL_CONTEXT_PLATFORM, (cl_context_properties)(platforms[0])(), 0};
		cl::Context context(CL_DEVICE_TYPE_GPU, properties);

		std::vector<cl::Device> devices = context.getInfo<CL_CONTEXT_DEVICES>();
		cl::CommandQueue queue(context, devices[0], 0, &err);


	    // Create memory buffers on the device for each vector
		cl::Buffer a_mem_obj = cl::Buffer(context, CL_MEM_READ_ONLY, matSize, NULL, &ret);
		cl::Buffer c_mem_obj = cl::Buffer(context, CL_MEM_WRITE_ONLY,matSize, NULL, &ret);

	    // Copy the lists A and B to their respective memory buffers
	    ret = queue.enqueueWriteBuffer(a_mem_obj, CL_TRUE, 0,matSize,a,0,NULL);

		std::string kernelSource = loadProgram("/data/data/com.nkt.compute/app_execdir/matAddKernel.cl");

		cl::Program::Sources source(1, std::make_pair(kernelSource.c_str(), kernelSource.length()+1));
		cl::Program program(context, source);
		const char *options = "-cl-fast-relaxed-math";
		program.build(devices, options);

		cl::Kernel kernel(program, "matAddKernel", &err);


	    // Set the arguments of the kernel
	    ret = kernel.setArg(0, a_mem_obj);
	    ret = kernel.setArg(1, c_mem_obj);

		cl::Event event;

		clock_t startTimer1, stopTimer1;
		startTimer1=clock();

		  // Execute the OpenCL kernel on the list
		size_t global_item_size = length; // Process the entire lists
		size_t local_item_size = 128; // Divide work items into groups of 64

		//run each element 300 times
		for (int k = 0; k < 300; k++)
		queue.enqueueNDRangeKernel(	kernel,
				cl::NullRange,
				cl::NDRange(global_item_size),
				cl::NDRange(local_item_size),
				NULL,
				&event);

		queue.finish();

		stopTimer1 = clock();
		double elapse = 1000.0* (double)(stopTimer1 - startTimer1)/(double)CLOCKS_PER_SEC;
		info[1] = (int)elapse;
		LOGI("OpenCL code on the GPU took %g ms\n\n", 1000.0* (double)(stopTimer1 - startTimer1)/(double)CLOCKS_PER_SEC) ;

		queue.enqueueReadBuffer(c_mem_obj, CL_TRUE, 0, matSize, c);
	}
	catch (cl::Error err) {
		LOGE("ERROR: %s\n", err.what());
	}
	return;
}
