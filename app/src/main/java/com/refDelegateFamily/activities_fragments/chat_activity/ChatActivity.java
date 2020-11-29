package com.refDelegateFamily.activities_fragments.chat_activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.refDelegateFamily.R;
import com.refDelegateFamily.adapters.Chat_Adapter;
import com.refDelegateFamily.databinding.ActivityChatBinding;
import com.refDelegateFamily.databinding.DialogSelectImageBinding;
import com.refDelegateFamily.interfaces.Listeners;
import com.refDelegateFamily.language.Language_Helper;
import com.refDelegateFamily.models.ChatUserModel;
import com.refDelegateFamily.models.MessageDataModel;
import com.refDelegateFamily.models.MessageModel;
import com.refDelegateFamily.models.OrderModel;
import com.refDelegateFamily.models.UserModel;
import com.refDelegateFamily.preferences.Preferences;
import com.refDelegateFamily.remote.Api;
import com.refDelegateFamily.share.Common;
import com.refDelegateFamily.tags.Tags;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityChatBinding binding;
    private String lang;
    private Chat_Adapter chat_adapter;
    private List<MessageModel> messagedatalist;
    private LinearLayoutManager manager;
    private Preferences preferences;
    private UserModel userModel;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int IMG_REQ1 = 3, IMG_REQ2 = 2;
    private Uri url = null;
    private ChatUserModel chatUserModel;
    private int current_page = 1;
    private boolean isLoading = false;
    private int which_image_upload_selected;
    private OrderModel orderModel;
    ImagePopup imagePopup;
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language_Helper.updateResources(newBase, Language_Helper.getLanguage(newBase)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        initView();
        getOrder();
        getChatMessages();


    }

    private void initView() {
        imagePopup = new ImagePopup(this);
        imagePopup.setFullScreen(true);
        imagePopup.setBackgroundColor(Color.BLACK);  // Optional
        imagePopup.setFullScreen(true); // Optional
        imagePopup.setHideCloseIcon(false);
        imagePopup.setImageOnClickClose(true);  // Optional
        EventBus.getDefault().register(this);

        getDataFromIntent();
        messagedatalist = new ArrayList<>();

        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setName(chatUserModel.getName());
        binding.setBackListener(this);
        manager = new LinearLayoutManager(this);

        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(manager);
        chat_adapter = new Chat_Adapter(messagedatalist, userModel.getData().getId(), this);
        binding.recView.setItemViewCacheSize(25);
        binding.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recView.setDrawingCacheEnabled(true);
        binding.progBar.setVisibility(View.GONE);
        // binding.llMsgContainer.setVisibility(View.GONE);

        binding.recView.setAdapter(chat_adapter);
        binding.imageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkdata();
            }
        });

        binding.imagePhoto.setOnClickListener(view -> {
            which_image_upload_selected = 0;
            CreateImageAlertDialog();
        });
        binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0) {
                    int lastItemPos = manager.findLastCompletelyVisibleItemPosition();
                    int total_items = chat_adapter.getItemCount();

                    if (lastItemPos <= (total_items - 2) && !isLoading) {
                        isLoading = true;
                        messagedatalist.add(0, null);
                        chat_adapter.notifyItemInserted(0);
                        int next_page = current_page + 1;
                        loadMore(next_page);


                    }
                }
            }
        });
        binding.llBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                which_image_upload_selected = 1;
                CreateImageAlertDialog();

            }
        });
    }


    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {

            chatUserModel = (ChatUserModel) intent.getSerializableExtra("chat_user_data");
            binding.setName(chatUserModel.getName());

        }
    }

    private void getOrder() {

        final ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .getorderdetials("Bearer " + userModel.getData().getToken(), chatUserModel.getOrder_id())
                .enqueue(new Callback<OrderModel>() {
                    @Override
                    public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            update(response.body());


                        } else {
                            dialog.dismiss();
                            try {
                                Log.e("error_code", response.code() + "" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });

    }

    private void update(OrderModel body) {
        this.orderModel = body;
        if (body.getOrder().getBill_step().equals("not_attach")) {
            binding.llBill.setVisibility(View.VISIBLE);
        }
    }

    private void Sendbiilattach(String msg, String cost) {
        RequestBody from_user_id_part = Common.getRequestBodyText(userModel.getData().getId()+"");
        RequestBody to_user_id_part = Common.getRequestBodyText(chatUserModel.getId());

        RequestBody user_msg_part = Common.getRequestBodyText(msg);
        RequestBody bill_cost_part = Common.getRequestBodyText(cost);

        RequestBody order_type_part = Common.getRequestBodyText(chatUserModel.getOrder_id()+"");

        MultipartBody.Part image_part = Common.getMultiPart(this, url, "bill_image");

        Api.getService(Tags.base_url)
                .sendbillWithImage("Bearer " + userModel.getData().getToken(), from_user_id_part, to_user_id_part, user_msg_part, order_type_part,bill_cost_part, image_part)
                .enqueue(new Callback<MessageModel>() {
                    @Override
                    public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {

                     //   Log.e("datttaa", response.body() + "_");
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            binding.llBill.setVisibility(View.GONE);
                            // chatUserModel.setBill_step("bill_attach");
                            if (chat_adapter == null) {
                                messagedatalist.add(response.body().getData());
                                chat_adapter = new Chat_Adapter(messagedatalist, userModel.getData().getId(), ChatActivity.this);
                                binding.recView.setAdapter(chat_adapter);
                                chat_adapter.notifyDataSetChanged();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        binding.recView.scrollToPosition(messagedatalist.size() - 1);

                                    }
                                }, 100);
                            } else {
                                messagedatalist.add(response.body().getData());
                                chat_adapter.notifyItemInserted(messagedatalist.size() - 1);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        binding.recView.scrollToPosition(messagedatalist.size() - 1);

                                    }
                                }, 100);
                            }
                            getOrder();
                        } else {

                            try {
                                Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageModel> call, Throwable t) {
                        try {
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });

    }

    private void checkdata() {
        String message = binding.edtMsgContent.getText().toString();
        if (!TextUtils.isEmpty(message)) {
            Common.CloseKeyBoard(this, binding.edtMsgContent);
            binding.edtMsgContent.setText("");
            sendmessagetext(message);

        } else {
            binding.edtMsgContent.setError(getResources().getString(R.string.field_req));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void listenToNewMessage(MessageModel messageModel) {
        messagedatalist.add(messageModel);
        scrollToLastPosition();
    }

    private void scrollToLastPosition() {

        new Handler()
                .postDelayed(() -> binding.recView.scrollToPosition(messagedatalist.size() - 1), 10);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        preferences.clearChatUserData(this);
    }

    private void getChatMessages() {
        try {


            Api.getService(Tags.base_url)
                    .getRoomMessages("Bearer " + userModel.getData().getToken(), chatUserModel.getRoom_id(), 1)
                    .enqueue(new Callback<MessageDataModel>() {
                        @Override
                        public void onResponse(Call<MessageDataModel> call, Response<MessageDataModel> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null) {
                                // chatUserModel = new ChatUserModel(response.body().getRoom().getOther_user_name(), response.body().getRoom().getOther_user_avatar(), response.body().getRoom().getSecond_user_id() + "", response.body().getRoom().getId());
                                preferences.create_update_ChatUserData(ChatActivity.this, chatUserModel);

                                messagedatalist.clear();
                                messagedatalist.addAll(response.body().getData());
                                chat_adapter.notifyDataSetChanged();
                                scrollToLastPosition();

                            } else {

                                try {

                                    Log.e("errorsssss", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 500) {
                                    Toast.makeText(ChatActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(ChatActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error", response.code() + "_" + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MessageDataModel> call, Throwable t) {
                            try {
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(ChatActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ChatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }

    private void loadMore(int next_page) {
        try {

            Api.getService(Tags.base_url)
                    .getRoomMessages("Bearer " + userModel.getData().getToken(), chatUserModel.getRoom_id(), next_page)
                    .enqueue(new Callback<MessageDataModel>() {
                        @Override
                        public void onResponse(Call<MessageDataModel> call, Response<MessageDataModel> response) {
                            isLoading = false;
                            messagedatalist.remove(0);
                            chat_adapter.notifyItemRemoved(0);
                            if (response.isSuccessful() && response.body() != null) {

                                current_page = response.body().getCurrent_page();
                                messagedatalist.addAll(0, response.body().getData());
                                chat_adapter.notifyItemRangeInserted(0, response.body().getData().size());


                            } else {

                                if (response.code() == 500) {
                                    Toast.makeText(ChatActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(ChatActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error", response.code() + "_" + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MessageDataModel> call, Throwable t) {
                            try {
                                isLoading = false;

                                if (messagedatalist.get(0) == null) {
                                    messagedatalist.remove(0);
                                    chat_adapter.notifyItemRemoved(0);
                                }
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(ChatActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ChatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }


    private void sendmessageimage() {


        RequestBody user_part = Common.getRequestBodyText(userModel.getData().getId() + "");
        RequestBody reciver_part = Common.getRequestBodyText(chatUserModel.getId() + "");
        RequestBody type_part = Common.getRequestBodyText("file");
        RequestBody room_part = Common.getRequestBodyText(chatUserModel.getRoom_id() + "");


        MultipartBody.Part image_part = Common.getMultiPart(this, Uri.parse(url.toString()), "file_link");

        Api.getService(Tags.base_url)
                .sendmessagewithimage("Bearer " + userModel.getData().getToken(), user_part, reciver_part, type_part, room_part, image_part)
                .enqueue(new Callback<MessageModel>() {
                    @Override
                    public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                        //                 dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            //listener.onSuccess(response.body());

                            messagedatalist.add(response.body().getData());
                            chat_adapter.notifyDataSetChanged();
                            scrollToLastPosition();
                        } else {
                            Log.e("codeimage", response.code() + "_");
                            try {
                                Toast.makeText(ChatActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                Log.e("respons", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            // listener.onFailed(response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageModel> call, Throwable t) {
                        try {
                            //                   dialog.dismiss();
                            Toast.makeText(ChatActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                        }
                    }
                });
    }


    private void sendmessagetext(String message) {
//        final Dialog dialog = Common.createProgressDialog(ChatActivity.this, getString(R.string.wait));
//        dialog.setCancelable(false);
//        dialog.show();

        try {
            Api.getService(Tags.base_url)
                    .sendmessagetext("Bearer " + userModel.getData().getToken(), userModel.getData().getId() + "", chatUserModel.getId(), "text", chatUserModel.getRoom_id() + "", message).enqueue(new Callback<MessageModel>() {
                @Override
                public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                    //            dialog.dismiss();
                    if (response.isSuccessful()) {

                        Log.e("llll", response.toString());

                        messagedatalist.add(response.body().getData());
                        chat_adapter.notifyDataSetChanged();
                        scrollToLastPosition();
                    } else {
                        try {

                            Toast.makeText(ChatActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            Log.e("Error", response.toString() + " " + response.code() + "" + response.message() + "" + response.errorBody() + response.raw() + response.body() + response.headers() + " " + response.errorBody().toString());
                        } catch (Exception e) {


                        }
                    }
                }

                @Override
                public void onFailure(Call<MessageModel> call, Throwable t) {
                    //          dialog.dismiss();
                    try {
                        Toast.makeText(ChatActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                        Log.e("Error", t.getMessage());
                    } catch (Exception e) {

                    }
                }
            });
        } catch (Exception e) {
            //dialog.dismiss();
            Log.e("error", e.getMessage().toString());
        }
    }


    @Override
    public void back() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQ2 && resultCode == Activity.RESULT_OK && data != null) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            url = getUriFromBitmap(bitmap);
            if (which_image_upload_selected == 1) {
                CreateBillAlertDialog(url);

            } else {
                sendmessageimage();
            }


        } else if (requestCode == IMG_REQ1 && resultCode == Activity.RESULT_OK && data != null) {

            url = data.getData();
            if (which_image_upload_selected == 1) {
                CreateBillAlertDialog(url);

            } else {
                sendmessageimage();

            }
        }

    }
    public void CreateBillAlertDialog(Uri uri) {
        final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(this)
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_bill_photo, null);
        ImageView image = view.findViewById(R.id.image);
        final EditText edt_order_cost = view.findViewById(R.id.edt_order_cost);

        File file = new File(Common.getImagePath(this, uri));
        Picasso.get().load(file).fit().into(image);

        Button btn_upload = view.findViewById(R.id.btn_upload);
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cost = edt_order_cost.getText().toString().trim();
              //  Currency currency = Currency.getInstance(new Locale(lang, userModel.getData().ge()));
                if (!TextUtils.isEmpty(cost)) {
                    dialog.dismiss();
                    Common.CloseKeyBoard(ChatActivity.this, edt_order_cost);

                  //  double total = Double.parseDouble(cost) + Double.parseDouble(chatUserModel.getOffer_cost());
                  //  network_per_totla=(total*network_per)/100;
//                    //   total+=network_per_totla;
                    String msg =getResources().getString(R.string.order_value)+cost;
                  //  bill_amount=total+"";
                    Sendbiilattach(msg,cost);

                }
            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
       // dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setView(view);
        dialog.show();
    }

    private void CreateImageAlertDialog() {

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();

        DialogSelectImageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_select_image, null, false);


        binding.btnCamera.setOnClickListener(v -> {
            dialog.dismiss();
            Check_CameraPermission();

        });

        binding.btnGallery.setOnClickListener(v -> {
            dialog.dismiss();
            CheckReadPermission();


        });

        binding.btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    private void CheckReadPermission() {
        if (ActivityCompat.checkSelfPermission(this, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{READ_PERM}, IMG_REQ1);
        } else {
            SelectImage(IMG_REQ1);
        }
    }

    private void Check_CameraPermission() {
        if (ContextCompat.checkSelfPermission(this, camera_permission) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, write_permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{camera_permission, write_permission}, IMG_REQ2);
        } else {
            SelectImage(IMG_REQ2);

        }

    }

    private void SelectImage(int img_req) {

        Intent intent = new Intent();

        if (img_req == IMG_REQ1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            } else {
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
            startActivityForResult(intent, img_req);

        } else if (img_req == IMG_REQ2) {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, img_req);
            } catch (SecurityException e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

            }


        }
    }

    private Uri getUriFromBitmap(Bitmap bitmap) {
        String path = "";
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "title", null);
            return Uri.parse(path);

        } catch (SecurityException e) {
            Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        }
        return null;
    }
    public void showimage(String image) {
        imagePopup.initiatePopupWithPicasso(Tags.IMAGE_URL+image);
        imagePopup.viewPopup();

    }

}
