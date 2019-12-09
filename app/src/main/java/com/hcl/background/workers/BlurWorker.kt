package com.hcl.background.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.hcl.background.utils.*
import java.io.FileNotFoundException
import java.lang.IllegalArgumentException

class BlurWorker(context: Context, parms: WorkerParameters) : Worker(context, parms) {


    override fun doWork(): Result {
        val appContext = applicationContext
        makeStatusNotification("worker has started", appContext)
        sleep()
        try {
            val outPutData = createBlurredBitMap(appContext, inputData.getString(KEY_IMAGE_URI));
            Result.success(outPutData)
        } catch (exception: Exception) {

        }
        return Result.success();
    }


    @Throws(FileNotFoundException::class, IllegalArgumentException::class)
    private fun createBlurredBitMap(appContext: Context, resourceUri: String?): Data {
        if (resourceUri.isNullOrEmpty()) {
            Log.d("Invalid Uri", "Invalid uri")
            throw (IllegalArgumentException("invalid uri"));
        }
        val contentResolver = appContext.contentResolver;
        //create a bitmap
         val bitMap = BitmapFactory.decodeStream(contentResolver.openInputStream(Uri.parse(resourceUri)));
         val outPut = blurBitMap(bitMap,appContext);

        // Write bitmap to a temp file
        val outputUri = writeBitmapToFile(appContext, outPut)

        // Return the output for the temp file
        return workDataOf(KEY_IMAGE_URI to outputUri.toString())

    }

}