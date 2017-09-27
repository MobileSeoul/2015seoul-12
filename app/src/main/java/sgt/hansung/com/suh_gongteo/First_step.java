package sgt.hansung.com.suh_gongteo;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.IOException;

import nl.changer.polypicker.ImagePickerActivity;


/**
 * Created by user on 2015-10-21.
 */

//이미지, 제목, 상세설명
public class First_step extends Fragment {

    private int INTENT_REQUEST_GET_IMAGES = 1;
    public EditText spaceTitle, spaceDetail;
    public Button spaceType;
    public ImageView[] spaceImage;
    public ImageView[] imageRemove;
    public Bitmap[] bitmap;
    public Bitmap default_image;
    private Uri uri=null;
    private String bitmapName = null;
    private int imageNum;

    public First_step() {
        imageNum = 0;
        spaceImage = new ImageView[4];
        imageRemove = new ImageView[4];
        bitmap = new Bitmap[4];
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_first_step, container, false);
        default_image = BitmapFactory.decodeResource(getResources(), R.drawable.maindefault);

        spaceTitle = (EditText)linearLayout.findViewById(R.id.edit_spaceTitle);
        spaceDetail = (EditText)linearLayout.findViewById(R.id.edit_spaceDetail);
        spaceImage[0] = (ImageView)linearLayout.findViewById(R.id.spaceImage0);
        spaceImage[1] = (ImageView)linearLayout.findViewById(R.id.spaceImage1);
        spaceImage[2] = (ImageView)linearLayout.findViewById(R.id.spaceImage2);
        spaceImage[3] = (ImageView)linearLayout.findViewById(R.id.spaceImage3);

        imageRemove[0] = (ImageView)linearLayout.findViewById(R.id.remove0);
        imageRemove[1] = (ImageView)linearLayout.findViewById(R.id.remove1);
        imageRemove[2] = (ImageView)linearLayout.findViewById(R.id.remove2);
        imageRemove[3] = (ImageView)linearLayout.findViewById(R.id.remove3);
        imageRemove[0].setVisibility(View.GONE);
        imageRemove[1].setVisibility(View.GONE);
        imageRemove[2].setVisibility(View.GONE);
        imageRemove[3].setVisibility(View.GONE);

//        spaceImage[0].setOnClickListener((View.OnClickListener) this);
//        spaceImage[1].setOnClickListener((View.OnClickListener) this);
//        spaceImage[2].setOnClickListener((View.OnClickListener) this);
//        spaceImage[3].setOnClickListener((View.OnClickListener) this);



        spaceImage[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_select(v);
                imageNum = 0;
            }
        });
        spaceImage[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_select(v);
                imageNum = 1;
            }
        });
        spaceImage[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_select(v);
                imageNum = 2;
            }
        });
        spaceImage[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_select(v);
                imageNum = 3;
            }
        });

        imageRemove[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap[0] = default_image;
                imageRemove[0].setVisibility(View.GONE);
                spaceImage[0].setImageBitmap(bitmap[0]);
            }
        });

        imageRemove[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap[1] = default_image;
                imageRemove[1].setVisibility(View.GONE);
                spaceImage[1].setImageBitmap(bitmap[1]);
            }
        });
        imageRemove[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap[2] = default_image;
                imageRemove[2].setVisibility(View.GONE);
                spaceImage[2].setImageBitmap(bitmap[2]);
            }
        });

        imageRemove[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap[3] = default_image;
                imageRemove[3].setVisibility(View.GONE);
                spaceImage[3].setImageBitmap(bitmap[3]);
            }
        });
        return linearLayout;
    }

    //    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.spaceImage0:
//                image_select(v);
//                imageNum = 0;
//                break;
//            case R.id.spaceImage1:
//                image_select(v);
//                imageNum = 1;
//                break;
//            case R.id.spaceImage2:
//                image_select(v);
//                imageNum = 2;
//                break;
//            case R.id.spaceImage3:
//                image_select(v);
//                imageNum = 3;
//                break;
//            default :
//
//        }
//    }
    public void image_select(View view) {
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.EXTRA_SELECTION_LIMIT, 1);
        startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == INTENT_REQUEST_GET_IMAGES) {
                Parcelable[] parcelableUris = intent.getParcelableArrayExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
                if (parcelableUris == null) {
                    return;
                }
                Uri[] uris = new Uri[parcelableUris.length];
                System.arraycopy(parcelableUris, 0, uris, 0, parcelableUris.length);

                if (uris != null) {
                    imageRemove[imageNum].setVisibility(View.VISIBLE);
                    imageNum = setImage(imageNum,uris);

                }
            }
        }
    }
    public int setImage(int imageNum, Uri[] uris){

        //First_step tmp = (First_step) mItems.get(FIRST_STEP);
        if(uris == null) {
            bitmap[imageNum] = null;
            spaceImage[imageNum].setImageBitmap(bitmap[imageNum]);
            return imageNum;
        }else{
            for (Uri u : uris) {
                u = Uri.parse("file://" + u);
                uri = u;
                bitmapName = getImageNameToUri(uri);
                try {
                    bitmap[imageNum] = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    bitmap[imageNum] = resizeBitmapImageFn(bitmap[imageNum], 600);
                    spaceImage[imageNum].setImageBitmap(bitmap[imageNum]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageNum++;
            }
            return imageNum;
        }
    }
    public Bitmap resizeBitmapImageFn(
            Bitmap bmpSource, int maxResolution){
        int iWidth = bmpSource.getWidth();
        int iHeight = bmpSource.getHeight();
        int newWidth = iWidth ;
        int newHeight = iHeight ;
        float rate = 0.0f;

        if(iWidth > iHeight ){
            if(maxResolution < iWidth ){
                rate = maxResolution / (float) iWidth ;
                newHeight = (int) (iHeight * rate);
                newWidth = maxResolution;
            }
        }else{
            if(maxResolution < iHeight ){
                rate = maxResolution / (float) iHeight ;
                newWidth = (int) (iWidth * rate);
                newHeight = maxResolution;
            }
        }

        return Bitmap.createScaledBitmap(
                bmpSource, newWidth, newHeight, true);
    }
    public String getImageNameToUri(Uri data) {
        String imgPath = data.toString();
        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);
        return imgName;
    }


}