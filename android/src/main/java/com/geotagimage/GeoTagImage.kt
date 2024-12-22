import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.location.Location
import android.location.LocationManager
import android.location.LocationProvider
import android.net.Uri
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

private const val PACKAGE_TAG = "GEOTAG"

class GeoTagImage(
  context: Context, private var contentResolver: ContentResolver, private val fontSize: Float = 20f

  ) {
  private var baseContext: Context = context
  private val horizontalRectPadding = 10f
  private val ellipsizeMargin = 20f
  private val rectMarginTop = 250f
  private val rectMarginBottom = 10f
  private val staticLayoutLineSpace = 12f
  private var locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

  /**
   * Adds a overlay for geotagging image with coordinates, address and timestamp
   * @param geoTagData Array of string which are to be placed on image
   * @param imagePath Path of the image
   * @return returns the path as Result
   */
  fun addGeoOverlay(geoTagData: ArrayList<String>, imagePath: String): Result<String> {

    Log.i(PACKAGE_TAG, "addTextOverlay")
    val imageUri = Uri.parse(imagePath)
    Log.i(PACKAGE_TAG, imageUri.toString())
    val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri))
    val bitmapOutputImage = bitmap.copy(Bitmap.Config.ARGB_8888, true)

    val canvas = Canvas(bitmapOutputImage)

    val rectPaint = Paint()
    rectPaint.color = Color.argb(64, 0, 0, 0)
    rectPaint.style = Paint.Style.FILL
    canvas.drawRect(
      horizontalRectPadding,
      canvas.height - rectMarginTop,
      (canvas.width - horizontalRectPadding),
      (canvas.height - rectMarginBottom),
      rectPaint
    )
    val textX = horizontalRectPadding + 10
    var textY = canvas.height - (rectMarginTop - 50f)

    val textPaint = TextPaint()
    textPaint.color = Color.WHITE
    textPaint.textSize = fontSize

    val locationString = fetchLocation()
    val elementList: ArrayList<String> = ArrayList(geoTagData)

    elementList.add(locationString)

    for (item in geoTagData) {
      val ellipsizeText = TextUtils.ellipsize(
        item,
        textPaint,
        canvas.width - ellipsizeMargin,
        TextUtils.TruncateAt.END
      ).toString()
      val staticLayout =
        StaticLayout.Builder.obtain(
          ellipsizeText,
          0,
          ellipsizeText.length,
          textPaint,
          canvas.width
        )
          .setAlignment(Layout.Alignment.ALIGN_NORMAL).setLineSpacing(0f, 1f)
          .setEllipsize(TextUtils.TruncateAt.END)
          .setMaxLines(1)
          .setIncludePad(false).build()

      canvas.save()
      canvas.translate(textX, textY)
      staticLayout.draw(canvas)
      canvas.restore()
      textY += staticLayout.height + staticLayoutLineSpace
    }

    val filePath = saveImageToStorage(bitmapOutputImage)
    return if (filePath != null) {
      Result.success(filePath)
    } else Result.failure(Throwable("failure adding overlay"))

  }

  /**
   * Saves a bitmap to application storage
   * @param imageBitmap The bitmap of the image
   * @return the file path as a string or null
   */
  private fun saveImageToStorage(imageBitmap: Bitmap): String? {
    val fileName = "geo_tag_img_${System.currentTimeMillis()}"
    val fileDir = baseContext.getExternalFilesDir("geo_images")
    try {
      val file = File(fileDir, fileName)
      FileOutputStream(file).use { outStream ->
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
        outStream.flush()
      }
      return file.absolutePath
    } catch (e: IOException) {
      return null
    }
  }

  /**
   * Deletes the geotagged image
   * @param imagePath: The Path of the image to be deleted
   * @return Result of the process
   */
  fun deleteImageFromPath(imagePath: String): Result<String> {
    val file = File(imagePath)
    if (file.exists()) {
      return if (file.delete()) {
        Result.success("Image deleted successfully")
      } else Result.failure(Throwable("Could not delete file"))

    }
    return Result.failure(Throwable("File does not exists"))
  }

  fun fetchLocation(): String {
    val hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    val hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)


      if (baseContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(
          Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
      ) {
        if (hasGps)
        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let {
          return "${it.latitude} ${it.longitude}"
        }
        if (hasNetwork){
          locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)?.let {
            return "${it.latitude} ${it.longitude}"
          }
        }
      }
    return "Unable to fetch location"
 }
}
