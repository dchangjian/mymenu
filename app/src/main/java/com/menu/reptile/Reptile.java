package com.menu.reptile;
import com.menu.addmenu.entity.Step;
import com.menu.model.Ingredient;
import com.menu.model.Menu;
import com.menu.util.Util;

import java.util.ArrayList;
public class Reptile
{
   
   private static ArrayList<String> liList;
   
// 获取img标签正则  
   private static final String IMGURL_REG = "<img.*?src=(.*?)[^>]*?>";  
   // 获取src路径的正则  
   private static final String IMGSRC_REG = "http:\"?(.*?)(\"|>|\\s+)";
   
   //得到网页的<li>标签
   private static final String LI_REG = "<li>[\\s\\S]*?<span>[\\s\\S]*?</li>";
   
   //简介标签,含有<span>标签
   private static final String PROFILE_REG = "<span class=\"search-target\">([\\s\\S]*?)</span>";
   
   //菜谱名称和具体链接正则
   private static final String Name_REG = "<span class=\"img\"[\\s\\S]*?href=[\\s\\S]*?target";
   
   //菜谱标签的正则，获取的是html,需要再获取title=""
   private static final String LABEL_REG = "<p>标签[\\s\\S].*?title(.*?)class=\"search-target\"(.*?)</p>";


   //菜谱详细信息大图
   private static final String FACE_LARGE_REG = "class=\"large\"[\\s\\S]*?/>";
   //获取制作时间
   private static final String MAKE_TIME_REG = "<li>制作时间(.*?)</li>";

   //获取步骤信息，包含图片地址和文字描述
   private static final String STEP_REG = "imit_m[\\s\\S]*?<img[\\s\\S]*?\\d+\\.</em>[\\s\\S]*?</p>";


   //获取步骤文字描述
   private static final String DESC_REG = "/em>[\\s\\S]*?</p>";

   private static final String MAIN_INGREDIENT_DEG = "主料[\\s\\S]*?辅料";
   private static final String ASSISTANT_INGREDIENT_DEG = "辅料[\\s\\S]*?</ul>";

   private static final String MAIN_INGREDIENT_NAME_DEG = "target[\\s\\S]*?<";
   private static final String MAIN_INGREDIENT_USE_DEG = "amount(.*?)<";

   private static final String  ASSISTANT_INGREDIENT_LI_REG = "<li[\\s\\S]*?<p[\\s\\S]*?</li>";
   private static final String ASSISTANT_INGREDIENT_NAME_REG = "name(.*?)<";
   private static final String ASSISTANT_INGREDIENT_USE_REG = "amount(.*?)<";

   /**
    * 获得一页信息
    * @param keyWord 搜索的关键字
    * @param pageNo 当前第几页，用于分页
    * @return 返回一个Menu的集合，只含有基本信息，没有步骤信息
    */
   public ArrayList<Menu> getMenuList(String keyWord,int pageNo){
      try{

         String str =  ReptileUtil.send(generateStrURL(keyWord,pageNo));

         liList = ReptileUtil.fetch(str,LI_REG);

      }
      catch(Exception e){
         e.printStackTrace();
      }

      ArrayList<Menu> menus = new ArrayList<Menu>();
      for(int i = 0; i< liList.size();i++){
         String liHtml = liList.get(i);

         // System.out.println(liHtml);
         Menu menu = new Menu();

         String profileHtml = ReptileUtil.fetch(liHtml,PROFILE_REG).get(0);

         //有的标签为空
         if(!ReptileUtil.fetch(liHtml,LABEL_REG).isEmpty()){
            String labelsHtml = ReptileUtil.fetch(liHtml,LABEL_REG).get(0);
            ArrayList<String> labels = ReptileUtil.fetch(labelsHtml, "title=\".*?\"");
            for(int k = 0; k < labels.size(); k++){
               String label = labels.get(k);
               labels.set(k,label.substring(7,label.length() - 1));
            }
            //获得了标签
            menu.setLabels(labels);
         }

         //获得了简介
         String profile = profileHtml.substring(28,profileHtml.lastIndexOf("<"));
         menu.setProfile(profile);

         //包含了name和src
         String srcAndNameHtml = ReptileUtil.fetch(liHtml,Name_REG).get(0);

         // System.out.println(srcAndNameHtml);

         String src = ReptileUtil.fetch(srcAndNameHtml,IMGSRC_REG).get(0);
         //处理最后一个双引号
         int size = src.length();
         //获得了具体信息的src地址
         menu.setDetailSrc(src.substring(0,size-1));

         String nameHtml = ReptileUtil.fetch(srcAndNameHtml,"title=\"(.*?)\"").get(0);

         //获得了菜谱名
         menu.setName(nameHtml.substring(7,nameHtml.lastIndexOf("\"")));

         String faceUrlHtml = ReptileUtil.fetch(liHtml,IMGURL_REG).get(0);

         //得到faceUrl
         String faceUrl =  ReptileUtil.fetch(faceUrlHtml,IMGSRC_REG).get(0);
         menu.setFaceUrl(faceUrl.substring(0,faceUrl.length()-1));

         menus.add(menu);
      }
      return menus;
   }


   /**
    * 根据menu的基本信息，继续查询所有信息并返回
    */
   public  Menu  getMenuAllInfo(Menu menu){

      String str = null;
      try {
         String url = menu.getDetailSrc();
         //防止访问手机页面
         str = ReptileUtil.send(url);

      } catch (Exception e) {
         e.printStackTrace();
      }

      String makeTimeHtml = ReptileUtil.fetch(str,MAKE_TIME_REG).get(0);

      String makeTime = makeTimeHtml.substring(9,makeTimeHtml.lastIndexOf("<"));
      //设置制作时间

      menu.setMakeTime(makeTime);

      String largeFaceHtml = ReptileUtil.fetch(str,FACE_LARGE_REG).get(0);
      String largeFace = ReptileUtil.fetch(largeFaceHtml,IMGSRC_REG).get(0);
      menu.setFaceUrl(largeFace.substring(0,largeFace.length()-1));

      //包含步骤文字和图片的src
      ArrayList<String> stepsHtml = ReptileUtil.fetch(str,STEP_REG);
      ArrayList<Step> steps = new ArrayList<Step>();
      for(int i = 0; i < stepsHtml.size(); i++){
         Step step = new Step();

         String stepHtml = stepsHtml.get(i);

         String url = ReptileUtil.fetch(stepHtml,IMGSRC_REG).get(0);
         //拿到图片的地址
         step.setUrl(url.substring(0,url.length()-1));

         String descHtml = ReptileUtil.fetch(stepHtml,DESC_REG).get(0);
         //拿到步骤描述
         step.setDesc(descHtml.substring(4,descHtml.indexOf("<")));

         steps.add(step);
      }

      //设置步骤
      menu.setSteps(steps);

      //获得主料相关信息的html
      String mainIngredientHtml = ReptileUtil.fetch(str,MAIN_INGREDIENT_DEG).get(0);
      //获得辅料相关信息的html
      String assistantIngredientHtml = ReptileUtil.fetch(str,ASSISTANT_INGREDIENT_DEG).get(0);

      //拿取主料的相关信息
      ArrayList<String> mainIngredientNames = ReptileUtil.fetch(mainIngredientHtml,MAIN_INGREDIENT_NAME_DEG);
      ArrayList<Ingredient> mainItems = new ArrayList<Ingredient>();
      for(int i = 0; i < mainIngredientNames.size();i++){
         Ingredient item = new Ingredient();
         String name = mainIngredientNames.get(i);

         item.setIngredientName(name.substring(name.lastIndexOf(">")+1,name.lastIndexOf("<")));

         mainItems.add(item);
      }

      ArrayList<String> mainIngredientUse = ReptileUtil.fetch(mainIngredientHtml,MAIN_INGREDIENT_USE_DEG);

      for(int i = 0; i < mainIngredientUse.size();i++){
         String useHtml = mainIngredientUse.get(i);

         String use = useHtml.substring(8,useHtml.lastIndexOf("<"));

         //匹配任意非数字的字符
         mainItems.get(i).setUnit(ReptileUtil.fetch(use,"[^\\d+/]+").get(0));

         if(!ReptileUtil.fetch(use,"(\\d+((.*?)\\d+)*)+").isEmpty()){
            mainItems.get(i).setUseLevel(ReptileUtil.fetch(use,"(\\d+((.*?)\\d+)*)+").get(0));
         }
      }

      //设置主料的所有信息
      menu.setMainIngredient(mainItems);

      //拿取辅料的相关信息
      ArrayList<String> assistantIngredientLiHtml = ReptileUtil.fetch(assistantIngredientHtml,ASSISTANT_INGREDIENT_LI_REG);

      ArrayList<Ingredient> assistantItems = new ArrayList<Ingredient>();
      for(int i= 0; i < assistantIngredientLiHtml.size();i++){

         Ingredient item = new Ingredient();
         String liHtml = assistantIngredientLiHtml.get(i);
         String name = ReptileUtil.fetch(liHtml,ASSISTANT_INGREDIENT_NAME_REG).get(0);
         String useHtml = ReptileUtil.fetch(liHtml,ASSISTANT_INGREDIENT_USE_REG).get(0);
         item.setIngredientName(name.substring(6, name.lastIndexOf("<")));
         String use = useHtml.substring(8,useHtml.lastIndexOf("<"));

         item.setUnit(ReptileUtil.fetch(use, "[^\\d+/]+").get(0));

         if(!ReptileUtil.fetch(use,"(\\d+((.*?)\\d+)*)+").isEmpty()){
            item.setUseLevel(ReptileUtil.fetch(use,"(\\d+((.*?)\\d+)*)+").get(0));
         }
         assistantItems.add(item);
      }

      //设置辅料的所有信息
      menu.setAssistantIngredient(assistantItems);

      return menu;
   }

   /**
    * 根据参数生成 url的字符串
    * @param keyWord
    * @param pageNo
    * @return
    */
   private String generateStrURL(String keyWord,int pageNo){
      StringBuilder strBuilder = new StringBuilder();

      strBuilder.append("http://www.haodou.com/s?wd=");
      strBuilder.append(keyWord);
      strBuilder.append("&tp=recipe&sort=&type=list&p=");
      strBuilder.append(pageNo);

      return strBuilder.toString();
   }
}
