library("pmml")
library("randomForest")

data(iris)

setwd("../resources")

write.csv(iris[1:4], "csv/Iris.csv", quote = FALSE, row.names = FALSE)

iris.rf = randomForest(Species ~ ., data = iris, ntree = 7)
saveXML(pmml(iris.rf, dataset = iris), "pmml/RandomForestIris.pmml")

iris.response = predict(iris.rf, newdata = iris, type = "response")
iris.prob = predict(iris.rf, newdata = iris, type = "prob")

result = data.frame(
	"Species" = iris.response, "Predicted_Species" = iris.response,
	"Probability_setosa" = iris.prob[, 1], "Probability_versicolor" = iris.prob[, 2], "Probability_virginica" = iris.prob[, 3]
)

write.csv(result, "csv/RandomForestIris.csv", quote = FALSE, row.names = FALSE)
