//
//  OpenCLActivity.java
//  OpenCL Example1
//
//  Created by Rasmusson, Jim on 18/03/13.
//
//  Copyright (c) 2013, Sony Mobile Communications AB
//  All rights reserved.
//
//  Redistribution and use in source and binary forms, with or without
//  modification, are permitted provided that the following conditions are met:
//
//     * Redistributions of source code must retain the above copyright
//       notice, this list of conditions and the following disclaimer.
//
//     * Redistributions in binary form must reproduce the above copyright
//       notice, this list of conditions and the following disclaimer in the
//       documentation and/or other materials provided with the distribution.
//
//     * Neither the name of Sony Mobile Communications AB nor the
//       names of its contributors may be used to endorse or promote products
//       derived from this software without specific prior written permission.
//
//  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
//  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
//  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
//  DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
//  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
//  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
//  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
//  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
//  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
//  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 

package com.nkt.compute;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.nkt.compute.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MatAddActivity extends Activity {
	protected static final String TAG = "MatAddActivity";
	private static final int N = 4096;

	private void copyFile(final String f) {
		InputStream in;
		try {
			in = getAssets().open(f);
			final File of = new File(getDir("execdir", MODE_PRIVATE), f);

			final OutputStream out = new FileOutputStream(of);

			final byte b[] = new byte[65535];
			int sz = 0;
			while ((sz = in.read(b)) > 0) {
				out.write(b, 0, sz);
			}
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static boolean sfoundLibrary = true;

	static {
		try {
			System.loadLibrary("compute");
		} catch (UnsatisfiedLinkError e) {
			sfoundLibrary = false;
		}
	}

	public static native int runOpenCL(int A[], int C[], int info[]);

	public static native int runNativeC(int A[], int C[], int info[]);

	final int info[] = new int[2]; // Length, Execution time (ms)

	LinearLayout layout;
	int A[], C[];
	ImageView imageView;
	TextView result, tv1, tv2, tv3;
	String s1, s2, s3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mat_add);

		imageView = (ImageView) findViewById(R.id.imageHere);
		result = (TextView) findViewById(R.id.resultText);
		tv1 = (TextView) findViewById(R.id.tv1);
		tv2 = (TextView) findViewById(R.id.tv2);
		tv3 = (TextView) findViewById(R.id.tv3);

		copyFile("matAddKernel.cl"); // copy cl kernel file from assets to
										// /data/data/...assets

		A = new int[N];
		C = new int[N];
		info[0] = N;

		for (int i = 0; i < N; i++)
			A[i] = i;

		s1 = s2 = s3 = "";

		for (int i = 0; i < N; i++) {
			s1 += (A[i] + " ");
			s2 += ((N - A[i]) + " ");
		}

		result.setText("Not added yet");
		tv1.setText(s1);
		tv2.setText(s2);
	}

	public void showOriginalMatrix(View v) {
		result.setText("Not added yet");
		tv1.setText(s1);
		tv2.setText(s2);
		tv3.setText("");
	}

	public void showOpenCLCompute(View v) {
		runOpenCL(A, C, info);
		result.setText("Matrix Addition, OpenCL, Processing time is " + info[1]
				+ " ms");
		s3 = "";
		for (int i = 0; i < N; i++)
			s3 += (C[i] + " ");
		tv3.setText(s3);
	}

	public void showNativeCCompute(View v) {
		runNativeC(A, C, info);
		result.setText("Matrix Addition, NativeC, Processing time is "
				+ info[1] + " ms");
		s3 = "";
		for (int i = 0; i < N; i++)
			s3 += (C[i] + " ");
		tv3.setText(s3);
	}
}
