package com.mh2z.activity;

import com.mh2z.adapter.PictureAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private GridView gridview;
	private String[] titles = new String[] { "1", "2", "3", "4", "5", "6", "7",
			"8", "9" };
	private int[] images = new int[] { R.drawable.b3, R.drawable.b3,
			R.drawable.b3, R.drawable.b3, R.drawable.b3, R.drawable.b3,
			R.drawable.b3, R.drawable.b3, R.drawable.aa };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		gridview = (GridView) findViewById(R.id.gridView);
		
		PictureAdapter adapter = new PictureAdapter(titles, images, this);
		gridview.setAdapter(adapter);

		gridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Toast.makeText(MainActivity.this, "pic" + (position + 1),
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
