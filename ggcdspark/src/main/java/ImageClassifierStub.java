import protobuf.ImageReferenceProto;
import protobuf.LabelsProto;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;

public class ImageClassifierStub{

    private static Socket socket ;

    public ImageClassifierStub() throws Exception{
    }

    //Compare accuracy of two models
    //Compare google vision speed with pre-trained model speed
    //Compare socket vs pipe speed
    //Change socket to unix pipe(better performance)
    public static List<String> classifyImage(String url) throws Exception {
        socket  = new Socket("localhost",6144);
        ImageReferenceProto.ImageReference imageReference = ImageReferenceProto.ImageReference.newBuilder()
                .setReference(url).build();
        OutputStream outputStream = socket.getOutputStream();
        InputStream inputStream = socket.getInputStream();
        imageReference.writeTo(outputStream);
        outputStream.flush();

        LabelsProto.Labels labels = LabelsProto.Labels.parseFrom(inputStream);
        socket.close();
        return labels.getLabelsList();
    }

    public static void main(String args[]) throws Exception{
        long start = System.currentTimeMillis();
        for(int i = 0; i < 50; i++) {
            ImageClassifierStub imageClassifierStub = new ImageClassifierStub();
            imageClassifierStub.classifyImage("http://pbs.twimg.com/media/C_UdnvPUwAE3Dnn.jpg");
        }
        long finish = System.currentTimeMillis();
        System.out.println((finish-start) + "ms");
    }

}
