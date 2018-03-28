package activity;

import com.coolweather.app.R;

import android.app.Activity;  
import android.content.Intent;
import android.os.Bundle;  
import android.view.View;  
import android.view.Window;
import android.widget.Button; 

public class ChooseButton extends Activity{
	@Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState); 
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.start_layout);
    
        //第一种方式    
        Button Btn1 = (Button)findViewById(R.id.choose_city);//获取按钮资源
        Btn1.setOnClickListener(new Button.OnClickListener(){//创建监听    
            @Override
        	public void onClick(View v) {    
                Intent intent=new Intent(ChooseButton.this,ChooseAreaActivity.class);   
                startActivity(intent);
            }    
  
        });    
}
}