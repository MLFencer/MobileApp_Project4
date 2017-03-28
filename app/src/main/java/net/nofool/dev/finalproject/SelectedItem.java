package net.nofool.dev.finalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class SelectedItem extends AppCompatActivity {

    private String name, id, message;

    TextView nameTV, idTV, messageTV;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_item);

        nameTV = (TextView)findViewById(R.id.name);
        idTV = (TextView)findViewById(R.id.id);
        messageTV = (TextView)findViewById(R.id.message);
        image = (ImageView)findViewById(R.id.imageView);

        nameTV.setText(getIntent().getStringExtra(fragmentforlist.EXTRA_TITLE));
        idTV.setText(getIntent().getStringExtra(fragmentforlist.EXTRA_ID));
        messageTV.setText(getIntent().getStringExtra(fragmentforlist.EXTRA_MESSAGE));

        String st = getIntent().getStringExtra(fragmentforlist.EXTRA_MESSAGE);
        if (Double.parseDouble(st)<0.0){
            image.setImageResource(R.drawable.ic_trending_down);
        } else if (Double.parseDouble(st)>0.0){
            image.setImageResource(R.drawable.ic_action_trending_up);
        } else {
            image.setImageResource(R.drawable.ic_action_trending_neutral);
        }
    }
}
