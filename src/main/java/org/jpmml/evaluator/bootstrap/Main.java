/*
 * Copyright (c) 2016 Villu Ruusmann
 *
 * This file is part of JPMML-Evaluator
 *
 * JPMML-Evaluator is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JPMML-Evaluator is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with JPMML-Evaluator.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jpmml.evaluator.bootstrap;

import java.io.*;
import java.util.*;

import com.google.common.base.Splitter;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.PMML;
import org.jpmml.evaluator.*;
import org.jpmml.model.PMMLUtil;


import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;


import com.google.common.cache.CacheBuilderSpec;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.PMML;
import org.dmg.pmml.PMMLObject;
;
import org.jpmml.model.visitors.LocatorNullifier;
import org.jpmml.model.visitors.MemoryMeasurer;


public class Main {

	private static final List<String> missingValues = Arrays.asList("N/A", "NA");

	static public CsvUtil.Table readTable(File file, String separator) throws IOException {

		try(InputStream is = new FileInputStream(file)){
			return CsvUtil.readTable(is, separator);
		}
	}

	static public java.util.function.Function<String, String> createCellParser(final Collection<String> missingValues){
		java.util.function.Function<String, String> function = new Function<String, String>(){

			@Override
			public String apply(String string){

				if(missingValues != null && missingValues.contains(string)){
					return null;
				}

				// Remove leading and trailing quotation marks
				string = stripQuotes(string, '\"');
				string = stripQuotes(string, '\"');

				// Standardize European-style decimal marks (',') to US-style decimal marks ('.')
				if(string.indexOf(',') > -1){
					String usString = string.replace(',', '.');

					try {
						Double.parseDouble(usString);

						string = usString;
					} catch(NumberFormatException nfe){
						// Ignored
					}
				}

				return string;
			}

			private String stripQuotes(String string, char quoteChar){

				if(string.length() > 1 && ((string.charAt(0) == quoteChar) && (string.charAt(string.length() - 1) == quoteChar))){
					return string.substring(1, string.length() - 1);
				}

				return string;
			}
		};

		return function;
	}

	static private String stripQuotes(String string, char quoteChar){

		if(string.length() > 1 && ((string.charAt(0) == quoteChar) && (string.charAt(string.length() - 1) == quoteChar))){
			return string.substring(1, string.length() - 1);
		}

		return string;
	}

	static
	public void main(String... args) throws Exception {
		PMML pmml;

		try(InputStream is = new FileInputStream(args[0])){
			pmml = PMMLUtil.unmarshal(is);
		}

		ModelEvaluatorFactory modelEvaluatorFactory = ModelEvaluatorFactory.newInstance();

		Evaluator evaluator = (Evaluator)modelEvaluatorFactory.newModelEvaluator(pmml);

		System.out.println("Summary: " + evaluator.getSummary());
		System.out.println("Mining function: " + evaluator.getMiningFunction());

		System.out.println("Input fields:");

		List<InputField> inputFields = evaluator.getInputFields();
		for(InputField inputField : inputFields){
			System.out.println("\t" + inputField);
		}

		if(evaluator instanceof HasGroupFields){
			HasGroupFields hasGroupFields = (HasGroupFields)evaluator;

			List<InputField> groupFields = hasGroupFields.getGroupFields();
			for(InputField groupField : groupFields){
				System.out.println("\t" + groupField);
			}
		} // End if

		if(evaluator instanceof HasOrderFields){
			HasOrderFields hasOrderFields = (HasOrderFields)evaluator;

			List<InputField> orderFields = hasOrderFields.getOrderFields();
			for(InputField orderField : orderFields){
				System.out.println("\t" + orderField);
			}
		}

		System.out.println("Result fields:");

		List<TargetField> targetFields = evaluator.getTargetFields();
		for(TargetField targetField : targetFields){
			System.out.println("\t" + targetField);
		}

		List<OutputField> outputFields = evaluator.getOutputFields();
		for(OutputField outputField : outputFields){
			System.out.println("\t" + outputField);
		}

		File input = new File(args[1]);

		CsvUtil.Table inputTable = readTable(input, ",");


		InputStream is = new FileInputStream(input);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		double accurate = 0;
		double total = 0;
		int tp = 0, tn = 0, fp = 0, fn = 0;
		reader.readLine();
		while(true) {
			String line = reader.readLine();
			if (line == null)
				break;

			String message = Main.stripQuotes(line.substring(0, line.length() - 2), '"');

			String labelString = line.substring(line.length() - 1, line.length());
			//System.out.println(message+ ":"+ labelString);
			try {
				int label = Integer.parseInt(labelString );

				Map<FieldName, String> evalMap = new HashMap<FieldName, String>();
				evalMap.put(FieldName.create("x1"), message);
				Map<FieldName, ?> resultMap = evaluator.evaluate(evalMap);
				//System.out.println(message+ ":"+ label);

				Object o = resultMap.get(FieldName.create("probability(1)"));
				Double d = (Double)o;
				//System.out.println(d);
				int isSpam = d > 0.5 ? 1 : 0;
				//System.out.println("Prediction: "+isSpam);
				if (isSpam == label) {
					accurate += 1;
				}
				if (isSpam == 1 && label == 1) {
					tp += 1;
				} else if (isSpam == 1 && label == 0) {
					fp += 1;
				} else if (isSpam == 0 && label == 1) {
					fn += 1;
				} else if (isSpam == 0 && label == 0) {
					tn += 1;
				}
				total += 1;
				if (total % 100 == 0) {
					System.out.format("Processed %d rows\n", (int)total);
				}
				//System.out.println("Accuracy so far: "+(accurate/total));
				//System.out.format("Confidence Matrix: (%d, %d, %d, %d)\n", tn, fn, fp, tp );

			} catch (NumberFormatException nfe) {

				System.out.format("Skipping row %d : %s\n", (int)total, nfe.getMessage());
			}



		}

		System.out.println("Accuracy : "+(accurate/total));
		System.out.format("Confusion Matrix: (%d, %d, %d, %d)\n", tn, fn, fp, tp );


	}
}