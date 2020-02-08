package pt.coloryou.utils;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;


public class PermissionsUtil {


    public static boolean isCamera(Fragment fragment) {
        return (ContextCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED );
    }

    public static boolean isGallery(Fragment fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return ContextCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    public static int askCameraPermissions(Fragment fragment) {
        if (!isCamera(fragment)) {
            if (fragment.shouldShowRequestPermissionRationale(
                    Manifest.permission.CAMERA) || fragment.shouldShowRequestPermissionRationale(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                return -1;
            } else {
                // No explanation needed; request the permission
                fragment.requestPermissions(
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
                return 0;
            }
        }
        return 1;
    }

    public static int askGalleryPermission(Fragment fragment) {
        if (!isGallery(fragment)) {
            if (fragment.shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                return -1;
            } else {
                // No explanation needed; request the permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    fragment.requestPermissions(
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            1);
                }
                return 0;
            }
        }
        return 1;
    }




}
