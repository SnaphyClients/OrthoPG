package com.androidsdk.snaphy.snaphyandroidsdk.models;







import org.json.JSONObject;
import org.json.JSONArray;

import java.util.List;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.remoting.adapters.Adapter;
import android.content.Context;

/*
Replacing with custom Snaphy callback methods
import com.strongloop.android.loopback.callbacks.ListCallback;
import com.strongloop.android.loopback.callbacks.ObjectCallback;
import com.strongloop.android.loopback.callbacks.VoidCallback;
*/
import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.ObjectCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.DataListCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.VoidCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;

//Import self repository..
import com.androidsdk.snaphy.snaphyandroidsdk.repository.BookDetailRepository;

//Now import repository of related models..

    
            import com.androidsdk.snaphy.snaphyandroidsdk.repository.BookRepository;
            

        
    


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class BookDetail extends Model {


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

    private BookDetail that ;

    public BookDetail (){
        that = this;
    }

    
        
            

            
                private Map<String, Object> bookPdf;
                /* Adding Getter and Setter methods */
                public Map<String, Object> getBookPdf(){
                    return bookPdf;
                }

                /* Adding Getter and Setter methods */
                public void setBookPdf(Map<String, Object> bookPdf){
                    this.bookPdf = bookPdf;
                    //Update Map value..
                    hashMap.put("bookPdf", bookPdf);
                }

            
            
        
    
        
            

            
                private String added;
                /* Adding Getter and Setter methods */
                public String getAdded(){
                    return added;
                }

                /* Adding Getter and Setter methods */
                public void setAdded(String added){
                    this.added = added;
                    //Update hashMap value..
                    hashMap.put("added", added);
                }

            
            
        
    
        
            

            
            
        
    
        
            

            
            
        
    


    //------------------------------------Database Method---------------------------------------------------


    public void save(final com.strongloop.android.loopback.callbacks.VoidCallback callback){
      //Save to database..
      save__db();
      //Also save to database..
      super.save(callback);
    }

    public void destroy(final com.strongloop.android.loopback.callbacks.VoidCallback callback){
      BookDetailRepository lowercaseFirstLetterRepository = (BookDetailRepository) getRepository();
      if(lowercaseFirstLetterRepository.isSTORE_LOCALLY()){
          //Delete from database..
          String id = getId().toString();
          if(id != null && lowercaseFirstLetterRepository.getDb() != null){
             lowercaseFirstLetterRepository.getDb().delete__db(id);
          }
      }
      //Also save to database..
      super.destroy(callback);
    }



    public void save__db(String id){
      BookDetailRepository lowercaseFirstLetterRepository = (BookDetailRepository) getRepository();

      if(lowercaseFirstLetterRepository.isSTORE_LOCALLY()){
        if(id != null && lowercaseFirstLetterRepository.getDb() != null){
          lowercaseFirstLetterRepository.getDb().upsert__db(id, this);
        }
      }
    }


    public void delete__db(){
      BookDetailRepository lowercaseFirstLetterRepository = (BookDetailRepository) getRepository();
      if(lowercaseFirstLetterRepository.isSTORE_LOCALLY()){

        if(getId() != null && lowercaseFirstLetterRepository.getDb() != null){
            String id = getId().toString();
          lowercaseFirstLetterRepository.getDb().delete__db(id);
        }
      }
    }


    public void save__db(){
      if(getId() == null){
        return;
      }
      String id = getId().toString();
      save__db(id);
    }



//-----------------------------------END Database Methods------------------------------------------------


    




    //Now adding relations between related models
    
        
        
                
                    //Define belongsTo relation method here..
                    private transient Book  book ;
                    private String bookId;

                    public String getBookId(){
                         return bookId;
                    }

                    public void setBookId(Object bookId){
                        if(bookId != null){
                          this.bookId = bookId.toString();
                        }
                    }

                    public Book getBook() {
                        try{
                          //Adding database method for fetching from relation if not present..
                                      if(book == null){
                                        BookDetailRepository bookDetailRepository = (BookDetailRepository) getRepository();

                                        RestAdapter restAdapter = bookDetailRepository.getRestAdapter();
                                        if(restAdapter != null){
                                          //Fetch locally from db
                                          book = getBook__db(restAdapter);
                                        }
                                      }
                        }catch(Exception e){
                          //Ignore
                        }

                        return book;
                    }

                    public void setBook(Book book) {
                        this.book = book;
                    }

                    //Adding related model automatically in case of include statement from server..
                    public void setBook(Map<String, Object> book) {
                        //First create a dummy Repo class object for customer.
                        BookRepository bookRepository = new BookRepository();
                        Book book1 = bookRepository.createObject(book);
                        setBook(book1);
                    }

                    //Adding related model automatically in case of include statement from server..
                    public void setBook(HashMap<String, Object> book) {
                        //First create a dummy Repo class object for customer.
                        BookRepository bookRepository = new BookRepository();
                        Book book1 = bookRepository.createObject(book);
                        setBook(book1);
                    }

                    //Adding relation method..
                    public void addRelation(Book book) {
                        that.setBook(book);
                    }


                    //Fetch related data from local database if present a bookId identifier as property for belongsTo
                    public Book getBook__db(RestAdapter restAdapter){
                      if(bookId != null){
                        BookRepository bookRepository = restAdapter.createRepository(BookRepository.class);
                            try{
                            BookDetailRepository lowercaseFirstLetterRepository = (BookDetailRepository) getRepository();
                                          if(lowercaseFirstLetterRepository.isSTORE_LOCALLY()){
                                                Context context = lowercaseFirstLetterRepository.getContext();
                                                if(bookRepository.getDb() == null ){
                                                    bookRepository.addStorage(context);
                                                }

                                                if(context != null && bookRepository.getDb() != null){
                                                    bookRepository.addStorage(context);
                                                    Book book = (Book) bookRepository.getDb().get__db(bookId);
                                                    return book;
                                                }else{
                                                    return null;
                                                }
                                          }else{
                                            return null;
                                          }
                            }catch(Exception e){
                            //Ignore exception..
                            return null;
                            }

                        }else{
                          return null;
                      }
                    }
                

                
                







                    //Now add instance methods to fetch the related belongsTo Model..
                    

                    

                                    //Write the method here..
                                    public void get__book( Boolean refresh,  RestAdapter restAdapter, final ObjectCallback<Book> callback) {
                                        //Call the onBefore callback method..
                                        callback.onBefore();

                                        //Define methods here..
                                        final BookDetailRepository  bookDetailRepo = restAdapter.createRepository(BookDetailRepository.class);
                                        
                                        
                                        
                                        
                                        



                                        bookDetailRepo.get__book( (String)that.getId(), refresh,  new ObjectCallback<Book> (){
                                            

                                            
                                                @Override
                                                
                                                    public void onSuccess(Book object) {
                                                        if(object != null){
                                                            //now add relation to this recipe.
                                                            addRelation(object);
                                                            //Also add relation to child type for two way communication..Removing two way communication for cyclic error
                                                            //object.addRelation(that);
                                                            callback.onSuccess(object);
                                                            //Calling the finally..callback
                                                            callback.onFinally();
                                                        }else{
                                                            callback.onSuccess(null);
                                                            //Calling the finally..callback
                                                            callback.onFinally();
                                                        }

                                                    }
                                                
                                            


                                            

                                            @Override
                                            public void onError(Throwable t) {
                                                //Now calling the callback
                                                callback.onError(t);
                                                //Calling the finally..callback
                                                callback.onFinally();
                                            }

                                        });
                                    } //method def ends here.
                                 
                            
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                    

                

                 
                 
             
          
      

}
