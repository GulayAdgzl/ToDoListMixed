package com.example.shoppingtodolist.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.shoppingtodolist.db.AppDatabase;
import com.example.shoppingtodolist.db.Category;

import java.util.List;

public class MainActivityViewModel  extends AndroidViewModel {
    private MutableLiveData<List<Category>> listOfCategory;
    private AppDatabase appDatabase;
    public MainActivityViewModel(Application application){
        super(application);
        listOfCategory=new MutableLiveData<> ();
        appDatabase=AppDatabase.getDBinstance (getApplication ().getApplicationContext ());

    }
    public  MutableLiveData<List<Category>>getCategoryListObserve(){

         return  listOfCategory;
    }
    public  void getAllCategoryList(){
        List<Category> categoryList=appDatabase.shoppingListDao().getAllCategoriesList ();
        if(categoryList.size ()>0){
            listOfCategory.postValue (categoryList);
        }
        else{
            listOfCategory.postValue (null);
        }
    }




    public  void insertCategory(String catName){
        Category category=new Category ();
        category.categoryName=catName;
        appDatabase.shoppingListDao().insertCategory(category);
        getAllCategoryList ();

    }
    public  void updateCategory(Category category){

        appDatabase.shoppingListDao().updateCategory (category);
        getAllCategoryList ();
    }
    public  void deleteCategory(Category category){

        appDatabase.shoppingListDao().deleteCategory (category);
        getAllCategoryList ();
    }



}
