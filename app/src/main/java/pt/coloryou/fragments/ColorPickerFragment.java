package pt.coloryou.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pt.coloryou.R;
import pt.coloryou.enums.ErrorEnum;
import pt.coloryou.enums.FragmentsEnum;
import pt.coloryou.utils.PermissionsUtil;

public class ColorPickerFragment extends Fragment {

    static final int REQUEST_GET_GALLERY_PHOTO = 1;
    static final int REQUEST_TAKE_PHOTO = 2;
    static final int RESULT_OK = -1;
    static ColorPickerFragment colorPickerFragment;
    static String currentPhotoPath;
    static String pickedColor;

    ImageButton btnCamera;
    ImageButton btnGallery;
    Button btnColor;
    ImageView imgShowImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // TODO Ask for camera permission

        View view = inflater.inflate(R.layout.color_picker_fragment, container, false);

        // Initialize Layout Listeners
        btnCamera = view.findViewById(R.id.btn_camera);
        btnGallery = view.findViewById(R.id.btn_gallery);
        btnColor = view.findViewById(R.id.btn_color);
        imgShowImage = view.findViewById(R.id.img_show_image);

        // Set ImageView Settings
        imgShowImage.setDrawingCacheEnabled(true);
        imgShowImage.buildDrawingCache(true);
        imgShowImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                    Bitmap bitmap = imgShowImage.getDrawingCache();
                    int pixel = bitmap.getPixel((int) event.getX(), (int) event.getY());

                    int r = Color.red(pixel);
                    int g = Color.green(pixel);
                    int b = Color.blue(pixel);

                    pickedColor = ("#" + String.format("%02X", r) + String.format("%02X", g) + String.format("%02X", b));

                    btnColor.setBackgroundColor(Color.parseColor(pickedColor));

                }
                return false;
            }
        });

        // On Click open Camera
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int res = PermissionsUtil.askCameraPermissions(colorPickerFragment);
                if (res == 1) {
                    dispatchTakePictureIntent();
                } else if (res == -1) {
                    Toast.makeText(getContext(), "Color You não tem autorização para utilizar a câmara.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // On Click open Gallery
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int res = PermissionsUtil.askGalleryPermission(colorPickerFragment);
                if (res == 1)
                    openGallery();
                else if (res == -1)
                    Toast.makeText(getContext(), "Color You não tem autorização para utilizar a galeria.", Toast.LENGTH_SHORT).show();
            }
        });

        // On Click open Color Information
        btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("color", pickedColor);
                Fragment colorFragment = new ColorFragment();
                colorFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().hide(getActivity().getSupportFragmentManager().findFragmentByTag(FragmentsEnum.COLOR_PICKER_FRAGMENT.getValor())).commit();
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, colorFragment, FragmentsEnum.COLOR_FRAGMENT.getValor()).commit();
            }
        });

        // If phone orientation changed
        if (savedInstanceState != null) {

            // Image already set
            if (currentPhotoPath != null) setPic();
            // Color Already Picked
        } else {
            currentPhotoPath = null;
            pickedColor = "#ffffff";
        }

        btnColor.setBackgroundColor(Color.parseColor(pickedColor));

        colorPickerFragment = this;
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // Set taken picture
                case REQUEST_TAKE_PHOTO:
                    setPic();
                    break;
                // Set picture get from gallery
                case REQUEST_GET_GALLERY_PHOTO:
                    currentPhotoPath = getRealPathFromURI(getActivity(), data.getData());
                    setPic();
                    break;
            }
        }
    }

    /* GENERAL Methods */

    private void setPic() {

        int scaleFactor;

        // Get the dimensions of the Image View
        int targetW = imgShowImage.getWidth();
        int targetH = imgShowImage.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        try {
            scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        } catch (ArithmeticException e) {
            scaleFactor = 0;
        }

        // Decode the image file into color_add_white Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        try {
            imgShowImage.destroyDrawingCache();
            imgShowImage.setImageBitmap(orientateImage(currentPhotoPath, bitmap));
        } catch (Exception e) {
            Toast.makeText(getActivity(), ErrorEnum.CREATE_IMAGE.getValor(), Toast.LENGTH_LONG).show();
        }


    }

    /* GALLERY Methods */

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, REQUEST_GET_GALLERY_PHOTO);
    }

    private String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /* CAMERA Methods */


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's color_add_white camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(getActivity(), ErrorEnum.CREATE_IMAGE.getValor(), Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "pt.coloryou",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save color_add_white file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // Set Image Orientation Methods
    private Bitmap orientateImage(String photoPath, Bitmap bitmap) throws Exception {
        ExifInterface ei = new ExifInterface(photoPath);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotatedBitmap = null;
        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }
        return rotatedBitmap;
    }

    private Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                }
            }
            break;
            case 2: {
                openGallery();
            }
            break;
        }
    }
}
