package com.menu.reptile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ReptileUtil
{
   /**
    * 根据传入的url返回一个网页字符串
    * @param desUrl
    * @return
    * @throws Exception
    */
   public static String send(String desUrl) throws Exception{
     
      URL url = new URL(desUrl);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();

      conn.setInstanceFollowRedirects(false);
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
     //设置代理，禁止重定向到手机端页面
      conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; ru; rv:1.9.0.11) Gecko/2009060215 Firefox/3.0.11 (.NET CLR 3.5.30729)");
      conn.setRequestProperty("Content-Language", "en-US");
      conn.setUseCaches(false);
      conn.setDoInput(true);
      conn.setDoOutput(true);

      StringBuilder strBuilder = new StringBuilder();
      
      InputStream  is = (InputStream) conn.getInputStream();
      
      BufferedReader bf = new BufferedReader(new InputStreamReader(is));
      String str = null;
      while(null != (str = bf.readLine())){
         strBuilder.append(str);
      }
      return strBuilder.toString();
   }

   /**
    * 根据传入的正则查找符合要求的字符串
    * @param htmlString
    * @param reg 正则表达式
    * @return
    */
   public static ArrayList<String>  fetch(String htmlString,String reg){
      Pattern p = Pattern.compile(reg);
      Matcher m = p.matcher(htmlString);
      ArrayList<String> result=new ArrayList<String>();
      while(m.find()){
         result.add(m.group());
      }
      return result;
   }
}
