#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Thu May 23 00:15:27 2019

@author: luisferreira
"""

import keras
import numpy as np
from keras.applications import vgg16, inception_v3, resnet50, mobilenet
from keras.preprocessing.image import load_img
from keras.preprocessing.image import img_to_array
from keras.applications.imagenet_utils import decode_predictions
import matplotlib.pyplot as plt
import time

 
#Load the VGG model
vgg_model = vgg16.VGG16(weights='imagenet')
 



def predict_image(imagepath,acceptable_names):
    # load an image in PIL format
    original = load_img(imagepath, target_size=(224, 224))
    #print('PIL image size',original.size)
    #plt.imshow(original)
    #plt.show()
     
    # convert the PIL image to a numpy array
    # IN PIL - image is in (width, height, channel)
    # In Numpy - image is in (height, width, channel)
    numpy_image = img_to_array(original)
    #plt.imshow(np.uint8(numpy_image))
    #plt.show()
    #print('numpy array size',numpy_image.shape)
     
    # Convert the image / images into batch format
    # expand_dims will add an extra dimension to the data at a particular axis
    # We want the input matrix to the network to be of the form (batchsize, height, width, channels)
    # Thus we add the extra dimension to the axis 0.
    image_batch = np.expand_dims(numpy_image, axis=0)
    #print('image batch size', image_batch.shape)
    #plt.imshow(np.uint8(image_batch[0]))
    
    # prepare the image for the VGG model
    processed_image = vgg16.preprocess_input(image_batch.copy())
     
    # get the predicted probabilities for each class
    predictions = vgg_model.predict(processed_image)
    # print predictions
     
    # convert the probabilities to class labels
    # We will get top 5 predictions which is the default
    label = decode_predictions(predictions)
    for label_tuple in label[0]:
        if label_tuple[1] in acceptable_names:
            return True
    return False
    

filename = '/Users/luisferreira/Downloads/101_ObjectCategories/'

start = int(round(time.time() * 1000))
imageList = []
for i in range(1,51):
    imageList.append("%sstegosaurus/image_%.4d.jpg"%(filename,i))
counter = 0
for image in imageList:
    if predict_image(image,['triceratops']):
        counter += 1
finish = int(round(time.time() * 1000))
print('Stegosaurus accuracy = ', counter/50)
print(finish-start,' ms')

start = int(round(time.time() * 1000))
imageList = []
for i in range(1,51):
    imageList.append("%sscorpion/image_%.4d.jpg"%(filename,i))
counter = 0
for image in imageList:
    if predict_image(image,['scorpion']):
        counter += 1
finish = int(round(time.time() * 1000))
print('Scorpion accuracy = ', counter/50)     
print(finish-start,' ms') 

start = int(round(time.time() * 1000))
imageList = []
for i in range(1,51):
    imageList.append("%sLeopards/image_%.4d.jpg"%(filename,i))
counter = 0
for image in imageList:
    if predict_image(image,['leopard']):
        counter += 1
finish = int(round(time.time() * 1000))
print('Leopard accuracy = ', counter/50)  
print(finish-start,' ms')

start = int(round(time.time() * 1000))
imageList = []
for i in range(1,41):
    imageList.append("%smayfly/image_%.4d.jpg"%(filename,i))
counter = 0
for image in imageList:
    if predict_image(image,['dragonfly','lacewing']):
        counter += 1
finish = int(round(time.time() * 1000))
print('Mayfly accuracy = ', counter/40)
print(finish-start,' ms')


start = int(round(time.time() * 1000))
imageList = []
for i in range(1,47):
    imageList.append("%sbeaver/image_%.4d.jpg"%(filename,i))
counter = 0
for image in imageList:
    if predict_image(image,['beaver','otter']):
        counter += 1
finish = int(round(time.time() * 1000))
print('Beaver accuracy = ', counter/46)   
print(finish-start,' ms')
