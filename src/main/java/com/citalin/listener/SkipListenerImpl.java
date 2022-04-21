package com.citalin.listener;

import java.io.File;
import java.io.FileWriter;

import org.springframework.batch.core.SkipListener;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.citalin.model.StudentCsv;
import com.citalin.model.StudentJson;

@Component
public class SkipListenerImpl implements SkipListener<StudentCsv, StudentJson> {

	
	@Value("${batch.error.chunk.firststep.reader}")
	String chunkFirstStepReaderPath;
	
	@Value("${batch.error.chunk.firststep.processor}")
	String chunkFirstStepProcessorPath;
	
	@Value("${batch.error.chunk.firststep.writer}")
	String chunkFirstStepWriterPath;
	
	
	@Override
	public void onSkipInRead(Throwable th) {
		// here comes to logic to store the bad records
		// it could be on a file or db
		
		// in this practice it will be stored in a folder that has the structure
		// JOB
		//   STEP
		//		Reader
		//		Writer
		//		Processor
		
		if(th instanceof FlatFileParseException)
		{
			String data 	=  ((FlatFileParseException)th).getInput();
			int lineNumber 	=  ((FlatFileParseException)th).getLineNumber();
			//data +="|"+ new Date()+"|"+lineNumber;
			createFile(chunkFirstStepReaderPath, data);
		}
		
	}

	@Override
	public void onSkipInWrite(StudentJson item, Throwable t) {
		createFile(chunkFirstStepWriterPath, item.toString());
		
	}

	@Override
	public void onSkipInProcess(StudentCsv item, Throwable t) {
		createFile(chunkFirstStepProcessorPath, item.toString());
		
	}
	
	public void createFile(String filePath, String data)
	{
		try(FileWriter fileWriter = new FileWriter(new File(filePath),true)) {
			fileWriter.write(data + "\n");
		} catch (Exception e) {
			
		}
	}

}
