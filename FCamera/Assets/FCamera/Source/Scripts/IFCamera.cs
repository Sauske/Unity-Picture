using UnityEngine;
using System.Collections;

namespace FCamera
{
    // 回调结果类型
    public enum FResultState
    {
        Succeed,
        Cancel,
        Error,
    }

    public interface FCameraInterface
    {
        // 照相回调
        void TakePhotoResults(int result);
        // 录制视频回调
        void RecordVideoResults(int result);
        // 选择照片回调
        void SelectPhotoResults(int result);
        // 裁切图片回调
        void CutResult(int result);
    }

}
