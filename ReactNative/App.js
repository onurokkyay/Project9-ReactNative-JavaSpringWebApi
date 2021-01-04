/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */
import {
  SafeAreaView,
  StyleSheet,
  ScrollView,
  View,
  Text,
  StatusBar,
  CameraRoll,
} from 'react-native';
import {observable} from 'mobx';
import React, { Component } from 'react';
import { TouchableOpacity } from 'react-native-gesture-handler';
import Icon from 'react-native-vector-icons/FontAwesome5'
import Camera from './src/Components/Camera'
import Gallery from './src/Components/Gallery';
import Upload from './Upload'
export default class App extends React.Component {
  constructor(props) {
    super(props);
    this.state={
    name:'Selam'
    }
  }
  render() {
    
    return (
   
<SafeAreaView style={{flex:1}}>
<Upload />
</SafeAreaView>






    );
  }
}
