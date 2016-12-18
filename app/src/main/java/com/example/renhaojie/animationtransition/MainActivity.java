package com.example.renhaojie.animationtransition;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.transition.Explode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends Activity {

    @BindView(R.id.album_list)
    RecyclerView mAlbumList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpTransition();
        ButterKnife.bind(this);
        populate();
    }

    private void setUpTransition() {
        getWindow().setReenterTransition(null);
        getWindow().setExitTransition(new Explode());
    }

    interface OnVHClickedListener {
        void onVHClicked(AlbumVH vh);
    }

    static class AlbumVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final OnVHClickedListener listener;
        @BindView(R.id.album_art)
        ImageView albumArt;

        public AlbumVH(View itemView, OnVHClickedListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            listener.onVHClicked(this);
        }
    }

    private void populate() {
        StaggeredGridLayoutManager lm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mAlbumList.setLayoutManager(lm);

        final int[] albumArts = {
                R.drawable.mean_something_kinder_than_wolves,
                R.drawable.cylinders_chris_zabriskie,
                R.drawable.broken_distance_sutro,
                R.drawable.playing_with_scratches_ruckus_roboticus,
                R.drawable.keep_it_together_guster,
                R.drawable.the_carpenter_avett_brothers,
                R.drawable.please_sondre_lerche,
                R.drawable.direct_to_video_chris_zabriskie};


        RecyclerView.Adapter adapter = new RecyclerView.Adapter<AlbumVH>() {

            @Override
            public AlbumVH onCreateViewHolder(ViewGroup parent, int viewType) {
                View albumView = getLayoutInflater().inflate(R.layout.album_grid_item, parent, false);

                return new AlbumVH(albumView, new OnVHClickedListener() {
                    @Override
                    public void onVHClicked(AlbumVH vh) {
                        int albumId = albumArts[vh.getPosition() % albumArts.length];
                        Intent intent = new Intent(MainActivity.this, AlbumDetailActivity.class);

                        intent.putExtra(AlbumDetailActivity.EXTRA_ALBUM_ART_RESID, albumId);
                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, vh.albumArt, "albumArt");
                        startActivity(intent, options.toBundle());
//                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onBindViewHolder(AlbumVH holder, int position) {
//                holder.albumArt.setImageBitmap(decodeSampleBitmapFromResource(holder, position, albumArts));
                holder.albumArt.setImageResource(albumArts[position % albumArts.length]);
            }

            @Override
            public int getItemCount() {
                return albumArts.length * 4;
            }
        };
        mAlbumList.setAdapter(adapter);
    }

    private Bitmap decodeSampleBitmapFromResource(AlbumVH holder, int position, int[] albumArts) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), albumArts[position % albumArts.length], options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        String imageType = options.outMimeType;

        int requiredWidth = holder.albumArt.getMeasuredWidth();
        int requiredHeight = holder.albumArt.getMeasuredHeight();

        options.inSampleSize = calculateInSampleSize(options, 100, 100);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(getResources(), albumArts[position % albumArts.length], options);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int requiredWidth, int requiredHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > requiredHeight || width > requiredWidth) {
            final int halfWidth = width / 2;
            final int halfHeight = height / 2;

            while ((halfHeight / inSampleSize >= requiredHeight) && (halfWidth / inSampleSize >= requiredWidth))
                inSampleSize *= 2;
        }
        return inSampleSize;
    }
}
