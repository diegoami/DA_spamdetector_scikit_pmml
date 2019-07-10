FROM oracle/openjdk:8
WORKDIR /opt
RUN mkdir data
RUN mkdir target
ADD target/bootstrap-executable-1.0-SNAPSHOT.jar target/
ADD data/spam_model.pmml data/
ADD data/spam_out.csv data/
CMD [ "java", "-cp", "target/bootstrap-executable-1.0-SNAPSHOT.jar", "org.jpmml.evaluator.bootstrap.Main", "data/spam_model.pmml", "data/spam_out.csv" ]