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
    
        //��һ�ַ�ʽ    
        Button Btn1 = (Button)findViewById(R.id.choose_city);//��ȡ��ť��Դ
        Btn1.setOnClickListener(new Button.OnClickListener(){//��������    
            @Override
        	public void onClick(View v) {    
                Intent intent=new Intent(ChooseButton.this,ChooseAreaActivity.class);   
                startActivity(intent);
            }    
  
        });    
}
}