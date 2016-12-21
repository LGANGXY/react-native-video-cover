#import <AVFoundation/AVFoundation.h>
#import "RNVideoCover.h"
#import "RCTLog.h"
#import <CommonCrypto/CommonDigest.h>

@implementation RNVideoCover
{
    NSString* imagePath;
}

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

RCT_EXPORT_MODULE(RNVideoCover)


//Promise
RCT_REMAP_METHOD(getVideoCover,
                 path:(NSString *)path
                 resolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject) {
    
    NSURL *url = [NSURL URLWithString:path];
    AVURLAsset *urlSet = [AVURLAsset assetWithURL:url];
    AVAssetImageGenerator *imageGenerator = [AVAssetImageGenerator assetImageGeneratorWithAsset:urlSet];
    
    NSError *error = nil;
    CMTime time = CMTimeMake(0,1);//缩略图创建时间 CMTime是表示电影时间信息的结构体，第一个参数表示是视频第几秒，第二个参数表示每秒帧数.(如果要活的某一秒的第几帧可以使用CMTimeMake方法)
    CMTime actucalTime; //缩略图实际生成的时间
    CGImageRef cgImage = [imageGenerator copyCGImageAtTime:time actualTime:&actucalTime error:&error];
    if (error) {
        NSLog(@"截取视频图片失败:%@ --%@  ==%@",error.localizedDescription,path, urlSet.metadata);
        reject(@"-1001", @"截取视频图片失败", nil);
    }
    else{
        CMTimeShow(actucalTime);
        UIImage *image = [UIImage imageWithCGImage:cgImage];
        UIImageWriteToSavedPhotosAlbum(image,nil, nil,nil);
        NSString* imageNameStr = [self getmd5WithString:path];
        NSString* imageName = [NSString stringWithFormat:@"/Documents/%@.png",imageNameStr];
        NSString *path_sandox = NSHomeDirectory();
        imagePath = [path_sandox stringByAppendingString:imageName];
        //把图片直接保存到指定的路径（同时应该把图片的路径imagePath存起来，下次就可以直接用来取）
        [UIImagePNGRepresentation(image) writeToFile:imagePath atomically:YES];
        CGImageRelease(cgImage);
        
        float second = 0;
        second = urlSet.duration.value/urlSet.duration.timescale;
        NSNumber *num = [NSNumber numberWithFloat:second];
        
        NSLog(@"视频截取成功  -- %@  --长度-%f",imagePath,second);
        NSMutableDictionary * dict = [NSMutableDictionary dictionary];
        [dict setObject:num forKey:@"duration"];
        [dict setObject:imagePath forKey:@"thumbnail"];
        resolve(dict);
    }
}

-(NSString*)getmd5WithString:(NSString *)string

{
    
    const char* original_str=[string UTF8String];
    
    unsigned char digist[CC_MD5_DIGEST_LENGTH]; //CC_MD5_DIGEST_LENGTH = 16
    
    CC_MD5(original_str, strlen(original_str), digist);
    
    NSMutableString* outPutStr = [NSMutableString stringWithCapacity:10];
    
    for(int  i =0; i<CC_MD5_DIGEST_LENGTH;i++){
        
        [outPutStr appendFormat:@"%02x", digist[i]];//小写x表示输出的是小写MD5，大写X表示输出的是大写MD5
        
    }
    
    return [outPutStr lowercaseString];
    
}


@end

