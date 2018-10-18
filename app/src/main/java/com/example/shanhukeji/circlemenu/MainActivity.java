package com.example.shanhukeji.circlemenu;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.custom.view.PolygonMenu;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private PolygonMenu vPolygonMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vPolygonMenu=(PolygonMenu)findViewById(R.id.polygon_menu);
        testData();
    }
  /**
   * 此方法用来动态设置数据,务必将数据组装成序
   * */
    private void testData(){
        //数组添加顺序"已鉴定", "待鉴定", "已拒绝", "过期", "好评",以string.xml中appraisal_status为准
        List<String> numberList =new ArrayList<>();
        numberList.add("100");
        numberList.add("200");
        numberList.add("300");
        numberList.add("400");
        numberList.add("500");
        vPolygonMenu.setData(numberList);
    }
}
