package com.citalin.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.citalin.model.StudentCsv;
import com.citalin.model.StudentJdbc;
import com.citalin.model.StudentJson;

@Component
public class FirstItemProcessor implements ItemProcessor<StudentCsv,StudentJson>{

	@Override
	public StudentJson process(StudentCsv item) throws Exception {
				
		StudentJson studentJson = new StudentJson();
		if(item.getId() == 6)
		{
			System.out.println("Inside item processor");
			throw new NullPointerException("Not valid id - ");
		}
		studentJson.setId(item.getId());
		studentJson.setFirstName(item.getFirstName());
		studentJson.setLastName(item.getLastName());
		studentJson.setEmail(item.getEmail());
		
		return studentJson;
	}

}
