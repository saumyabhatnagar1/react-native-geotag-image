package com.geotagimage

import GeoTagImage
import android.os.Build.VERSION_CODES.P
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReadableArray


class GeotagImageModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

  override fun getName(): String {
    return NAME
  }

  private var geoTagImage = GeoTagImage(reactContext.baseContext, reactContext.contentResolver)

  @ReactMethod
  fun geoTagImage(
    geoTagData: ReadableArray,
    imagePath: String,
    promise: Promise
  ) {

    val elementsList = ArrayList<String>()
    for(i in 0 until geoTagData.size()){
      elementsList.add(geoTagData.getString(i))
    }
    val imagePathResult = geoTagImage.addGeoOverlay(elementsList, imagePath)

    imagePathResult.onSuccess {
      promise.resolve(it)
    }

    imagePathResult.onFailure {
      promise.reject(it)
    }
  }

  @ReactMethod
  fun deleteGeoTagImage(imagePath: String, promise: Promise) {
    val deleteImageResult = geoTagImage.deleteImageFromPath(imagePath)

    deleteImageResult.onSuccess {
      promise.resolve(it)
    }
    deleteImageResult.onFailure {
      promise.resolve(it)
    }
  }



  companion object {
    const val NAME = "GeotagImage"
  }
}
