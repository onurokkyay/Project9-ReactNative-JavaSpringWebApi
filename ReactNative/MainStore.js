import {observable,action, makeObservable} from 'mobx';
import React, { Component } from 'react';
import { View, Text } from 'react-native';

class MainStore  {
    constructor(){
        makeObservable(this)
    }
    @observable downloadurl='string';
    @observable imageName='string';
    @observable SonucURL=null;
    @observable ObjeSayisi;
    @observable BulunanObjeler;
    @action getdownloadurl(){
            return this.downloadurl;
    }

    @action setdownloadurl(downloadurlgelen){
       this.downloadurl=downloadurlgelen;

}
@action getimageName(){
    return this.imageName;
}

@action setimageName(imagenamegelen){
this.imageName=imagenamegelen;

}


}
export default new MainStore();