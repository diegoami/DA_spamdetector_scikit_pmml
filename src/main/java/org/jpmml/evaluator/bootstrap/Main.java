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

import javax.xml.transform.Source;

import org.dmg.pmml.PMML;
import org.jpmml.evaluator.Evaluator;
import org.jpmml.evaluator.ModelEvaluatorFactory;
import org.jpmml.model.ImportFilter;
import org.jpmml.model.JAXBUtil;
import org.xml.sax.InputSource;

public class Main {

	static
	public void main(String... args) throws Exception {
		PMML pmml;

		try(InputStream is = new FileInputStream(args[0])){
			Source source = ImportFilter.apply(new InputSource(is));

			pmml = JAXBUtil.unmarshalPMML(source);
		}

		ModelEvaluatorFactory modelEvaluatorFactory = ModelEvaluatorFactory.newInstance();

		Evaluator evaluator = (Evaluator)modelEvaluatorFactory.newModelManager(pmml);

		System.out.println("Mining function: " + evaluator.getMiningFunction());

		System.out.println("Input schema:");
		System.out.println("\t" + "Active fields: " + evaluator.getActiveFields());
		System.out.println("\t" + "Group fields: " + evaluator.getGroupFields());

		System.out.println("Output schema:");
		System.out.println("\t" + "Target fields: " + evaluator.getTargetFields());
		System.out.println("\t" + "Output fields: " + evaluator.getOutputFields());
	}
}