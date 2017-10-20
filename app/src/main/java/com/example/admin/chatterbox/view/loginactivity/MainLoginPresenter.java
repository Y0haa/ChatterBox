package com.example.admin.chatterbox.view.loginactivity;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.admin.chatterbox.model.chat.User;
import com.example.admin.chatterbox.util.CurrentStoredUser;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import static com.example.admin.chatterbox.util.CurrentStoredUser.generateUserBaseOnAuthObject;

/**
 * Created by Admin on 10/12/2017.
 */

public class MainLoginPresenter implements MainLoginContract.UserActionsListener {

    private FirebaseAuth mAuth;
    private MainLoginContract.View mRegisterActivityView;

    public MainLoginPresenter(MainLoginContract.View mRegisterActivityView) {
        this.mRegisterActivityView = mRegisterActivityView;
        mAuth = FirebaseAuth.getInstance();
    }

    public MainLoginPresenter() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void attacheView(MainLoginContract.View mRegisterActivityView) {
        this.mRegisterActivityView = mRegisterActivityView;
    }

    @Override
    public void createFirebaseUser(String emailUser, final String passwordUser) {
        String email = emailUser;
        String password = passwordUser;

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((Activity) mRegisterActivityView,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Chat", "createUser onComplete: " + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.d("Chat", "user creation failed: ");
                            mRegisterActivityView.showDialog("Oops", "Registration attemp failed");
                        } else {
                            generateUserBaseOnAuthObject(mAuth);
                            verifyOrCreateIfUserExistOnDB(mAuth.getCurrentUser().getUid());
                            CurrentStoredUser.getInstance().setPassword(passwordUser);
                            mRegisterActivityView.userSuccessful(CurrentStoredUser.getInstance().getUser().getEmail());
                        }
                    }
                });
    }

    @Override
    public void signinFirebaseUser(String emailUser, final String passwordUser) {
        String email = emailUser;
        final String password = passwordUser;

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                Log.d("Chat", " signInWithEmail onComplete: " + task.isComplete());

                if (!task.isSuccessful()) {
                    Log.d("Chat", "Problem signing in: " + task.getException());
                    mRegisterActivityView.showDialog("Oops", "Registration attemp failed");
                } else {
                    generateUserBaseOnAuthObject(mAuth);
                    verifyOrCreateIfUserExistOnDB(mAuth.getCurrentUser().getUid());
                    CurrentStoredUser.getInstance().setPassword(passwordUser);
                    mRegisterActivityView.userSuccessful(CurrentStoredUser.getInstance().getUser().getEmail());
                }
            }
        });
    }

    @Override
    public void updateFirebaseUser(final User user, final String password) {


        DatabaseReference mDatabaseReference;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabaseReference.child("users").orderByChild("id").equalTo(user.getId());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        // do something with the individual "issues"
                        Log.d("TAG", "onDataChange: " + issue.getKey());
                        updateAuthUser(user, password);
                        updateDBUser(issue.getKey(), user);
                    }
                } else {
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void updateFirebaseWithoutAuth(final User user) {


        DatabaseReference mDatabaseReference;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabaseReference.child("users").orderByChild("id").equalTo(user.getId());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        // do something with the individual "issues"
                        Log.d("TAG", "onDataChange: " + issue.getKey());
                        updateDBUser(issue.getKey(), user);
                    }
                } else {
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) mRegisterActivityView, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG firebasefb", "signInWithCredential:success");
                            generateUserBaseOnAuthObject(mAuth);
                            verifyOrCreateIfUserExistOnDB(mAuth.getCurrentUser().getUid());
                            mRegisterActivityView.userSuccessful(CurrentStoredUser.getInstance().getUser().getEmail());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG firebasefb", "signInWithCredential:failure", task.getException());
                            mRegisterActivityView.showDialog("Oops", "Facebook authentication failed.");
                        }
                    }
                });
    }

    @Override
    public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) mRegisterActivityView, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            generateUserBaseOnAuthObject(mAuth);
                            verifyOrCreateIfUserExistOnDB(mAuth.getCurrentUser().getUid());
                            mRegisterActivityView.userSuccessful(CurrentStoredUser.getInstance().getUser().getEmail());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            mRegisterActivityView.showDialog("Oops", "Facebook authentication failed.");
                        }
                    }
                });
    }

    public void verifyOrCreateIfUserExistOnDB(String uid) {
        DatabaseReference mDatabaseReference;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabaseReference.child("users").orderByChild("id").equalTo(uid);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        // do something with the individual "issues"
                        Log.d("TAG", "onDataChange: " + issue.getKey());
                        CurrentStoredUser.generateUserBaseOnUserObject((HashMap<String, String>) issue.getValue());
                    }
                } else {
                    DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                    mDatabaseReference.child("users").push().setValue(CurrentStoredUser.getInstance().getUser());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateAuthUser(User user, final String password) {

        final FirebaseUser firebaseUser = mAuth.getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(user.getUsername())
                .build();

        firebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "User profile updated.");
                        }
                    }
                });

        firebaseUser.updateEmail(user.getEmail()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TAG", "onSuccess: ");
            }
        });

        try {

            AuthCredential credential = EmailAuthProvider
                    .getCredential(user.getEmail(), CurrentStoredUser.getInstance().getPassword());

            firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        firebaseUser.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("TAG", "Password updated");
                                    CurrentStoredUser.getInstance().setPassword(password);
                                } else {
                                    Log.d("TAG", "Error password not updated");
                                }

                            }
                        });
                    }
                }
            });

        } catch (Exception e) {
            mRegisterActivityView.showDialog("Error", e.getMessage().toString());
        }


    }

    private void updateDBUser(String uid, final User user) {

        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> result = new HashMap<>();
        result.put("id", user.getId());
        result.put("email", user.getEmail());
        result.put("name", user.getName());
        result.put("phoneNumber", user.getPhoneNumber());
        result.put("username", user.getUsername());
        result.put("userImage", user.getUserImage());


        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/users/" + uid, result);

        mDatabaseReference.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                CurrentStoredUser.updateUserBaseOnUserDB(user);
            }
        });


    }

    public void getLastUserOnDB(String uid) {
        DatabaseReference mDatabaseReference;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabaseReference.child("users").orderByChild("id").equalTo(uid);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        // do something with the individual "issues"
                        mRegisterActivityView.userSuccessful(issue.getValue());


                        Log.d("TAG", "onDataChange: " + issue.getKey());

                    }
                } else {
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void savePictureProfile(Uri imageUri, String ownerId) {
        FirebaseStorage storage = FirebaseStorage.getInstance(); // Use for Firebase storage
        StorageReference storageRef = storage.getReferenceFromUrl("gs://chatterbox-b78d6.appspot.com/");//Firebase storage location

        if (imageUri != null) {
            StorageReference childRef = storageRef.child("profileimage" + "/" + ownerId);
            UploadTask uploadTask = childRef.putFile(imageUri);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("TAG", "onSuccess: ");
                    CurrentStoredUser.getInstance().getUser().setUserImage(taskSnapshot.getDownloadUrl().toString());
                    updateFirebaseWithoutAuth(CurrentStoredUser.getInstance().getUser());
                    mRegisterActivityView.userSuccessful(taskSnapshot.getDownloadUrl().toString());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("TAG", "onFailure: ");
                }
            });

        }
    }

}
