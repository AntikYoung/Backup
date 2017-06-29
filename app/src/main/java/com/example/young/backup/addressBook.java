package com.example.young.backup;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Manifest;

public class addressBook extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    List<String> contactslist = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_book);
        ListView contactsView =(ListView) findViewById(R.id.contacts_view);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,contactslist);
        contactsView.setAdapter(adapter);
        if(ContextCompat.checkSelfPermission(this, Manifest.permssion.READ_CONTACTS)!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.READ_CONTACTS },1 );
        } else{
            readContacts();
        }

    }
    private void readContacts(){
        Cursor cursor = null;
        try{
            //查询联系人数据
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds
                    .Phone.CONTENT_URI,null,null,null,null);
            if(cursor !=null){
                while (cursor.moveToNext()){
                    //获取联系人姓名
                    String displayName =cursor.getString(cursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    //获取联系人手机号
                    String number =cursor.getString(cursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contactslist.add(displayName+"\n"+number);
                }
                adapter.notifyDataSetChanged();
            }
        } catch(Exception e){
            e.printStackTrace();
        } finally{
                if (cursor != null){
                  cursor.close();
                }

    }
    }
    @Override
    public  void onRequestPermissionsResult(int requestCode ,String[] permissions,
        int[] grantResults){
            switch(resquestCode){
                case 1:
                    if (grantResults.length >0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                        readContacts();
                 } else {
                     Toast.makeText(this,"You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:

            }
        }
    }
    /*public class PhoneInfo {
        private String name;
        private String number;
        public PhoneInfo(String name, String number) {
            this.name = name;
            this.number = number;
        }
        public String getName() {
            return name;
        }
        public String getNumber() {
            return number;
        }
    }


    public class GetPhoneNumberFromMobile {
        private List<phoneinfo> list;

        public List<phoneinfo> getPhoneNumberFromMobile(Context context) {
            // TODO Auto-generated constructor stub
            list = new ArrayList<phoneinfo>();
            Cursor cursor = context.getContentResolver().query(Phone.CONTENT_URI,
                    null, null, null, null);
            //moveToNext方法返回的是一个boolean类型的数据
            while (cursor.moveToNext()) {
                //读取通讯录的姓名
                String name = cursor.getString(cursor
                        .getColumnIndex(Phone.DISPLAY_NAME));
                //读取通讯录的号码
                String number = cursor.getString(cursor
                        .getColumnIndex(Phone.NUMBER));
                PhoneInfo phoneInfo = new PhoneInfo(name, number);
                list.add(phoneInfo);
            }
            return list;
        }
    }



    public class MyAdapter extends BaseAdapter{
        private List<phoneinfo> list;
        private Context context;

        public MyAdapter(List<phoneinfo> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            if(convertView==null){
                ViewHolder viewHolder=new ViewHolder();
                LayoutInflater inflater=LayoutInflater.from(context);
                convertView=inflater.inflate(R.layout.item, null);
                viewHolder.name=(TextView) convertView.findViewById(R.id.name);
                viewHolder.number=(TextView) convertView.findViewById(R.id.number);
                viewHolder.name.setText(list.get(position).getName());
                viewHolder.number.setText(list.get(position).getNumber());
            }
            return convertView;
        }
        public class ViewHolder{
            TextView name;
            TextView number;
        }
    }

    public class MainActivity extends Activity implements OnItemClickListener {
        private ListView lv;
        private MyAdapter adapter;
        private GetPhoneNumberFromMobile getPhoneNumberFromMobile;
        private List<phoneinfo> list = new ArrayList<phoneinfo>();

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            lv = (ListView) findViewById(R.id.listView1);
            getPhoneNumberFromMobile = new GetPhoneNumberFromMobile();
            list = getPhoneNumberFromMobile.getPhoneNumberFromMobile(this);
            adapter = new MyAdapter(list, this);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(this);
        }

        @Override
        public void onItemClick(AdapterView<!--?--> parent, View view, int position,
                                long id) {
            // TODO Auto-generated method stub
            String number = list.get(position).getNumber();
            Intent intent = new Intent();
            intent.setAction("android.intent.action.CALL");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("tel:"+number));
            startActivity(intent);
        }

    }
*/

