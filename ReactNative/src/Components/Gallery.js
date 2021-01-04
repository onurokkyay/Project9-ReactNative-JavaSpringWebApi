import React, { Component } from 'react';
import { View, Text,Button,ScrollView,Image,TouchableHighlight } from 'react-native';
import CameraRoll from "@react-native-community/cameraroll";
import { TouchableOpacity } from 'react-native-gesture-handler';
import MainStore from '../../MainStore';
import { Observer } from 'mobx-react';
@Observer
export default class Camera extends Component {
  constructor(props) {
    super(props);
    this.state = {
      photos:[]
    };
  }

  _handleButtonPress = () => {
    CameraRoll.getPhotos({
        first: 20,
        assetType: 'Photos',
      })
      .then(r => {
        this.setState({ photos: r.edges });
      })
      .catch((err) => {
         //Error Loading Images
      });
    };
 render() {
  return (
    <View style={{flex:1}}>
      <Button title="Load Images" onPress={this._handleButtonPress} />
      <ScrollView >
        {this.state.photos.map((p, i) => {
        return (
          <View>
<TouchableHighlight
                      style={{opacity: i === this.state.index ? 0.5 : 1}}
                      key={i}
                      underlayColor='transparent'
                      onPress={() => {MainStore.seturi(p.node.image.uri)}}>
                     
          <Image
          
            style={{
              width: 420,
              height: 200,
            }}
            source={{ uri: p.node.image.uri }}
          />
          </TouchableHighlight>
            </View>
        );
      })}
      </ScrollView>
    </View>
  );
 }
}
