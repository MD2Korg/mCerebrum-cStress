package org.md2k.cstress;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

import md2k.mCerebrum.CSVDataPoint;
import md2k.mCerebrum.CSVParser;
import md2k.mCerebrum.cStress.autosense.AUTOSENSE;
import md2k.mCerebrum.cStress.library.structs.DataPoint;


/**
 * Copyright (c) 2015, The University of Memphis, MD2K Center
 * - Syed Monowar Hossain <monowar.hossain@gmail.com>
 * All rights reserved.
 * <p/>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * <p/>
 * * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * <p/>
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * <p/>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

public class ServiceStreamProcessor extends Service {
    private static final String TAG = ServiceStreamProcessor.class.getSimpleName();
    StreamProcessorWrapper streamProcessorWrapper;

    @Override
    public void onCreate() {
        super.onCreate();
        streamProcessorWrapper=new StreamProcessorWrapper();
        try {
            readFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public void readFile() throws IOException {
        CSVParser tp = new CSVParser();

        String path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/mCerebrum/";
        Log.d(TAG,"path="+path);
        Log.d(TAG,"before read...");
        tp.importData(path + "/"+"rip.txt", AUTOSENSE.CHEST_RIP);
        tp.importData(path+"ecg.txt", AUTOSENSE.CHEST_ECG);
        tp.importData(path+"accelx.txt", AUTOSENSE.CHEST_ACCEL_X);
        tp.importData(path+"accely.txt", AUTOSENSE.CHEST_ACCEL_Y);
        tp.importData(path+"accelz.txt", AUTOSENSE.CHEST_ACCEL_Z);
        tp.sort();
        for (CSVDataPoint ap : tp) {
            streamProcessorWrapper.addDataPoint(ap);
        }
    }

}
