package sgt.hansung.com.suh_gongteo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by MinJeong on 2015-10-26.
 */
public class RecyclerInterestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ParseObject> favorite;
    private Bitmap bmp, basicBmp;
    private byte[] bmpbytes;
    private LayoutInflater mInflater;
    private String curAddress;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    public class InterestListViewHolder extends ViewHolder {
        TextView spaceTitle, spaceDate, spaceFee;
        ImageView imageView;
        LinearLayout layout;
        Button btnCancel;

        public InterestListViewHolder(View v) {
            super(v);
            this.spaceTitle = (TextView) v.findViewById(R.id.space_name_interest);
            this.spaceDate = (TextView) v.findViewById(R.id.space_date_interest);
            this.spaceFee = (TextView) v.findViewById(R.id.space_fee_interest);
            this.imageView = (ImageView) v.findViewById(R.id.imageView_interest);
            this.layout = (LinearLayout) v.findViewById(R.id.layout_card_view);
            this.btnCancel = (Button) v.findViewById(R.id.interest_cancel);

            BitmapDrawable drawable = (BitmapDrawable) v.getResources().getDrawable(R.drawable.basic);
            basicBmp = drawable.getBitmap();
        }
    }

    // Constructor
    public RecyclerInterestAdapter(ArrayList<ParseObject> dataSet, LayoutInflater inflater) {
        this.favorite = dataSet;
        this.mInflater = inflater;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.content_my_interest_card, viewGroup, false);

        return new InterestListViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {

        InterestListViewHolder holder = (InterestListViewHolder) viewHolder;
        try {
            ParseFile imageFile = (ParseFile) favorite.get(position).get("image1");
            bmpbytes = imageFile.getData();
            bmp = BitmapFactory.decodeByteArray(bmpbytes, 0, bmpbytes.length);
        } catch (NullPointerException ne) {
            bmp = basicBmp;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.imageView.setImageBitmap(bmp);

        curAddress = favorite.get(position).getString("address").toString();
        holder.spaceTitle.setText(favorite.get(position).getString("spaceTitle").toString());
        SimpleDateFormat format = new SimpleDateFormat("MM월 dd일 HH:mm");
        holder.spaceDate.setText(format.format(favorite.get(position).get("StartTime"))
                + " ~ " + format.format(favorite.get(position).get("EndTime")));
        holder.spaceFee.setText(favorite.get(position).getString("price").toString() + "원");

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mInflater.getContext(), Detail.class);
                intent.putExtra("address", curAddress);
                mInflater.getContext().startActivity(intent);

            }
        });
        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorite");
                query.whereEqualTo("address", favorite.get(position).getString("address"));
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {
                        parseObject.deleteInBackground();
                        Toast.makeText(mInflater.getContext(), "찜 삭제되었습니다.", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(mInflater.getContext(), MyInterest.class);
                        mInflater.getContext().startActivity(intent);
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return favorite.size();
    }



}