package data;

import com.menu.model.Classify;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/3/24 0024.
 */
public class ClassifyMap {

    private static HashMap<String,Classify> maps;


    public static  HashMap<String,Classify> getMap(){
        if(maps == null){
            maps = new HashMap<String,Classify>();

            maps.put("主食", new Classify("主食",new String[]{"寿司","饼","炒饭","饺子","炒面","馒头","包子","三明治","便当","拌面"}));
            maps.put("菜式", new Classify("菜式",new String[]{"家常菜","素菜","凉菜","汤","煲汤","汤羹","粥","面"}));
            maps.put("菜系", new Classify("菜系",new String[]{"东北菜","湘菜","川菜","鲁菜","粤菜","苏菜","徽菜"}));
        }
        return maps;
    }

}
