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




## About the Model

* A convolutional deep learning model has been trained for Image Classification task.

* The architecture of model is as follows, 

![model](https://github.com/am15h/InkToMath/blob/model-readme/model-architecture.png)


* You can view the code in [InkToMath-notebook.ipynb](https://github.com/am15h/InkToMath/blob/master/InkToMath-notebook.ipynb).

* You can download the dataset [here](https://github.com/am15h/InkToMath/raw/master/train_final.zip).

### Dataset

* MNIST dataset for Digit Classification.

* Appended the images of symbols **+**, **-**, and **X**, to the MNIST dataset.
