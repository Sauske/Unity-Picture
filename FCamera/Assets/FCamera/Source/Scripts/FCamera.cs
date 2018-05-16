using UnityEngine;
using System.Collections;
using System;

namespace FCamera
{
    public class FCamera : MonoBehaviour
    {
        private FCamera inst = null;

        private void Awake()
        {
            inst = this;
            DontDestroyOnLoad(this);
        }

        private void CutResult(int result)
        {
            throw new NotImplementedException();
        }
        
        private void SelectPhotoResults(int result)
        {
            throw new NotImplementedException();
        }

        public static void TakePhoto()
        {
#if UNITY_ANDROID
            AndroidJavaClass jc = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
            var obj = jc.GetStatic<AndroidJavaObject>("currentActivity");
            obj.Call("TakePhoto");
#elif UNITY_IPHONE
#endif
        }

        private void TakePhotoResults(int result)
        {
            throw new NotImplementedException();
        }

        public static void RecordVideo()
        {
#if UNITY_ANDROID
            AndroidJavaClass jc = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
            var obj = jc.GetStatic<AndroidJavaObject>("currentActivity");
            obj.Call("RecordVideo");
#elif UNITY_IPHONE
#endif
        }

        private void RecordVideoResults(int result)
        {
            throw new NotImplementedException();
        }
    }
}
