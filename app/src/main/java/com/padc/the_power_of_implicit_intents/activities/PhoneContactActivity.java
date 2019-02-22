package com.padc.the_power_of_implicit_intents.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.padc.the_power_of_implicit_intents.R;

public class PhoneContactActivity extends AppCompatActivity {

    private ImageView ivProfile;
    private  Button btnPhoneContact;
    private int REQUEST_SELECT_CONTACT = 1;
    private TextView tvName,tvPhoneNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_contact);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.btn_contact));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ivProfile=findViewById(R.id.iv_profile);
        tvName=findViewById(R.id.tv_Name);
        tvPhoneNumber=findViewById(R.id.tv_phone_number);
        btnPhoneContact=findViewById(R.id.btn_phone_contact);
        btnPhoneContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectContact();

            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;

    }


    public void selectContact() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_SELECT_CONTACT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_SELECT_CONTACT)
        {
            String phoneNo="",name ="",profile="";
            int index;
            Uri contactUri = data.getData();
            Cursor cursorName = getContentResolver().query(contactUri,null,null,null,null);
            // Cursor cursorPhoneNo = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
            if(cursorName.moveToFirst())
            {
                index = cursorName.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                name = cursorName.getString(index);

                String contactId =
                        cursorName.getString(cursorName.getColumnIndex(ContactsContract.Contacts._ID));

                Cursor phones =  getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                while (phones.moveToNext()) {
                    String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    phoneNo = number;
                    int type = phones.getInt(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                    switch (type) {
                        case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                            // do something with the Home number here...
                            break;
                        case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                            // do something with the Mobile number here...
                            Log.d("ContactsH", number);
                            break;
                        case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                            // do something with the Work number here...
                            break;
                    }
                     profile = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                }
                phones.close();

            }


            ivProfile.setImageURI(Uri.parse(profile));
            tvName.setText("Name : "+name);
            tvPhoneNumber.setText("Phone Number : " + phoneNo);



        }

    }
}
