

package com.anmaza.friendsleague;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.anmaza.friendsleague.entities.Ligas;
import com.anmaza.friendsleague.entities.Users;
import com.anmaza.friendsleague.utils.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import java.io.File;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private CardView fifa_web, nba_web, rocket_web;
    private ImageView img_profile;
    private TextView text_name, text_email, btn_close, btn_delete;
    private View v;
    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String id = user.getUid();
    private String name, email, profile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_profile, null);
        text_name = v.findViewById(R.id.name_profile);
        text_email = v.findViewById(R.id.text_email);
        btn_close = v.findViewById(R.id.btn_close);
        btn_delete = v.findViewById(R.id.btn_delete);
        img_profile = v.findViewById(R.id.image_profile);
        fifa_web = v.findViewById(R.id.cardView_fifa);
        nba_web = v.findViewById(R.id.cardView_nba);
        rocket_web = v.findViewById(R.id.cardView_rocket);
        firebaseAuth = FirebaseAuth.getInstance();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fifa_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.ea.com/es-es/games/fifa/fifa-21"));
                startActivity(i);
            }
        });

        nba_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.nba2k.com/"));
                startActivity(i);
            }
        });

        rocket_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.rocketleague.com/es-es/"));
                startActivity(i);
            }
        });

        //Cerrar sesión
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View mDialogView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_general, null);
                Button btn_yes = mDialogView.findViewById(R.id.btn_yes);
                Button btn_no = mDialogView.findViewById(R.id.btn_no);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(mDialogView).setCancelable(false);
                AlertDialog showAlert = builder.show();

                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        firebaseAuth.signOut();
                        Intent i = new Intent(getContext(), MainActivity.class);
                        startActivity(i);
                    }
                });

                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAlert.dismiss();
                    }
                });
            }
        });

        //Motsrar datos en perfil
        seeProfile();

        //Eliminar cuenta
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View mDialogView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_general, null);
                Button btn_yes = mDialogView.findViewById(R.id.btn_yes);
                Button btn_no = mDialogView.findViewById(R.id.btn_no);
                TextView txt_title = mDialogView.findViewById(R.id.txt_welcome);
                TextView txt_info = mDialogView.findViewById(R.id.txt_information);

                txt_title.setText(R.string.delete_popup);
                txt_info.setText(R.string.text_delete);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(mDialogView).setCancelable(false);
                AlertDialog showAlert = builder.show();

                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteAccount();
                    }
                });

                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAlert.dismiss();
                    }
                });
            }
        });
    }

    public void seeProfile() {
        reference = FirebaseDatabase.getInstance().getReference();
        //Coger id del usuario y mostrarlo en el perfil
        reference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        reference.child("Users").child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                Users users = snapshot.getValue(Users.class);
                                if (id.equals(snapshot.getKey())) {
                                    try {
                                        name = users.getUsername();
                                        email = users.getEmail();
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }
                                }
                                text_name.setText(name);
                                text_email.setText(email);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void deleteAccount() {
        reference = FirebaseDatabase.getInstance().getReference();

        reference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    reference.child("Users").child(dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (id.equals(snapshot.getKey())) {
                                snapshot.getRef().removeValue();
                                user.delete();
                                Intent i = new Intent(getContext(), MainActivity.class);
                                startActivity(i);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}



