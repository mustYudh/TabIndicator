package com.denghao.tabindicatordemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.denghao.tabindicatorview.TabIndicator;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    final TabIndicator contentView1 = (TabIndicator) findViewById(R.id.test1);
    List<String> data = new ArrayList<>();
    data.add("您的好友");
    data.add("优秀店主");
    contentView1.setPadding(5).setData( R.color.colorAccent, R.color.colorPrimary,data);
  }
}
