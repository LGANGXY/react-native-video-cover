/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View,
    TouchableOpacity,
    Image,
} from 'react-native';
import ImagePicker from 'react-native-image-picker';
import RNVideoCover from 'react-native-video-cover';
export default class Example extends Component {

    constructor(props) {
        super(props);
        //debugger;
        this.state = {
            videoSource: "emptyurl",
        };
    }

    componentDidMount() {
    }

    choiceVideo(){
        const options ={
            title: '选择视频',
            cancelButtonTitle: '取消',
            takePhotoButtonTitle: '拍摄视频',
            chooseFromLibraryButtonTitle: '从相册选择…',
            quality: 1.0,
            allowsEditing: false,
            mediaType: 'video',
        };

        ImagePicker.showImagePicker(options, (response) => {
            console.log('Response = ', response);

            if (response.didCancel) {
                console.log('User cancelled video picker');
            }
            else if (response.error) {
                console.log('ImagePicker Error: ', response.error);
            }
            else if (response.customButton) {
                console.log('User tapped custom button: ', response.customButton);
            }
            else {
                //debugger;
                //  RNVideoCover.assetGetThumImage(response.uri).then({
                //      RNVideoCover.getNativeClass(name => {
                //      console.log("nativeClass: ", name);
                //     })
                //  });
                RNVideoCover.getVideoCover(response.uri).then(result => {
                        console.log("result is "+result.thumbnail+"   "+result.duration);
                        this.setState({
                            videoSource:result.thumbnail,
                        })
                    }
                ).catch(error => {
                        console.log(error);
                    }
                )

            }
        });
    }

  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>
          Welcome to React Native!
        </Text>
        <Text style={styles.instructions}>
          To get started, edit index.ios.js  dd
        </Text>
        <Image style={{height:120, width:120}} source={{uri:this.state.videoSource}} />
          <TouchableOpacity onPress={this.choiceVideo.bind(this)}>
              <Text>{'选择视频'}</Text>
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
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});

AppRegistry.registerComponent('Example', () => Example);
