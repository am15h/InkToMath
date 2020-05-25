# InkToMath

InkToMath Equation Solver, CSN-254 Semester Project

### Try it youself

You can [download **InkToMath.apk**](https://github.com/am15h/InkToMath/raw/master/InkToMath.apk) and install on in your phone.

### Usage Instructions

* The app can classify digits **0-9** and symbols **+**, **-**, **X**,
* Each digit, symbol has to be drawn separately on the pad provided in the app.

* Press **CLASSIFY** button to add each digit, symbol to the equation.
* Press **CALCULATE** button to evaluate the equation
* Press **CLEAR** button to clear the equation and start again

* For **- (negative)** symbol, prefer drawing on top of the canvas for higher accuracy


## Demo GIF

<img src="https://github.com/am15h/InkToMath/blob/readme/InkToMath.gif" width="360" height="640" />


## Technical Details

The project comprises a mobile application that can be used to compute values of
handwritten equations. The main tools and technologies that are used include :

* Android Studio
* Flutter for app development
* Convolutional Neural Networks
* Deep Learning
* MNIST dataset
* TensorFlow Framework for building the DL model
* TensorFlow Lite Library for integrating the model with the mobile app environment
* Colab for training the DL model

#### The architecture of the Deep Learning Model:

The model takes input as an image vector of dimensions (28, 28). This vector comprises
the pixel values of the image of the symbol or digit to be recognized. These values are
further normalized and fed into the Convolutional Neural Network (CNN).
The model has 2 layers of the CNN computing the convolution over the input image
vector with filters of appropriate dimensions, followed by a MaxPooling layer. The
earlier layers of the model are responsible for detecting simple features such as edges
and corners, while the deeper layers are more responsible towards extracting more
complex features.
After the Dropout, the Model has a Softmax Classifier. This is where the CNN outputs a
vector of dimensions (13, 1) consisting of the probabilities of the given image being any
of the required characters. The class with the highest probability is predicted as the
output by the model. The 13 classes include digits 0-9 and operators +, -, x.



## About the Model

* A convolutional deep learning model has been trained for Image Classification task.

* The architecture of model is as follows, 

![model](https://github.com/am15h/InkToMath/blob/model-readme/model-architecture.png)


* You can view the code in [InkToMath-notebook.ipynb](https://github.com/am15h/InkToMath/blob/master/InkToMath-notebook.ipynb).

* You can download the dataset [here](https://github.com/am15h/InkToMath/raw/master/train_final.zip).

### Dataset

* MNIST dataset for Digit Classification.

* Appended the images of symbols **+**, **-**, and **X**, to the MNIST dataset.
