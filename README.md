# react-native-geotag-image

A react native android package for geo tagging the images with coordinates and address.

## Installation

```sh
npm install react-native-geotag-image
```

## Usage


```js
import { geoTagImage } from 'react-native-geotag-image';

// ...

const result = await geoTagImage(
  imagePath: string,
  elementsList: string[],
  tagUserCoordinates: boolean
)
```

## Android Permission

Requires permission for accessing Fine or coarse location if tagUserCoordinages is set true.

Add following lines in AndroidManifest.xml
```
   <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
   <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />

```


## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
