import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class parseResultsGoogleVision {
    public static void main(String args[]) throws Exception{
        String resultsPath = "../vision-results";
        Map<String,Integer> categories = new HashMap<>();
        categories.put("Stegosaurus",50);
        categories.put("scorpion",50);
        categories.put("mayfly",40);
        categories.put("beaver",46);
        categories.put("Leopards",50);

        Map<String,List<String>> acceptableAnswers = new HashMap<>();
        acceptableAnswers.put("Stegosaurus",new ArrayList<>(Arrays.asList("dinosaur","stegosaurus","dinosaurs")));
        acceptableAnswers.put("scorpion",new ArrayList<>(Arrays.asList("scorpion","arachnid")));
        acceptableAnswers.put("mayfly",new ArrayList<>(Arrays.asList("mayfly","mayflies","dragonfly","dragonflies")));
        acceptableAnswers.put("beaver",new ArrayList<>(Arrays.asList("beaver","castor","rodent","groundhog","otter")));
        acceptableAnswers.put("Leopards",new ArrayList<>(Arrays.asList("leopard","leopards","cheetah")));

        List<Boolean> hits = new ArrayList<>();

        for(String category: categories.keySet()){
            hits.clear();
            int nrImages = categories.get(category);
            List<String>acceptableClassifications = acceptableAnswers.get(category);
            List<String> consideredClassifications = new ArrayList<>();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(resultsPath+"/"+category+".txt")));
            String currentLine;
            boolean added = false;
            while((currentLine = bufferedReader.readLine()) != null){
                if(currentLine.startsWith("Image")){
                    if(consideredClassifications.size() > 0){
                        for(String considered: consideredClassifications) {
                            if(isAcceptable(considered,acceptableClassifications)){
                                hits.add(true);
                                added = true;
                                break;
                            }
                        }
                        if(!added){
                            hits.add(false);
                        }
                    }
                    consideredClassifications.clear();
                } else if(consideredClassifications.size() < 5){
                    String[] values = currentLine.split(":");
                    if(consideredClassifications.size() == 0 || Float.parseFloat(values[1]) > 0.5){
                        consideredClassifications.add(values[0]);
                    }
                }
            }
            float accuracy = 0;
            for(boolean hit: hits){
                if(hit)
                    accuracy++;
            }
            accuracy = accuracy / nrImages;

            System.out.println("Accuracy for category " + category + " is:" + accuracy);

        }

    }

    public static boolean isAcceptable(String considered, List<String> acceptableAnswers){
        boolean result = false;
        for(String acceptable: acceptableAnswers){
            if(considered.toLowerCase().contains(acceptable)){
                result = true;
                break;
            }
        }
        return result;
    }
}
