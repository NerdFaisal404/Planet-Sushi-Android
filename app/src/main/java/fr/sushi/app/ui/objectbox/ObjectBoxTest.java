package fr.sushi.app.ui.objectbox;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import java.util.List;

import fr.sushi.app.R;
import fr.sushi.app.databinding.ActivityTestBinding;
import fr.sushi.app.ui.base.ItemClickListener;


public class ObjectBoxTest extends AppCompatActivity implements ItemClickListener<User> {
    private TestViewModel testViewModel;
    private ActivityTestBinding binding;
    private UserTestAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_test);
        testViewModel = getHomeViewModel();
        binding.rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserTestAdapter();
        binding.rv.setAdapter(adapter);

        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testViewModel.saveUser();
            }
        });

    }

    private TestViewModel getHomeViewModel(){
        return  ViewModelProviders.of(this).get(TestViewModel.class);
    }

    private void loadData(){
        testViewModel.getUserList().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                adapter.addItem(users);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onItemClick(View view, User item) {
        testViewModel.deleteItem(item);
    }
}
