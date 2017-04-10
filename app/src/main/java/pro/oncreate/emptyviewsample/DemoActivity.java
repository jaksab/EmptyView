package pro.oncreate.emptyviewsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;

import pro.oncreate.emptyview.EmptyView;

public class DemoActivity extends AppCompatActivity {

    private EmptyView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        emptyView = EmptyView.Builder.create(this)
                .where((ViewGroup) findViewById(R.id.layout))
                .empty(new EmptyView.EmptyViewOption("Empty view")) // Use other overloads
                .build();

        emptyView.empty();
        String name = emptyView.getClass().getName();
        Log.d("aadsfasdfsd", name);
    }
}
