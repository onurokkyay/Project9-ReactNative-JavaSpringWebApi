import React from 'react';
import {
  Image,
  PixelRatio,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
  Platform,
  PermissionsAndroid,
} from 'react-native';
import RNFetchBlob from 'react-native-fetch-blob'
import axios from 'axios'
import * as firebase from 'firebase';
import {
    launchCamera,
    launchImageLibrary
  } from 'react-native-image-picker';
import MainStore from './MainStore';
export default class App extends React.Component {
  state = {
    avatarSource: null,
    videoSource: null,
    refreshscreen:0,
  };
  

       
      
  
      
    
  constructor(props) {
    super(props);

    this.selectPhotoTapped = this.selectPhotoTapped.bind(this);
  
  }

  

  componentDidMount() {
    const config = {
        apiKey: 'AIzaSyDPazbRwqD3M7oCy9OY4ZUn_AFmH93Q1eQ',
        authDomain: 'nesnealgilama.firebaseapp.com',
        databaseURL: "https://nesnealgilama-default-rtdb.europe-west1.firebasedatabase.app",
        projectId: 'nesnealgilama',
        storageBucket: 'nesnealgilama.appspot.com',
        messagingSenderId: '165732810337',
        };

    if (!firebase.apps.length) {
      firebase.initializeApp(config);
      console.log('Firebase entergasyonu tamam!');
    }
  }


  
  uploadImage = () => {

    const mime = 'image/jpg'
    const image = this.state.avatarSource
    const imageName = this.state.avatarName
    const Blob = RNFetchBlob.polyfill.Blob
    const fs = RNFetchBlob.fs;
  
    window.XMLHttpRequest = RNFetchBlob.polyfill.XMLHttpRequest
    window.Blob = Blob
    const Fetch = RNFetchBlob.polyfill.Fetch
    // replace built-in fetch
    window.fetch = new Fetch({
        // enable this option so that the response data conversion handled automatically
        auto : true,
        // when receiving response data, the module will match its Content-Type header
        // with strings in this array. If it contains any one of string in this array, 
        // the response body will be considered as binary data and the data will be stored
        // in file system instead of in memory.
        // By default, it only store response data to file system when Content-Type 
        // contains string `application/octet`.
        binaryContentTypes : [
            'image/',
            'video/',
            'audio/',
            'foo/',
        ]
    }).build()
    console.log("URI =>", image.uri);
    imgUri = image.uri;
    
    let uploadBlob = null
    const imageRef = firebase.storage().ref('avatars/' + imageName)
    const uploadUri =
      Platform.OS === "ios" ? imgUri.replace("file://", "") : imgUri;
    fs.readFile(uploadUri, 'base64')
    .then((data) => {
      return Blob.build(data, { type: `${mime};BASE64` })
    })
    .then((blob) => {
      uploadBlob = blob
      return imageRef.put(blob, { contentType: mime })
    })
    .then(() => {
      uploadBlob.close()
      return imageRef.getDownloadURL()
    })
    .then((url) => {
     // console.log("URL =>", url)
      MainStore.downloadurl=url;
     // alert("Yüklenen URL:"+MainStore.downloadurl)
      console.log("Firebase'e yüklenen fotoğrafın adı: "+MainStore.imageName)
      console.log("Firebase'e yüklenen fotoğrafın indirme url'i: "+MainStore.downloadurl)
      axios.post('https://nesnealgilama.ew.r.appspot.com/ImageProcess',{
          imageName: MainStore.imageName,
          downloadURL:MainStore.downloadurl,
          totalObjects:0,
          allObjects:"",

      }).then((response)=>{
        
        

        console.log(response.data.downloadURL)
       // alert(response.data.imageName)
        MainStore.SonucURL="https://firebasestorage.googleapis.com/v0/b/nesnealgilama.appspot.com/o/avatars%2FIslenmis"+response.data.imageName+".jpg?alt=media&token=x";
        console.log("İndirme Linki:"+MainStore.SonucURL);
        MainStore.ObjeSayisi=response.data.totalObjects
        MainStore.ObjeSayisi=MainStore.ObjeSayisi/4
        MainStore.BulunanObjeler=response.data.allObjects
        console.log("Toplam Obje Sayısı:"+MainStore.ObjeSayisi);
        console.log("Tanımlanan Objeler:"+response.data.allObjects);
        this.setState({
          refreshscreen:this.state.refreshscreen+1,
        })
       // console.log("Ekran güncellendi");
      }
        
        
        )
    })
    .catch((error) => {
      console.log(error)
    })

 
   
  }

  selectPhotoTapped() {
    const options = {
      quality: 1.0,
      maxWidth: 500,
      maxHeight: 500,
      noData: true,
    };

    launchImageLibrary(options, response => {
      
      console.log('Response => ', response);

      if (response.didCancel) {
        console.log('Kullanıcı galeriden çıktı. ');
      } else if (response.error) {
        console.log('ImagePicker Hatası: ', response.error);
      } else if (response.customButton) {
        console.log('Custom butona basıldı: ', response.customButton);
      } else {
        
        this.setState({
          avatarSource: response,
          avatarName: response.fileName
        });
        
        console.log(" Image Source (Response) =>" , this.state.avatarSource)
        console.log(" Seçilen fotoğrafın uri'si =>" , this.state.avatarSource.uri)
        console.log(" Seçilen fotoğrafın adı: =>" , this.state.avatarName)
        MainStore.setimageName(this.state.avatarName);
      
      }
    });
  }

   requestCameraPermission = async () => {
    if (Platform.OS === 'android') {
      try {
        const granted = await PermissionsAndroid.request(
          PermissionsAndroid.PERMISSIONS.CAMERA,
          {
            title: 'Camera Permission',
            message: 'App needs camera permission',
          },
        );
        // If CAMERA Permission is granted
        return granted === PermissionsAndroid.RESULTS.GRANTED;
      } catch (err) {
        console.warn(err);
        return false;
      }
    } else return true;
  };
   requestExternalWritePermission = async () => {
    if (Platform.OS === 'android') {
      try {
        const granted = await PermissionsAndroid.request(
          PermissionsAndroid.PERMISSIONS.WRITE_EXTERNAL_STORAGE,
          {
            title: 'External Storage Write Permission',
            message: 'App needs write permission',
          },
        );
        // If WRITE_EXTERNAL_STORAGE Permission is granted
        return granted === PermissionsAndroid.RESULTS.GRANTED;
      } catch (err) {
        console.warn(err);
        alert('Write permission err', err);
      }
      return false;
    } else return true;
  };
   captureImage = async () => {
    let options = {
      mediaType: 'photo',
      maxWidth: 300,
      maxHeight: 550,
      quality: 1,
      videoQuality: 'low',
      durationLimit: 30, //Video max duration in seconds
      saveToPhotos: true,
    };
    
  
      launchCamera(options, (response) => {
        console.log('Response = ', response);

        if (response.didCancel) {
          alert('Kullanıcı kameradan çıktı');
          return;
        } else if (response.errorCode == 'camera_unavailable') {
          alert('Kamera bu cihazda kullanılamamakta');
          return;
        } else if (response.errorCode == 'permission') {
          alert('İzin alınamadı');
          return;
        } else if (response.errorCode == 'others') {
          alert(response.errorMessage);
          return;
        }
        console.log('base64 -> ', response.base64);

        this.setState({
          avatarSource: response,
          avatarName: response.fileName
        });
        
        console.log("Çekilen fotoğrafın urisi:", response.uri);
        
        console.log("Çekilen fotoğrafın adı:", response.fileName);
        MainStore.imageName=response.fileName;
        
        
      });
    
  };

  render() {
    const { avatarSource } = this.state; 
    return (
      
      <View style={styles.container}>
        <View style={styles.containeryanyana}>
        <TouchableOpacity onPress={this.selectPhotoTapped.bind(this)}>
          <View 
            style={[styles.avatar, styles.avatarContainer, {marginBottom: 8,}]}>
      
              <Text>Fotoğraf Seç</Text>
          
          </View>
        </TouchableOpacity>

        {this.state.videoSource && (
          <Text style={{margin: 8, textAlign: 'center'}}>
            {this.state.videoSource}
          </Text>
        )}

      <TouchableOpacity onPress={this.captureImage}>
          <View
            style={[styles.avatar, styles.avatarContainer, {marginBottom: 8,marginRight:10,marginLeft:10,}]}>
               {this.state.avatarSource === null ? (
              <Text> </Text>
            ) : (
              <Image style={styles.avatar} source={{uri: avatarSource.uri}} />
            )}
          </View>
        </TouchableOpacity>

        <TouchableOpacity onPress={this.captureImage}>
          <View
            style={[styles.avatar, styles.avatarContainer, {marginBottom: 8}]}>
          
              <Text>Fotoğraf Çek</Text>
          
          </View>
        </TouchableOpacity>

        {this.state.videoSource && (
          <Text style={{margin: 8, textAlign: 'center'}}>
            {this.state.videoSource}
          </Text>
        )}

       </View>
       <Text style={{marginBottom:10,color:'#BB86FC'}}>Fotoğrafı Yüklemek İçin Butona Basınız</Text>
       <Text style={{color:'#BB86FC'}}>Toplam Obje Sayısı:{MainStore.ObjeSayisi}</Text>
       <Text style={{color:'#BB86FC'}}>Tanımlanan Objeler:{MainStore.BulunanObjeler}</Text>
  <TouchableOpacity onPress={()=>
  { this.state.avatarSource === null ? (
    alert("Bir Fotoğraf Seç")
  ) : (
    this.uploadImage()
  )}
 

}  
  >
  <View
            style={[styles.avatarlarge, styles.avatarContainer, {marginBottom: 20}]}>
            {MainStore.SonucURL === null ? (
              <Text>Fotoğrafı Yükle</Text>
            ) : (
              <Image style={styles.avatarlarge} source={{uri: MainStore.SonucURL }} />
            )}
          </View>
        </TouchableOpacity>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#121212',
  },
  containeryanyana: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#121212',
    flexDirection:'row',
  
  },
  avatarContainer: {
    borderColor: '#9B9B9B',
    borderWidth: 1 / PixelRatio.get(),
    justifyContent: 'center',
    alignItems: 'center',
  },
  avatar: {
    borderRadius: 75,
    width: 125,
    height: 125,
    backgroundColor:'#BB86FC',
  },
  avatarlarge: {
    resizeMode:'stretch',
    width: 380,
    height: 425,
    backgroundColor:'#BB86FC'
  },
});