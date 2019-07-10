SPAM DETECTOR using PMML
=========================

This project uses the PMML Model generated created in http://github.com/diegoami/DA_spamdetector_scikit and verifies whether the result are the same


## SET UP

Make sure you have Java 8 or higher installed and Maven

## COMPILE

Execute `mvn clean compile package`

## RUN locally

To verify the model, execute the following commands.
If you have created `spam_model.pmml` and `spam_out.csv` in other directories, adapt the command line accordingly.

```
java -cp target/bootstrap-executable-1.0-SNAPSHOT.jar org.jpmml.evaluator.bootstrap.Main data/spam_model.pmml data/spam_out.csv
# java -cp target/bootstrap-executable-1.0-SNAPSHOT.jar org.jpmml.evaluator.bootstrap.Main <YOUR_DATA_DIRECTORY>/spam_model.pmml <YOUR_DATA_DIRECTORY>/spam_out.csv
```

The expected result is the following - same as in the original model created in Python in the repo http://github.com/diegoami/DA_spamdetector_scikit

```
Accuracy : 0.9937185929648241
Confidence Matrix: (4822, 32, 3, 715)
```


## EXECUTE IN DOCKER

The executable jar is already available in the root directory, therefore it can be used to create a docker image.
YOUR_DATA_DIRECTORY is where you put the files you created from http://github.com/diegoami/DA_spamdetector_scikit

```
docker build -t spam_detector_pmml . 
docker run  -v $(pwd)/data:/data spam_detector_pmml
# docker run  -v <YOUR_DATA_DIRECTORY>:/data spam_detector_pmml
```
