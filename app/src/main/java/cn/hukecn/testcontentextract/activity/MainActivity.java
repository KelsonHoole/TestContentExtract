package cn.hukecn.testcontentextract.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import cn.hukecn.testcontentextract.R;
import cn.hukecn.testcontentextract.util.ContentExtract;
import cn.hukecn.testcontentextract.view.SuperWebView;

public class MainActivity extends AppCompatActivity {

    SuperWebView webView = null;
    Spinner spinner = null;
    Button btn_parse = null;
    String[] urls = {"http://info.3g.qq.com/g/s?newchannel=news_h_nch&newpos=news_guonei_tab&aid=news_ss&id=news_20160508001510"
            ,"http://news.sina.cn/gj/2016-05-09/detail-ifxryahs0514761.d.html?vt=4&pos=3"
            ,"http://inews.ifeng.com/48523416/news.shtml"
            ,"http://m.blog.csdn.net/article/details?id=51352139"
            ,"http://blog.sina.cn/dpool/blog/s/blog_4120db8b0102wm8k.html?cid=95749&vt=4"
            ,"http://weather1.sina.cn/?code=wuhan&vt=4"
            ,"http://3g.163.com/touch/article.html?docid=BMK8LFK200014TUH&qd=pc_adaptation#163interesting?xssh"
            ,"http://zhidao.baidu.com/question/616999306319060972.html?qbpn=1_1&fr=newsearchlist&word=%E6%AD%A6%E6%B1%89&tx=wiki&aup=1&adown=1&from=0&ssid=0&uid=0&tj=www_normal_1_0_10_title&step=2"
            ,"http://m.toutiao.com/i6282504090249331201/"
            ,"http://m.sohu.com/n/556851092/?wscrid=137_2"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (SuperWebView) findViewById(R.id.webview);
        spinner = (Spinner) findViewById(R.id.sp_urllist);
        btn_parse = (Button) findViewById(R.id.btn_parse);

        String [] mItems = getResources().getStringArray(R.array.urls);
        ArrayAdapter<String> _Adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
        //绑定 Adapter到控件
        spinner.setAdapter(_Adapter);
        _Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                webView.loadUrl(urls[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_parse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.getHtml(new SuperWebView.ReceiveHtmlListener() {
                    @Override
                    public void onReceiveHTML(String url, String html) {
//                        Toast.makeText(getApplicationContext(),html.length()+"",Toast.LENGTH_SHORT).show();

                        List<String> list =Arrays.asList(html.split("\n"));
                        long start = System.currentTimeMillis();
                        JSONObject object = ContentExtract.parse(html);
                        try {
                            String content = object.getString("content");
                            int startLine = object.getInt("start");
                            int endLine = object.getInt("end");
//                            int length = object.getInt("length");
                            int threshold = object.getInt("threshold");
                            int blocksWidth = object.getInt("blocksWidth");
                            int lines = object.getInt("lines");
                            long end = System.currentTimeMillis();
                            long time = end - start;
                            String info = "抽取耗时:"+time+"ms\n";
                            info+="=============================\n";
                            info+= "源码总长度:"+html.length()+'\n';
                            info+="抽取正文长度:"+content.length()+'\n';
                            info+="=============================\n";
                            info+="预处理前源码总行数:"+list.size()+'\n';
                            info+="预处理后源码总行数:"+lines+'\n';
                            info+="正文起始行号:"+startLine+"\n";
                            info+="正文结束行号"+endLine+"\n";
                            int total = endLine - startLine;
                            info+="正文行数:"+total+'\n';
                            info+="=============================\n";
                            info+="块大小:"+blocksWidth+'\n';
                            info+="阈值:"+threshold+'\n';
                            Intent intent = new Intent(MainActivity.this,ResultActivity.class);
                            intent.putExtra("content",content);
                            intent.putExtra("info",info);
                            startActivity(intent);
                        } catch (JSONException e) {

                        }

                    }
                });
            }
        });
    }
}
