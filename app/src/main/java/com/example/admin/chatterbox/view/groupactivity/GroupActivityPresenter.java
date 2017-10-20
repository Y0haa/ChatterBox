package com.example.admin.chatterbox.view.groupactivity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.admin.chatterbox.model.chat.Chat;
import com.example.admin.chatterbox.model.chat.Group;
import com.example.admin.chatterbox.model.giphy.GiphyResponse;
import com.example.admin.chatterbox.model.giphyrand.GiphyRandomResponse;
import com.example.admin.chatterbox.model.giphytrend.GiphyTrendingResponse;
import com.example.admin.chatterbox.util.Commands;
import com.example.admin.chatterbox.util.CurrentStoredUser;
import com.example.admin.chatterbox.util.RetrofitHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by admin on 10/11/2017.
 */

public class GroupActivityPresenter implements GroupActivityContract.Presenter {

    public static final String TAG = "GroupPresenterTAG";

    private static final Intent RESULT_LOAD_IMG = null;
    GroupActivityContract.View view;
    DatabaseReference databaseReference;
    String owner = CurrentStoredUser.getInstance().getUser().getName();
    String ownerId = CurrentStoredUser.getInstance().getUser().getId();
    private String groupId;
    FirebaseStorage storage = FirebaseStorage.getInstance(); // Use for Firebase storage
    StorageReference storageRef = storage.getReferenceFromUrl("gs://chatterbox-b78d6.appspot.com/");//Firebase storage location

    Observer<Response<GiphyResponse>> giphyObserver = new Observer<Response<GiphyResponse>>() {
        @Override
        public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
            Log.d(TAG, "onSubscribe: ");
        }

        @Override
        public void onNext(@io.reactivex.annotations.NonNull Response<GiphyResponse> giphyResponseResponse) {
            if (giphyResponseResponse != null) {
                String url = "/giphy " + giphyResponseResponse.body().getData().getImages().getDownsized().getUrl();
                sendMessage(url, 0l);
            }
        }

        @Override
        public void onError(@io.reactivex.annotations.NonNull Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    };

    private Observer<Response<GiphyRandomResponse>> giphyRandObserver = new Observer<Response<GiphyRandomResponse>>() {
        @Override
        public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

        }

        @Override
        public void onNext(@io.reactivex.annotations.NonNull Response<GiphyRandomResponse> giphyRandomResponseResponse) {
            if (giphyRandomResponseResponse != null) {
                Log.d(TAG, "onNext: " + giphyRandomResponseResponse.raw().request().url().toString());
                if (giphyRandomResponseResponse.body() != null) {

                    String url = "/giphy " + giphyRandomResponseResponse.body().getData().getFixedHeightDownsampledUrl();
                    sendMessage(url, 0l);
                }
            }
        }

        @Override
        public void onError(@io.reactivex.annotations.NonNull Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    };

    private Observer<Response<GiphyTrendingResponse>> giphyTrendObserver = new Observer<Response<GiphyTrendingResponse>>() {
        @Override
        public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

        }

        @Override
        public void onNext(@io.reactivex.annotations.NonNull Response<GiphyTrendingResponse> giphyTrendingResponseResponse) {
            if (giphyTrendingResponseResponse != null) {
                Log.d(TAG, "onNext: " + giphyTrendingResponseResponse.raw().request().url().toString());
                String url = "/giphy " + giphyTrendingResponseResponse.body().getData().get(0).
                        getImages().getDownsized().getUrl();
                sendMessage(url, 0l);
            }
        }

        @Override
        public void onError(@io.reactivex.annotations.NonNull Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    };

    @Override
    public void attacheView(GroupActivityContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void sendMessage(String msg, Long time) {
        Chat chat = new Chat(msg, owner, ownerId, time);
        databaseReference.child("Groups").child(groupId).child("messages").push().setValue(chat);
        view.updateInputMsg();
    }

    @Override
    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    @Override
    public Group getGroup(String id) {

        return null;
    }

    @Override
    public void findDatabaseReference() {
        databaseReference =

                FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void checkCommand(String msg) {

        String cmdString = msg.substring(1);
        String args = cmdString.substring(cmdString.indexOf(' ') + 1);
        if (cmdString.contains(" ")) {
            cmdString = cmdString.substring(0, cmdString.indexOf(' '));
        }
        Commands cmd = null;
        try {
            cmd = Commands.valueOf(cmdString.toUpperCase());
        } catch (Exception e) {
            view.sendSystemMsg("System\n This isn't a valid command");
            return;
        }
        switch (cmd) {
            case HELP:
                String commandList = "System \n List of commands:\n";
                for (Commands c :
                        Commands.values()) {
                    commandList += "/" + c.name() + "\n";
                }
                view.sendSystemMsg(commandList);
                break;

            case GIPHY:
                Log.d(TAG, "checkCommand: " + args + " " + args.toLowerCase().compareTo("trending"));
                if (args != null && args.compareTo("") != 0) {
                    Log.d(TAG, "checkCommand: ");
                    if (args.toLowerCase().contains("random")) {
                        if (args.contains(" ")) {
                            args = args.substring(args.indexOf(" ") + 1);
                        } else {
                            args = "";
                            Log.d(TAG, "checkCommand: " + args.length());
                        }
                        final Observable<Response<GiphyRandomResponse>> giphyObservable = RetrofitHelper.createGiphyRandom(args);
                        giphyObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe(giphyRandObserver);
                    } else if (args.toLowerCase().compareTo("trending") == 0) {
                        Log.d(TAG, "checkCommand: trending observable");
                        final Observable<Response<GiphyTrendingResponse>> giphyObservable = RetrofitHelper.createGiphyTrending();
                        giphyObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe(giphyTrendObserver);
                    } else {
                        final Observable<Response<GiphyResponse>> giphyObservable = RetrofitHelper.createGiphyTranslate(args);
                        giphyObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe(giphyObserver);
                    }
                } else {
                    sendMessage("System \n Giphy requires at least on paramater", 0l);
                }
                break;

            case YOKO:
                sendMessage("/yoko", 0l);
                break;

            default:
                view.sendSystemMsg("System\n This isn't a valid command");
        }

    }

    @Override
    public void setGroupId(String id) {
        groupId = id;
    }

    @Override
    public void uploadImage(Uri imageUri, String filename) {
        if (imageUri != null) {

            //Storing in unique location
            final StorageReference childRef = storageRef.child(ownerId + "/" + filename);

            String referenceLocation = ownerId + "/" + filename;
            //uploading the image
            UploadTask uploadTask = childRef.putFile(imageUri);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    String taskSnapshopURL = downloadUrl.toString();

                    final long ONE_MEGABYTE = 1024 * 1024;
                    childRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            // Data for "images/island.jpg" is returns, use this as needed
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });

                    sendMessage(taskSnapshopURL, 0l);
                    view.updateOnSendImage("File uploaded");

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    view.updateOnSendImage("Failed to upload");
                }
            });
        } else {
            view.updateOnSendImage("No Image choosen");
        }

    }


}
