package com.example.shoppingtodolist;

import android.content.Intent;
import android.os.Bundle;

import com.example.shoppingtodolist.db.Category;
import com.example.shoppingtodolist.viewmodel.MainActivityViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements CategoryListAdapter.HandleCategoryClick {
    private MainActivityViewModel viewModel;
    private TextView noResulttextView;
    private RecyclerView recyclerView;
    private CategoryListAdapter categoryListAdapter;
    private  Category categoryForEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        getSupportActionBar ().setTitle ("Shopping To Do List");
       noResulttextView= findViewById (R.id.noResult);

       recyclerView= findViewById (R.id.recyclerView);
        ImageView addNew=findViewById (R.id.addNewCategoryImageView);
        addNew.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                showAddCategoryDialog(false);
            }
        });

        initViewModel();
        initRecyclerView ();
        viewModel.getAllCategoryList ();
    }

    private void initViewModel() {

        viewModel=new ViewModelProvider (this).get(MainActivityViewModel.class);
        viewModel.getCategoryListObserve().observe(this,new Observer<List<Category>> () {
            @Override
            public void onChanged(List<Category> categories) {

                if(categories==null) {
                    noResulttextView.setVisibility (View.VISIBLE);
                    recyclerView.setVisibility (View.GONE);
                }
                else {
                    categoryListAdapter.setCategoryList (categories);

                    recyclerView.setVisibility (View.VISIBLE);


                    noResulttextView.setVisibility (View.GONE);
                }
            }
        });



    };


    private void initRecyclerView(){

        categoryListAdapter=new CategoryListAdapter (this,this);
        recyclerView.setLayoutManager (new LinearLayoutManager (this));
        recyclerView.setAdapter (categoryListAdapter);
    }








    private void showAddCategoryDialog(boolean isForEdit){
        AlertDialog dialogBuilder=new AlertDialog.Builder(this).create ();
         View dialogView=getLayoutInflater ().inflate (R.layout.add_category_layout,null);
        EditText enterCategoryInput=dialogView.findViewById (R.id.enterCategoryInput);
        TextView createButton=dialogView.findViewById (R.id.createbutton);
        TextView cancelButton=dialogView.findViewById (R.id.cancelbutton);

        if(isForEdit){
            createButton.setText ("Update");
            enterCategoryInput.setText (categoryForEdit.categoryName);
        }

        cancelButton.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss ();
            }
        });
        createButton.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                String name=enterCategoryInput.getText ().toString ();
                if(TextUtils.isEmpty (name)){
                    Toast.makeText (MainActivity.this,"Enter category name",Toast.LENGTH_SHORT).show ();
                    return;
                }
                if(isForEdit){
                    categoryForEdit.categoryName=name;
                   viewModel.updateCategory (categoryForEdit);
                }
                else {
                    viewModel.insertCategory (name);
                }

                dialogBuilder.dismiss ();
            }
        });

        dialogBuilder.setView (dialogView);
        dialogBuilder.show ();



    }

    @Override
    public void itemClick(Category category) {




        Intent intent =new Intent (MainActivity.this,ShowItemsListActivity.class);
        intent.putExtra("category_id",category.uid);
        intent.putExtra ("category_name",category.categoryName);
        startActivity (intent);
    }

    @Override
    public void removeItem(Category category) {

        viewModel.deleteCategory (category);
    }

    @Override
    public void editItem(Category category) {

        this.categoryForEdit=category;
        showAddCategoryDialog (true);
    }
}