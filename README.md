### BottomNavigationView使用
 
 - 把BottomNavigationView添加到Activity布局中


````

<FrameLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
  <com.denghao.control.view.BottomNavigationView
      android:id="@+id/bottom_navigation_view"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      />
</FrameLayout>

````

- 在Activity初始化BottomNavigationView使用

````
  BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
    List<TabItem> items = new ArrayList<TabItem>();
    items.add(new TabView(tabView1, fragment1));
    items.add(new TabView(tabView2, fragment2));
    items.add(new TabView(tabView3, fragment3));
    items.add(new TabView(tabView4, fragment4));
    navigationView.initControl(this).setPagerView(items, 0);
    //设置底部导航栏控制器的高度
    navigationView.getNavgation().setTabControlHeight(60);
    //监听导航栏的切换
       navigationView.getControl().setOnTabClickListener(new TabViewControl.TabClickListener() {
      @Override public void onTabClickListener(int position, View view) {
        
      }
    });

````

- Api参数说明

````
  /**
   * @param view 对应Fragment底部道航栏的View  (控制显示隐藏的控件id必须为tab_control,其他情况只需要是用state_selected的状态选择器即可)
   * @param fragment 当前导航栏对应的Fragment
   */
public TabView(View view, Fragment fragment)

  /**
   * 初始化底部导航栏
   */
public BottomNavigationView initControl(FragmentActivity activity)

 /**
   * 获取整个导航栏容器
   */
 public BottomNavigationControl getNavgation()
 
  /**
   * 获取导航栏控制器
   */
 public TabController getControl()
````
