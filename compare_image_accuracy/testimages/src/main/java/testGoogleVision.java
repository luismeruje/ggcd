import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class testGoogleVision {
        public static final String basePath = "/Users/luisferreira/Downloads/101_ObjectCategories/";
        public static final String resultPath = "/Users/luisferreira/Documents/Universidade/4ano/GGCD/";

        //TODO: forgot to measure time
        public static void main(String... args) throws Exception {
            // Instantiates a client
            try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {
                Map<String,Integer> categories = new HashMap<>();
                categories.put("Stegosaurus",50);
                categories.put("scorpion",50);
                categories.put("mayfly",40);
                categories.put("beaver",46);
                categories.put("Leopards",50);


                categories.forEach((category,numImages)->{
                    try {
                        annotateImagesFromCategory(vision, category, numImages);
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                });
            }

        }

        public static void annotateImagesFromCategory(ImageAnnotatorClient vision, String category, int numImages) throws Exception{
            long startTime = System.currentTimeMillis();
            List<List<AnnotateImageResponse>> annotationsPerImage = new ArrayList<>();
            // Builds the image annotation request
            List<AnnotateImageRequest> requests = new ArrayList<>();

            PrintWriter pw = new PrintWriter(new File(resultPath+category+".txt"));

            for(int i = 0; i <= (numImages / 16); i++ ) {
                for (int j = 1; j <= 16 && (j + (16*i))<= numImages; j++) {
                    // Reads the image file into memory
                    Path path = Paths.get(String.format(basePath + category + "/" + String.format("image_%04d.jpg", j + (16*i))));
                    byte[] data = Files.readAllBytes(path);
                    ByteString imgBytes = ByteString.copyFrom(data);

                    Image img = Image.newBuilder().setContent(imgBytes).build();
                    Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
                    AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                            .addFeatures(feat)
                            .setImage(img)
                            .build();
                    requests.add(request);
                    // Performs label detection on the image file
                }
                BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
                annotationsPerImage.add(response.getResponsesList());
                requests.clear();
            }
            int imageCounter = 1;
            for(int i = 0; i < annotationsPerImage.size(); i++) {
                List<AnnotateImageResponse> responses = annotationsPerImage.get(i);
                for (AnnotateImageResponse res : responses) {
                    pw.println("Image " + imageCounter);
                    System.out.println("Image "+ imageCounter++);
                    if (res.hasError()) {
                        System.out.printf("\tError: %s\n", res.getError().getMessage());
                        return;
                    }

                    for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
                        annotation.getAllFields().forEach((k, v) ->{
                            if(k.toString().endsWith("description")) {
                                System.out.print("\t" + v.toString() + ":");
                                pw.print("\t" + v.toString() + ":");
                            } else if(k.toString().endsWith("score")){
                                System.out.println(v);
                                pw.println(v);
                            }
                        });
                    }
                }
            }
            long finishTime = System.currentTimeMillis();
            pw.println("Elapsed time: " + (finishTime - startTime) + "ms");
            pw.flush();
            pw.close();
        }
}
