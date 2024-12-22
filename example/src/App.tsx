import { useState } from 'react';
import { View, StyleSheet, Button } from 'react-native';
import { geoTagImage } from 'react-native-geotag-image';

export default function App() {
  const [imagePath, _setImagePath] = useState<string | undefined>();

  return (
    <View style={styles.container}>
      <Button
        title="Geotag Image"
        onPress={async () => {
          if (imagePath) {
            try {
              await geoTagImage(
                imagePath,
                [`Timestamp: ${Date.now().toString()}`],
                false
              );
            } catch (e) {}
          }
        }}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
});
