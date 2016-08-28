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

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import org.dmg.pmml.PMML;
import org.jpmml.evaluator.Evaluator;
import org.jpmml.evaluator.HasGroupFields;
import org.jpmml.evaluator.HasOrderFields;
import org.jpmml.evaluator.InputField;
import org.jpmml.evaluator.ModelEvaluatorFactory;
import org.jpmml.evaluator.OutputField;
import org.jpmml.evaluator.TargetField;
import org.jpmml.model.PMMLUtil;

public class Main {

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

		List<InputField> activeFields = evaluator.getActiveFields();
		for(InputField activeField : activeFields){
			System.out.println("\t" + activeField);
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
	}
}