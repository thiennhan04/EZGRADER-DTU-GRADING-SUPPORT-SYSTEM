package com.example.scorescanner;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.concurrent.CompletableFuture;

public class DetectText {

    public CompletableFuture<String> detectTxt(Bitmap img) {
        CompletableFuture<String> resultFuture = new CompletableFuture<>();

        InputImage image = InputImage.fromBitmap(img, 0);
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        Task<Text> resultTask = recognizer.process(image);
        resultTask.addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(Text text) {
                StringBuilder resultBuilder = new StringBuilder();
                for (Text.TextBlock block : text.getTextBlocks()) {
                    String blockText = block.getText();
                    for (Text.Line line : block.getLines()) {
                        String lineText = line.getText();
                        for (Text.Element element : line.getElements()) {
                            String elementText = element.getText();
                            resultBuilder.append(elementText);
                            resultBuilder.append(" ");
                        }
                    }
                }

                String result = resultBuilder.toString();
//                Log.d("DetectText", "Detected text: " + result);

                // Hoàn thành CompletableFuture với kết quả
                resultFuture.complete(result);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Xử lý khi có lỗi và hoàn thành CompletableFuture với giá trị mặc định
                e.printStackTrace();
                resultFuture.completeExceptionally(e);
            }
        });
        return resultFuture;
    }
}
