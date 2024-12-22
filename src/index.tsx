import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-geotag-image' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const GeotagImage = NativeModules.GeotagImage
  ? NativeModules.GeotagImage
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

/**
 * Geotags image
 * @param imagePath URI of the image
 * @param elementsList Elements to be placed on the image, can be exif data of the image
 * @param tagUserCoordinates If the user coordinates are to be placed on the image
 * @returns Promise with new imagePath or error
 */
export function geoTagImage(
  imagePath: string,
  elementsList: string[],
  tagUserCoordinates: boolean
): string {
  return GeotagImage.geoTagImage(elementsList, imagePath, tagUserCoordinates);
}

/**
 * Deletes the geotagged Image, if not required further
 * @param imageString The path of the image to be deleted
 * @returns Promise with success or error
 */
export function deleteGeoTaggedImage(imageString: string) {
  return GeotagImage.deleteGeoTagImage(imageString);
}
