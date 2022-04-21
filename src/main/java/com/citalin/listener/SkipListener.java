package com.citalin.listener;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;

import org.springframework.batch.core.annotation.OnSkipInProcess;
import org.springframework.batch.core.annotation.OnSkipInRead;
import org.springframework.batch.core.annotation.OnSkipInWrite;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.citalin.model.StudentCsv;
import com.citalin.model.StudentJson;

@Component
public class SkipListener {
	
	@Value("${batch.error.chunk.firststep.reader}")
	String chunkFirstStepReaderPath;
	
	@Value("${batch.error.chunk.firststep.processor}")
	String chunkFirstStepProcessorPath;
	
	@Value("${batch.error.chunk.firststep.writer}")
	String chunkFirstStepWriterPath;
	
	
	@OnSkipInRead
	public void skipInRead(Throwable th) {
		
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
	
	@OnSkipInProcess
	public void skipInProcess(StudentCsv item,Throwable th)
	{		
		createFile(chunkFirstStepProcessorPath, item.toString());
	}
	
	@OnSkipInWrite
	public void skipInWrite(StudentJson item,Throwable th )
	{
		createFile(chunkFirstStepWriterPath, item.toString());
	}
	
	public void createFile(String filePath, String data)
	{
		try(FileWriter fileWriter = new FileWriter(new File(filePath),true)) {
			fileWriter.write(data + "\n");
		} catch (Exception e) {
			
		}
	}

}
