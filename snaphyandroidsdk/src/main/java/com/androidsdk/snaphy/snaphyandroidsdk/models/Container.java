package com.androidsdk.snaphy.snaphyandroidsdk.models;







import java.util.HashMap;
import java.util.Map;

/*
Replacing with custom Snaphy callback methods
import com.strongloop.android.loopback.callbacks.ListCallback;
import com.strongloop.android.loopback.callbacks.ObjectCallback;
import com.strongloop.android.loopback.callbacks.VoidCallback;
*/
//Import self repository..
//Now import repository of related models..



public class Container extends Model {


    //For converting all model values to hashMap
    private  transient Map<String, Object> hashMap = new HashMap<>();

    public Map<String,  ? extends Object> convertMap(){
        if(that.getId() != null){
            return hashMap;
        }else{
            hashMap.put("id", that.getId());
            return hashMap;
        }
    }

    private Container that ;

    public Container (){
        that = this;
    }



            
            
            
            
            

        
    


    



    //Now adding relations between related models
      

}
