FROM oracle/openjdk:8
WORKDIR /opt

ADD bootstrap-executable-1.0-SNAPSHOT.jar .
ADD spam_out.csv .
ADD spam_model.pmml .

CMD [ "java", "-cp", "bootstrap-executable-1.0-SNAPSHOT.jar", "org.jpmml.evaluator.bootstrap.Main", "spam_model.pmml", "spam_out.csv" ]