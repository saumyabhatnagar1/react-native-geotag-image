import { useState, useEffect } from 'react';
import { Text, View, StyleSheet } from 'react-native';

export default function App() {
  const [result, setResult] = useState<number | undefined>(0);

  return (
    <View style={styles.container}>
      <Text>Result: {result}</Text>
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
