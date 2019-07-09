SPAM DETECTOR using PMML
=========================

This project uses the PMML Model generated created in https://github.com/diegoami/SMS-Message-Spam-Detector and verifies whether the result are the same


## SET UP

Make sure you have Java 8 or higher installed and Maven

## COMPILE

Execute `mvn clean compile package`

## RUN locally

To verify the model, execute the following

```
java -cp target/bootstrap-executable-1.0-SNAPSHOT.jar org.jpmml.evaluator.bootstrap.Main spam_model.pmml spam_out.csv
```

The expected result is the following - same as in the original model created in Python in the repo https://github.com/diegoami/SMS-Message-Spam-Detector

```
Accuracy : 0.9937185929648241
Confidence Matrix: (4822, 32, 3, 715)
```


## EXECUTE IN DOCKER

The executable jar is already available in the root directory, therefore it can be used to create a docker image.

```
docker build -t spam_detector_pmml . 
docker run spam_detector_pmml
```
