import socket
import ImageReference_pb2
import Labels_pb2
import keras
import numpy as np
from keras.applications import vgg16, inception_v3, resnet50, mobilenet
from keras.preprocessing.image import load_img
from keras.preprocessing.image import img_to_array
from keras.applications.imagenet_utils import decode_predictions
import matplotlib.pyplot as plt
from PIL import Image, ImageOps
import requests
from io import BytesIO

def predict_image(url):
    print("url:",url)
    size = 224, 224
    response = requests.get(url)
    original = Image.open(BytesIO(response.content))
    original = ImageOps.fit(original,size, Image.ANTIALIAS)
    # load an image in PIL format
    #original = load_img(imagepath, target_size=(224, 224))

    # convert the PIL image to a numpy array
    numpy_image = img_to_array(original)

    # Convert the image / images into batch format
    image_batch = np.expand_dims(numpy_image, axis=0)


    # prepare the image for the VGG model
    processed_image = vgg16.preprocess_input(image_batch.copy())

    # get the predicted probabilities for each class
    predictions = vgg_model.predict(processed_image)

    # top 5 predictions
    label = decode_predictions(predictions)

    label_name_list = []
    for label_tuple in label[0]:
        label_name_list.append(label_tuple[1])
    return label_name_list

#Load the VGG model
vgg_model = vgg16.VGG16(weights='imagenet')

#Start listening for requests
s = socket.socket()
port = 6144



init_url = "https://pbs.twimg.com/media/D7ilK0BXsAAziEg.jpg"
predict_image(init_url)
s.bind(('',port))
print("Socket binded to %s" % (port))

s.listen(200)
while True:
    connection, addr = s.accept()
    message = connection.recv(4096)
    imageReference = ImageReference_pb2.ImageReference()
    imageReference.ParseFromString(message)
    resultLabels = predict_image(imageReference.reference)
    labelsProtoMessage = Labels_pb2.Labels()
    labelsProtoMessage.labels.extend(resultLabels)
    print(labelsProtoMessage)
    connection.sendall(labelsProtoMessage.SerializeToString())
    print("Sent result")
    connection.close()
