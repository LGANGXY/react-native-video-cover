
# react-native-video-cover

## Getting started

`$ npm install react-native-video-cover --save`

### Mostly automatic installation

`$ react-native link react-native-video-cover`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-video-cover` and add `RNVideoCover.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNVideoCover.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNVideoCoverPackage;` to the imports at the top of the file
  - Add `new RNVideoCoverPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-video-cover'
  	project(':react-native-video-cover').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-video-cover/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-video-cover')
  	```

#### Windows
[Read it! :D](https://github.com/ReactWindows/react-native)

1. In Visual Studio add the `RNVideoCover.sln` in `node_modules/react-native-video-cover/windows/RNVideoCover.sln` folder to their solution, reference from their app.
2. Open up your `MainPage.cs` app
  - Add `using Cl.Json.RNVideoCover;` to the usings at the top of the file
  - Add `new RNVideoCoverPackage()` to the `List<IReactPackage>` returned by the `Packages` method


## Usage
```javascript
import RNVideoCover from 'react-native-video-cover';

// TODO: What do with the module?
RNVideoCover;
```
  