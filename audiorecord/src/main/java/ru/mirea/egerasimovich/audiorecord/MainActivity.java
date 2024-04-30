package ru.mirea.egerasimovich.audiorecord;

import	androidx.annotation.NonNull;
import	androidx.appcompat.app.AppCompatActivity;
import	androidx.core.app.ActivityCompat;
import	androidx.core.content.ContextCompat;
import	android.Manifest;
import	android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import	android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.IOException;

import ru.mirea.egerasimovich.audiorecord.databinding.ActivityMainBinding;

public	class MainActivity	extends	AppCompatActivity	{
    private MediaRecorder recorder;
    private MediaPlayer player;
    private ActivityMainBinding binding;
    private	static	final int REQUEST_CODE_PERMISSION =	200;
    private	boolean	isWork;
    private String recordFilePath;
    boolean	isStartRecording = true;
    boolean	isStartPlaying = true;

    private static final String TAG ="MainActivity";
    @Override
    protected void onCreate(Bundle	savedInstanceState)	{
        super.onCreate(savedInstanceState);

        binding	= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.playButton.setEnabled(false);

        recordFilePath = (new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC),
                "audiorecordtest.3gp")).getAbsolutePath();
        checkPermissions();

        int audioRecordPermissionStatus = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO);
        int storagePermissionStatus = ContextCompat.checkSelfPermission(this, android.Manifest.permission.
                WRITE_EXTERNAL_STORAGE);
        if (audioRecordPermissionStatus == PackageManager.PERMISSION_GRANTED && storagePermissionStatus
                == PackageManager.PERMISSION_GRANTED) {
            isWork = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        }

        binding.recordButton.setOnClickListener(new	View.OnClickListener()	{
            @Override
            public void onClick(View v)	{
                if	(isStartRecording)	{
                    binding.recordButton.setText("Stop	recording");
                    binding.playButton.setEnabled(false);
                    startRecording();
                }	else	{
                    binding.recordButton.setText("Start	recording");
                    binding.playButton.setEnabled(true);
                    stopRecording();
                }
                isStartRecording = !isStartRecording;
            }
        });
        binding.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if	(isStartPlaying)	{
                    binding.playButton.setText("Stop playing");
                    binding.recordButton.setEnabled(false);
                    startPlaying();
                }	else	{
                    binding.playButton.setText("Start playing");
                    binding.recordButton.setEnabled(true);
                    stopPlaying();
                }
                isStartPlaying = !isStartPlaying;
            }
        });

    }
    @Override
    public	void onRequestPermissionsResult(int	requestCode, @NonNull String[] permissions,	@NonNull int[]
            grantResults)	{
        super.onRequestPermissionsResult(requestCode,	permissions,	grantResults);
        if (grantResults.length == 0) {
            return;}
        switch	(requestCode){
            case REQUEST_CODE_PERMISSION:
                isWork = grantResults[0] ==	PackageManager.PERMISSION_GRANTED;
                break;
        }
        if	(!isWork) finish();
    }
    private	void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(recordFilePath);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try	{
            recorder.prepare();
        }	catch (IOException e)	{
            Log.e(TAG, "prepare() failed");
        }
        recorder.start();
    }

    private	void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }
    private	void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(recordFilePath);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }
    }
    private	void stopPlaying()	{
        player.release();
        player = null;
    }
    private void checkPermissions(){
        int	audioRecordPermissionStatus	= ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int	storagePermissionStatus	= ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if	(audioRecordPermissionStatus == PackageManager.PERMISSION_GRANTED && storagePermissionStatus
                ==	PackageManager.PERMISSION_GRANTED)	{
            isWork = true;
        } else {
            //	Выполняется	запрос к пользователю на получение необходимых разрешений
            ActivityCompat.requestPermissions(this,	new	String[] {Manifest.permission.RECORD_AUDIO,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        }
    }
}